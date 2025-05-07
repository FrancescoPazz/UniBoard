package com.unibo.pazzagliacasadei.uniboard.data.repositories.detail

import com.unibo.pazzagliacasadei.uniboard.data.models.detail.Comment

interface IDetailRepository {
    suspend fun addComment(postId: String, text: String): Comment
}