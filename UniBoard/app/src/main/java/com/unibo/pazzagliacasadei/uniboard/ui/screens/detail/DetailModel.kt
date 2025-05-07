package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail

import androidx.compose.runtime.State
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.Comment
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post

data class DetailParams(
    val post: State<Post?>,
    val comments: State<List<Comment>?>,
    val addComment: (text: String) -> Unit,
)