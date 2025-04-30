package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth

import androidx.lifecycle.LiveData

data class AuthParams(
    val authState: LiveData<AuthState>,
    val login: (email: String, password: String) -> Unit,
    val signUp: (
        email: String, password: String, username: String, name: String?, surname: String?, tel: String?
    ) -> Unit,
    val resetPassword: (email: String) -> Unit
)

sealed class AuthState {
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Failure(val message: String) : AuthResponse
}