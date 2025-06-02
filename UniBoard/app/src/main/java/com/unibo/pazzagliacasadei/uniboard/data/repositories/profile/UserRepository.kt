package com.unibo.pazzagliacasadei.uniboard.data.repositories.profile

import android.util.Log
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import com.unibo.pazzagliacasadei.uniboard.data.repositories.POSTS_TABLE
import com.unibo.pazzagliacasadei.uniboard.data.repositories.USERS_TABLE
import com.unibo.pazzagliacasadei.uniboard.utils.images.getPreviewImage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class UserRepository(
    private val supabase: SupabaseClient
) {
    suspend fun getUser() : User {
        val userId = supabase.auth.currentUserOrNull()?.id
        return try {
            supabase.from(USERS_TABLE).select {
                filter {
                    if (userId != null) {
                        eq("id", userId)
                    }
                }
            }.decodeSingle<User>()
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception loading user data", e)
            throw e
        }
    }

    suspend fun getUserPosts(): List<PostWithPreviewImage> {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return emptyList()

        return try {
            supabase.from(POSTS_TABLE).select {
                filter {
                    eq("author", userId)
                }
            }.decodeList<Post>().map { post -> PostWithPreviewImage(post, getPreviewImage(supabase, post.id)) }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching user posts", e)
            emptyList()
        }
    }
}
