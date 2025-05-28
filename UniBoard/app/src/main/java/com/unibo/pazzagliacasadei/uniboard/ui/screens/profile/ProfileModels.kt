package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile

import androidx.compose.runtime.State
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Conversation

data class ProfileParams(
    val user: State<User?>,
    val logout: () -> Unit,
    val updatePasswordWithOldPassword: (oldPassword: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) -> Unit,
    val loadUserPosts: () -> Unit,
    val userPosts: State<List<PostWithPreviewImage>?>,
    val selectUserPost: (post: PostWithPreviewImage) -> Unit,
    val conversations: State<List<Conversation>?>,
    val loadConversations: () -> Unit,
    val setContactInfo: (contactId: String, contactUsername: String) -> Unit
)