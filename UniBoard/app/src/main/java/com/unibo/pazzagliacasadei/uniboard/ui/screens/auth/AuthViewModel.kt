package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.unibo.pazzagliacasadei.uniboard.ui.contracts.AuthState

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (auth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("ERROR : Empty fields")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(
                    task.exception?.message ?: "ERROR : Login failed"
                )
            }
        }
    }

    fun signUp(email: String, password: String, name: String, surname: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("ERROR : Empty fields")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                _authState.value = AuthState.Error(
                    task.exception?.message ?: "ERROR : Signup failed"
                )
            }
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun sendPasswordResetEmail(email: String) {
        if (email.isEmpty()) {
            _authState.value = AuthState.Error("ERROR : Empty email")
            return
        }

        _authState.value = AuthState.Loading
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Unauthenticated
            } else {
                _authState.value = AuthState.Error(
                    task.exception?.message ?: "ERROR : Password reset failed"
                )
            }
        }
    }
}
