package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.repositories.auth.AuthRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.profile.UserRepository
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: AuthRepository, private val userRepo: UserRepository
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    val authState: LiveData<AuthState> = _authState

    init {
        viewModelScope.launch {
            authRepo.authState().collect { state ->
                Log.d("AuthViewModel", "Auth state changed: $state")
                if (_authState.value != AuthState.ForgotPassword){
                    _authState.postValue(state)
                    if (state is AuthState.Authenticated) {
                        userRepo.loadUserData()
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepo.signIn(email, password).collect { resp ->
                when (resp) {
                    is AuthResponse.Success -> {
                        userRepo.loadUserData()
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
                email = email,
                password = password,
                name = name,
                surname = surname,
                username = username,
                tel = tel
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
                        userRepo.loadUserData()
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
            val ok = authRepo.resetPassword(email).single()
            if (ok is AuthResponse.Success) {
                _authState.value = AuthState.Unauthenticated
            } else if (ok is AuthResponse.Failure) {
                _authState.value = AuthState.Error("Impossible to reset password")
            }
        }
    }

    private val emailForgotPassword = MutableLiveData<String>("")
    fun sendOTPCode(email: String, otp: String) {
        viewModelScope.launch {
            val ok = authRepo.sendOtp(email, otp).single()
            if (ok is AuthResponse.Success) {
                emailForgotPassword.value = email
                _authState.value = AuthState.ForgotPassword
            } else if (ok is AuthResponse.Failure) {
                _authState.value = AuthState.Error("Impossible to send OTP code")
            }
        }
    }

    fun changeForgottenPassword(newPassword: String) {
        viewModelScope.launch {
            authRepo.changeForgottenPassword(emailForgotPassword.value!!, newPassword)
        }
    }

    fun logout() {
        viewModelScope.launch {
            val ok = authRepo.signOut().single()
            if (ok is AuthResponse.Success) {
                userRepo.clearUserData()
                _authState.value = AuthState.Unauthenticated
            } else if (ok is AuthResponse.Failure) {
                _authState.value = AuthState.Error("Logout error")
            }
        }
    }
}