package com.unibo.pazzagliacasadei.uniboard.data.repositories.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.Comment
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.post.Photo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.postgrest.from
import java.time.Instant

class DetailRepository(
    private val supabase: SupabaseClient
) : IDetailRepository {
    val currentDetailPost = MutableLiveData<Post?>()
    val currentAuthorPost = MutableLiveData<User?>()
    val comments = MutableLiveData<List<Comment>?>(emptyList())
    val photos = MutableLiveData<List<Photo>?>(emptyList())
    val convertedPhotos = MutableLiveData<List<ByteArray>?>(emptyList())

    suspend fun setPost(post: Post) {
        currentDetailPost.value = post
        val author = getAuthor()
        currentAuthorPost.postValue(author)
        val listOfComments = getComments()
        comments.postValue(listOfComments)
        val listOfPhotos = getPhotos()
        photos.value = listOfPhotos
        val listOfConvertedPhotos = convertPhotos()
        convertedPhotos.postValue(listOfConvertedPhotos)
    }

    private suspend fun getAuthor() : User {
        return try {
            supabase.from("users").select {
                filter {
                    eq("id", currentDetailPost.value?.author ?: throw Exception("No author, ${currentDetailPost.value?.author}"))
                }
            }.decodeSingle<User>()
        } catch (e: Exception) {
            Log.e("DetailRepository", "getAuthor failed", e)
            throw e
        }
    }

    private suspend fun getComments (): List<Comment> {
        Log .d("DetailRepository", "getComments: ${currentDetailPost.value?.id}")
        return try {
            val resp = supabase.from("comments").select {
                filter {
                    eq("post_id", currentDetailPost.value?.id ?: throw Exception("No post id"))
                }
            }
            resp.decodeList<Comment>()
        } catch (e: Exception) {
            Log.e("DetailRepository", "getComments failed", e)
            throw e
        }
    }

    private suspend fun getPhotos(): List<Photo> {
        return try {
            val resp = supabase.from("photos").select {
                filter {
                    eq("post_id", currentDetailPost.value?.id ?: throw Exception("No post id"))
                }
            }
            Log.d("DetailRepository", "getPhotos: ${resp.decodeList<Photo>()}")
            resp.decodeList<Photo>()
        } catch (e: Exception) {
            Log.e("DetailRepository", "getPhotos failed", e)
            throw e
        }
    }

    private suspend fun convertPhotos() : List<ByteArray> {
        try {
            val convPhotos = mutableListOf<ByteArray>()
            photos.value?.forEach { photo ->
                Log.d("DetailRepository", "photos.value: ${photos.value}")
                Log.d("DetailRepository", "convertPhotona: ${photo.name}")
                val bucket = supabase.storage.from("post-images")
                val bytes = bucket.downloadPublic(photo.name) {
                    transform {
                        fill()
                        quality = 100
                    }
                }
                convPhotos.add(bytes)
            }
            Log.d("DetailRepository", "convertPhotos: $convPhotos")
            return convPhotos
        } catch (e: Exception) {
            Log.e("DetailRepository", "convertPhotos failed", e)
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
        val updated = (comments.value ?: emptyList()) + newComment
        comments.postValue(updated)
        return newComment
    }
}