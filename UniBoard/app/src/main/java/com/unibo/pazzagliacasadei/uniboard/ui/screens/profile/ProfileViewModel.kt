package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.repositories.auth.AuthRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun updatePasswordWithOldPassword(
        oldPassword: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit
    ) = viewModelScope.launch {
        Log.d("ProfileViewModel", "updatePasswordWithOldPassword called")
        try {
            Log.d("ProfileViewModel", "Attempting to change password")
            authRepository.changePassword(oldPassword, newPassword)
            onSuccess()
        } catch (e: Exception) {
            onError(e.message ?: "Error changing password")
        }
    }
}