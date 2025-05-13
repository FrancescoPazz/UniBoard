package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile

import androidx.compose.runtime.State
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User

data class ProfileParams(
    val user: State<User?>,
    val logout: () -> Unit,
    val updatePasswordWithOldPassword: (oldPassword: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) -> Unit,
)