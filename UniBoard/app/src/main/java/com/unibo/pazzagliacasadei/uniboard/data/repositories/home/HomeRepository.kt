package com.unibo.pazzagliacasadei.uniboard.data.repositories.home

import android.util.Log
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import com.unibo.pazzagliacasadei.uniboard.utils.images.getPreviewImage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class HomeRepository(
    private val supabase: SupabaseClient
) : IHomeRepository {

    override suspend fun getAllPosts(): List<PostWithPreviewImage> {
        return try {
            val resp = supabase.from("posts").select()
            val postList = resp.decodeList<Post>()
            postList.map { post -> PostWithPreviewImage(post, getPreviewImage(supabase, post.id)) }
        } catch (e: Exception) {
            Log.e("HomeRepository", "getAllPosts failed", e)
            emptyList()
        }
    }

    override suspend fun searchPosts(query: String): List<PostWithPreviewImage> {
        return try {
            val resp = supabase.from("posts").select {
                filter {
                    ilike("content", "%$query%")
                }
            }
            val postList = resp.decodeList<Post>()
            postList.map { post -> PostWithPreviewImage(post, getPreviewImage(supabase, post.id)) }
        } catch (e: Exception) {
            Log.e("HomeRepository", "searchPosts failed", e)
            emptyList()
        }
    }

    override suspend fun getRecentPosts(): List<PostWithPreviewImage> {
        return try {
            val resp = supabase.from("posts").select {
                order("publish_date", order = Order.DESCENDING)
                limit(10)
            }
            val postList = resp.decodeList<Post>()
            postList.map { post -> PostWithPreviewImage(post, getPreviewImage(supabase, post.id)) }
        } catch (e: Exception) {
            Log.e("HomeRepository", "getRecentPosts failed", e)
            emptyList()
        }
    }

    override suspend fun getPopularPosts(): List<PostWithPreviewImage> {
        TODO()
    }

    override suspend fun getNearbyPosts(): List<PostWithPreviewImage> {
        TODO()
    }
}
