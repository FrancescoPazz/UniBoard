package com.unibo.pazzagliacasadei.uniboard.data.repositories.detail

import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.Comment
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.CommentWithAuthor
import com.unibo.pazzagliacasadei.uniboard.data.models.post.Photo
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PositionLatLon

interface IDetailRepository {
    suspend fun getAuthor(authorId: String) : User
    suspend fun getPostPosition(postId: String): PositionLatLon?
    suspend fun getPhotos(postId: String): List<Photo>
    suspend fun convertPhotos(unconvertedPhotos: List<Photo>) : List<ByteArray>
    suspend fun addComment(postId: String, text: String): Comment
    suspend fun getComments (postId: String): List<CommentWithAuthor>
}