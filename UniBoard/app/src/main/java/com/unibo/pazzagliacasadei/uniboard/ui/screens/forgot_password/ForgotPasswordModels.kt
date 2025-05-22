package com.unibo.pazzagliacasadei.uniboard.ui.screens.forgot_password

data class ForgotPasswordParams(
    val changeForgottenPassword: (newPassword: String) -> Unit,
)