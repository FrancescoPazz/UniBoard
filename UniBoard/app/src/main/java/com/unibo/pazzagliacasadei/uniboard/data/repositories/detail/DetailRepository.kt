package com.unibo.pazzagliacasadei.uniboard.data.repositories.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.Comment
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import java.time.Instant

class DetailRepository(
    private val supabase: SupabaseClient
) : IDetailRepository {
    val currentDetailPost = MutableLiveData<Post?>()
    val currentAuthorPost = MutableLiveData<User?>()
    val comments = MutableLiveData<List<Comment>?>(emptyList())

    suspend fun setPost(post: Post) {
        currentDetailPost.value = post
        val author = getAuthor()
        currentAuthorPost.postValue(author)
        val listOfComments = getComments()
        comments.postValue(listOfComments)
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