package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth

import android.content.Context
import androidx.lifecycle.LiveData

data class AuthParams(
    val authState: LiveData<AuthState>,
    val login: (email: String, password: String) -> Unit,
    val loginAsGuest: () -> Unit,
    val signUp: (
        email: String, password: String, username: String, name: String?, surname: String?, tel: String?
    ) -> Unit,
    val resetPassword: (email: String) -> Unit,
    val sendOtp: (email: String, otp: String, newPassword: String) -> Unit,
    val loginGoogle: (context: Context) -> Unit,
)

sealed class AuthState {
    data object Authenticated : AuthState()
    data object AnonymousAuthenticated : AuthState()
    data object Unauthenticated : AuthState()
    data object Registered : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Failure(val message: String) : AuthResponse
}