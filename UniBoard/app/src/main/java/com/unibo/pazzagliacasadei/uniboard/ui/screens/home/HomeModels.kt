package com.unibo.pazzagliacasadei.uniboard.ui.screens.home

import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage

data class HomeParams(
    val posts: List<PostWithPreviewImage>?,
    val searchPosts: (query: String) -> Unit,
    val filterPosts: (filterIndex: Int) -> Unit,
    val selectPost: (post: PostWithPreviewImage) -> Unit,
)