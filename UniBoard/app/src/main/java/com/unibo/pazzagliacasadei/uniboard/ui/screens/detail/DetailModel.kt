package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.CommentWithAuthor
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PositionLatLon
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthState

data class DetailParams(
    val authState: State<AuthState?>,
    val post: State<Post?>,
    val author: State<User?>,
    val position: State<PositionLatLon?>,
    val photos: SnapshotStateList<ByteArray>,
    val comments: SnapshotStateList<CommentWithAuthor>,
    val addComment: (text: String) -> Unit,
    val logout: () -> Unit,
)