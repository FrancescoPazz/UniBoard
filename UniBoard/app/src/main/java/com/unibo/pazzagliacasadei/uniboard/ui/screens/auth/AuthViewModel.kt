package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.repositories.auth.AuthRepository
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    val authState: LiveData<AuthState> = _authState

    init {
        viewModelScope.launch {
            authRepo.authState().collect { state ->
                Log.d("AuthViewModel", "Auth state changed: $state")
                _authState.postValue(state)
            }
        }
    }

    fun loginAsGuest() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepo.signInAsGuest().collect { resp ->
                when (resp) {
                    is AuthResponse.Success -> {
                        _authState.value = AuthState.AnonymousAuthenticated
                    }

                    is AuthResponse.Failure -> {
                        _authState.value = AuthState.Error(resp.message)
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepo.signIn(email.trim(), password).collect { resp ->
                when (resp) {
                    is AuthResponse.Success -> {
                        _authState.value = AuthState.Authenticated
                    }

                    is AuthResponse.Failure -> {
                        _authState.value = AuthState.Error(resp.message)
                    }
                }
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        username: String,
        name: String?,
        surname: String?,
        tel: String?
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepo.signUp(
                email = email.trim(),
                password = password,
                name = name?.trim(),
                surname = surname?.trim(),
                username = username.trim(),
                tel = tel?.trim()
            ).collect { resp ->
                when (resp) {
                    is AuthResponse.Success -> {
                        _authState.value = AuthState.Unauthenticated
                    }

                    is AuthResponse.Failure -> {
                        _authState.value = AuthState.Error(resp.message)
                    }
                }
            }
        }
    }

    fun loginGoogle(context: Context) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepo.signInWithGoogle(context).collect { resp ->
                when (resp) {
                    is AuthResponse.Success -> {
                        _authState.value = AuthState.Authenticated
                    }

                    is AuthResponse.Failure -> {
                        _authState.value = AuthState.Error(resp.message)
                    }
                }
            }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            val ok = authRepo.resetPassword(email.trim()).single()
            if (ok is AuthResponse.Success) {
                _authState.value = AuthState.Unauthenticated
            } else if (ok is AuthResponse.Failure) {
                _authState.value = AuthState.Error("Impossible to reset password")
            }
        }
    }

    private val emailForgotPassword = MutableLiveData("")
    fun sendOTPCode(email: String, otp: String, newPassword: String) {
        viewModelScope.launch {
            val ok = authRepo.sendOtp(email.trim(), otp).single()
            if (ok is AuthResponse.Success) {
                emailForgotPassword.value = email.trim()
                _authState.value = AuthState.Authenticated
                changeForgottenPassword(newPassword)
            } else if (ok is AuthResponse.Failure) {
                _authState.value = AuthState.Error("Impossible to send OTP code")
            }
        }
    }

    private fun changeForgottenPassword(newPassword: String) {
        viewModelScope.launch {
            val ok = authRepo.changeForgottenPassword(newPassword).single()
            if (ok is AuthResponse.Success) {
                _authState.value = AuthState.Authenticated
            } else if (ok is AuthResponse.Failure) {
                _authState.value = AuthState.Error("Impossible to change password")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val ok = authRepo.signOut().single()
            if (ok is AuthResponse.Success) {
                _authState.value = AuthState.Unauthenticated
            } else if (ok is AuthResponse.Failure) {
                _authState.value = AuthState.Error("Logout error")
            }
        }
    }
}