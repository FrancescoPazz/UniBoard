package com.unibo.pazzagliacasadei.uniboard.data.repositories.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.Comment
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import java.time.Instant
import java.util.UUID

class DetailRepository(
    private val supabase: SupabaseClient
) : IDetailRepository {
    val currentDetailPost = MutableLiveData<Post?>()
    val comments = MutableLiveData<List<Comment>?>(emptyList())

    suspend fun setPost(post: Post) {
        currentDetailPost.postValue(post)
        val list = getComments(post.id)
        comments.postValue(list)
    }

    private suspend fun getComments (postId: String): List<Comment> {
        Log .d("DetailRepository", "getComments: $postId")
        return try {
            val resp = supabase.from("comments").select {
                filter {
                    eq("post_id", postId)
                }
            }
            resp.decodeList<Comment>()
        } catch (e: Exception) {
            Log.e("DetailRepository", "getComments failed", e)
            throw e
        }
    }

    override suspend fun addComment(postId: String, text: String): Comment {
        val newComment = try {
            val comment = Comment(
                id = UUID.randomUUID().toString(),
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