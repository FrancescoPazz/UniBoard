package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.repositories.auth.AuthRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.profile.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val user: LiveData<User?> = userRepository.currentUserLiveData

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