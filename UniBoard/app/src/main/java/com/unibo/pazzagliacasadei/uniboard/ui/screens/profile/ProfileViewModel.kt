package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Conversation
import com.unibo.pazzagliacasadei.uniboard.data.repositories.auth.AuthRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.chat.ChatRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.profile.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {
    val user = mutableStateOf<User?>(null)

    private val _conversations = MutableLiveData<List<Conversation>>()
    val conversations: LiveData<List<Conversation>> = _conversations

    private val _userPosts = MutableLiveData<List<PostWithPreviewImage>?>()
    val userPosts: LiveData<List<PostWithPreviewImage>?> = _userPosts

    init {
        viewModelScope.launch {
            user.value = userRepository.getUser()
        }
    }

    fun loadUserPosts() {
        viewModelScope.launch {
            try {
                _userPosts.value = userRepository.getUserPosts()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading announcements: ${e.message}")
                _userPosts.value = null
            }
        }
    }

    fun loadConversations() {
        viewModelScope.launch {
            try {
                _conversations.value = chatRepository.fetchAllConversations()
            } catch (e: Exception) {
                throw e
            }
        }
    }

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