package com.unibo.pazzagliacasadei.uniboard.ui.screens.home

import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post

data class HomeParams(
    val posts: List<Post>,
    val searchPosts: (query: String) -> Unit,
    val filterPosts: (filterIndex: Int) -> Unit,
    val getPostDetails: (postId: String) -> Unit,
)