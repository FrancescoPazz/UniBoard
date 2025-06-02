package com.unibo.pazzagliacasadei.uniboard.data.repositories.detail

import android.util.Log
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.Comment
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.CommentWithAuthor
import com.unibo.pazzagliacasadei.uniboard.data.models.post.Photo
import com.unibo.pazzagliacasadei.uniboard.data.models.post.Position
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PositionLatLon
import com.unibo.pazzagliacasadei.uniboard.data.repositories.COMMENTS_TABLE
import com.unibo.pazzagliacasadei.uniboard.data.repositories.POSITIONS_TABLE
import com.unibo.pazzagliacasadei.uniboard.data.repositories.USERS_TABLE
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import java.time.Instant

class DetailRepository(
    private val supabase: SupabaseClient
) : IDetailRepository {

    override suspend fun getAuthor(authorId: String) : User {
        return try {
            supabase.from(USERS_TABLE).select {
                filter {
                    eq("id", authorId)
                }
            }.decodeSingle<User>()
        } catch (e: Exception) {
            Log.e("DetailRepository", "getAuthor failed", e)
            throw e
        }
    }

    override suspend fun getPostPosition(postId: String): PositionLatLon? {
        return try {
            val resp = supabase.from(POSITIONS_TABLE)
                .select {
                    filter {
                        eq("post_id", postId)
                    }
                }
            val position = resp.decodeSingleOrNull<Position>()
            val positionLatLon = position?.let { getLatLongFromWkb(it.latLng) }

            positionLatLon
        } catch (e: Exception) {
            Log.e("DetailRepository", "getPostPosition failed", e)
            throw e
        }
    }

    private suspend fun getLatLongFromWkb(wkbHex: String): PositionLatLon {
        val response = supabase.postgrest.rpc("extract_latlong_from_wkb", mapOf("wkb_hex" to wkbHex)
            ).decodeSingle<PositionLatLon>()

        return response
    }

    override suspend fun getPhotos(postId: String): List<Photo> {
        return try {
            val resp = supabase.from("photos").select {
                filter {
                    eq("post_id", postId)
                }
            }
            Log.d("DetailRepository", "getPhotos: ${resp.decodeList<Photo>()}")
            resp.decodeList<Photo>()
        } catch (e: Exception) {
            Log.e("DetailRepository", "getPhotos failed", e)
            throw e
        }
    }

    override suspend fun convertPhotos(unconvertedPhotos: List<Photo>) : List<ByteArray> {
        try {
            val convPhotos = mutableListOf<ByteArray>()
            unconvertedPhotos.forEach { photo ->
                val bucket = supabase.storage.from("post-images")
                val bytes = bucket.downloadPublic(photo.name) {
                    transform {
                        fill()
                        quality = 100
                    }
                }
                convPhotos.add(bytes)
            }
            return convPhotos
        } catch (e: Exception) {
            Log.e("DetailRepository", "convertPhotos failed", e)
            throw e
        }
    }

    override suspend fun getComments (postId: String): List<CommentWithAuthor> {
        try {
            val comments = supabase.from(COMMENTS_TABLE).select {
                filter {
                    eq("post_id", postId)
                }
            }.decodeList<Comment>()

            val authors = supabase.from(USERS_TABLE).select {
                filter {
                    isIn("id", comments.map { it.authorId })
                }
            }.decodeList<User>().associateBy { it.id }

            val commentsWithAuthors = comments.map { comment ->
                authors[comment.authorId]?.let {
                    CommentWithAuthor(
                        commentData = comment,
                        author = it
                    )
                }
            }

            return commentsWithAuthors.filterNotNull()
        } catch (e: Exception) {
            Log.e("DetailRepository", "getComments failed", e)
            throw e
        }
    }

    override suspend fun addComment(postId: String, text: String): Comment {
        val newComment = try {
            val comment = Comment(
                postId = postId,
                authorId = supabase.auth.currentUserOrNull()?.id
                    ?: throw Exception("User not logged in"),
                content = text,
                sentTime = Instant.now().toString(),
            )
            val resp = supabase.from("comments").insert(comment) { select() }
            resp.decodeSingle<Comment>()
        } catch (e: Exception) {
            Log.e("DetailRepository", "addComment failed", e)
            throw e
        }
        return newComment
    }
}