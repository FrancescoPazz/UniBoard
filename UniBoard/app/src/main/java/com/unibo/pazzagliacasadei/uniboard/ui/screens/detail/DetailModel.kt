package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail

import androidx.compose.runtime.State
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.CommentWithAuthor
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PositionLatLon

data class DetailParams(
    val post: State<Post?>,
    val author: State<User?>,
    val position: State<PositionLatLon?>,
    val photos: State<List<ByteArray>?>,
    val comments: State<List<CommentWithAuthor?>?>,
    val addComment: (text: String) -> Unit,
)