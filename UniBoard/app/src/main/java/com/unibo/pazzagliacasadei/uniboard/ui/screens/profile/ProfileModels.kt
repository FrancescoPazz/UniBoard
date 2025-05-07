package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile

data class ProfileParams(
    val logout: () -> Unit,
    val updatePasswordWithOldPassword: (oldPassword: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) -> Unit,
)