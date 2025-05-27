package com.unibo.pazzagliacasadei.uniboard.data.repositories.home

import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage

interface IHomeRepository {
    suspend fun getAllPosts(): List<PostWithPreviewImage>
    suspend fun searchPosts(query: String): List<PostWithPreviewImage>
    suspend fun getRecentPosts(): List<PostWithPreviewImage>
    suspend fun getPopularPosts(): List<PostWithPreviewImage>
    suspend fun getNearbyPosts(): List<PostWithPreviewImage>
}
