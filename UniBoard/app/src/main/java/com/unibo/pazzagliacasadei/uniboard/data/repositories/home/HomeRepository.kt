package com.unibo.pazzagliacasadei.uniboard.data.repositories.home

import android.util.Log
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class HomeRepository(
    private val supabase: SupabaseClient
) : IHomeRepository {

    override suspend fun getAllPosts(): List<Post> {
        return try {
            val resp = supabase.from("posts").select()
            resp.decodeList<Post>()
        } catch (e: Exception) {
            Log.e("HomeRepository", "getAllPosts failed", e)
            emptyList()
        }
    }

    override suspend fun searchPosts(query: String): List<Post> {
        return try {
            val resp = supabase.from("posts").select {
                filter {
                    ilike("content", "%$query%")
                }
            }
            resp.decodeList<Post>()
        } catch (e: Exception) {
            Log.e("HomeRepository", "searchPosts failed", e)
            emptyList()
        }
    }

    override suspend fun getRecentPosts(): List<Post> {
        return try {
            val resp = supabase.from("posts").select {
                order("publish_date", order = Order.DESCENDING)
                limit(10)
            }
            resp.decodeList<Post>()
        } catch (e: Exception) {
            Log.e("HomeRepository", "getRecentPosts failed", e)
            emptyList()
        }
    }

    override suspend fun getPopularPosts(): List<Post> {
        TODO()
    }

    override suspend fun getNearbyPosts(): List<Post> {
        TODO()
    }
}
