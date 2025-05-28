package com.unibo.pazzagliacasadei.uniboard.data.repositories.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import com.unibo.pazzagliacasadei.uniboard.utils.images.getPreviewImage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(
    private val supabase: SupabaseClient
) {
    val currentUserLiveData = MutableLiveData<User?>()

    fun loadUserData() {
        val userId = supabase.auth.currentUserOrNull()?.id ?: Log.d(
            "UserRepository", "loadUserData: no authenticated user"
        )

        Log.d("UserRepository", "loadUserData: userId: ${supabase.auth.currentUserOrNull()?.userMetadata?.get("email")}")

        CoroutineScope(Dispatchers.IO).launch {

            Log.d("UserRepository", "loadUserData: userId: $userId")

            try {
                val resp = supabase.from("users").select {
                    filter {
                        eq("id", userId)
                    }
                }

                Log.d("UserRepository", "loadUserData: response: $resp")

                val userData = resp.decodeList<User>()
                Log.d("UserRepository", "loadUserData: $userData")
                if (userData.isNotEmpty()) {
                    currentUserLiveData.postValue(userData[0])
                } else {
                    Log.d("UserRepository", "loadUserData: no user data found")
                    currentUserLiveData.postValue(null)
                }

            } catch (e: Exception) {
                Log.e("UserRepository", "Exception loading user data", e)
                currentUserLiveData.postValue(null)
            }
        }
    }

    suspend fun getUserPosts(): List<PostWithPreviewImage> {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return emptyList()

        return try {
            val response = supabase.from("posts").select {
                filter {
                    eq("author", userId)
                }
            }

            response.decodeList<Post>().map { post -> PostWithPreviewImage(post, getPreviewImage(supabase, post.id)) }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching user posts", e)
            emptyList()
        }
    }

    fun clearUserData() {
        currentUserLiveData.value = null
    }
}
