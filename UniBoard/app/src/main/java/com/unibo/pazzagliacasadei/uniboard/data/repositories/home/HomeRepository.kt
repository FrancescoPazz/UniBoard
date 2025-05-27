package com.unibo.pazzagliacasadei.uniboard.data.repositories.home

import android.util.Log
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage

class HomeRepository(
    private val supabase: SupabaseClient
) : IHomeRepository {

    override suspend fun getAllPosts(): List<PostWithPreviewImage> {
        return try {
            val resp = supabase.from("posts").select()
            val postList = resp.decodeList<Post>()
            postList.map { post -> PostWithPreviewImage(post, getPreviewImage(post.id)) }
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
            postList.map { post -> PostWithPreviewImage(post, getPreviewImage(post.id)) }
        } catch (e: Exception) {
            Log.e("HomeRepository", "searchPosts failed", e)
            emptyList()
        }
    }

    private suspend fun getPreviewImage(postId: String): ByteArray {
        try {
            val bucket = supabase.storage.from("post-images")
            val bytes = bucket.downloadPublic("${postId}/0.jpg") {
                transform {
                    fill()
                    quality = 100
                }
            }
            return bytes
        } catch (e: Exception) {
            Log.e("DetailRepository", "convertPhotos failed", e)
            throw e
        }
    }

    override suspend fun getRecentPosts(): List<PostWithPreviewImage> {
        return try {
            val resp = supabase.from("posts").select {
                order("publish_date", order = Order.DESCENDING)
                limit(10)
            }
            val postList = resp.decodeList<Post>()
            postList.map { post -> PostWithPreviewImage(post, getPreviewImage(post.id)) }
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
