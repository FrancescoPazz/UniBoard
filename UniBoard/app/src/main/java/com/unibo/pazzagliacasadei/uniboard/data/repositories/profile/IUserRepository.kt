package com.unibo.pazzagliacasadei.uniboard.data.repositories.profile

import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage

interface IUserRepository {
    suspend fun getUser() : User
    suspend fun getUserPosts(): List<PostWithPreviewImage>
}