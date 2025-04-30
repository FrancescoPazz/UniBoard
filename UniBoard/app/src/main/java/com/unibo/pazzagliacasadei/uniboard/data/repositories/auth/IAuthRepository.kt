package com.unibo.pazzagliacasadei.uniboard.data.repositories.auth

import android.content.Context
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthResponse
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthState
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun authState(): Flow<AuthState>

    fun signIn(email: String, password: String): Flow<AuthResponse>

    fun signUp(
        email: String,
        password: String,
        username: String,
        name: String?,
        surname: String?,
        tel: String?
    ): Flow<AuthResponse>

    fun signInWithGoogle(context: Context): Flow<AuthResponse>

    fun signOut(): Flow<AuthResponse>

    fun resetPassword(email: String): Flow<AuthResponse>

    suspend fun currentUser(): User?
}
