package com.unibo.pazzagliacasadei.uniboard.data.models.detail

import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User

data class CommentWithAuthor(
    val commentData: Comment,
    val author: User
)
