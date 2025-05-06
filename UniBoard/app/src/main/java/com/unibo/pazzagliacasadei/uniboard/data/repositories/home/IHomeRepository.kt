package com.unibo.pazzagliacasadei.uniboard.data.repositories.home

import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post

interface IHomeRepository {
    suspend fun getAllPosts(): List<Post>
    suspend fun searchPosts(query: String): List<Post>
    suspend fun getRecentPosts(): List<Post>
    suspend fun getPopularPosts(): List<Post>
    suspend fun getNearbyPosts(): List<Post>
}
