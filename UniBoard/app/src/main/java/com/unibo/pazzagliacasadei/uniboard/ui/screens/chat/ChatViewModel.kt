package com.unibo.pazzagliacasadei.uniboard.ui.screens.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Message
import com.unibo.pazzagliacasadei.uniboard.data.repositories.chat.ChatRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.profile.UserRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {
    val user = mutableStateOf<User?>(null)
    val messages: LiveData<List<Message>> = chatRepository.currentMessages

    val currentContactId: LiveData<String?> = chatRepository.currentContactId
    val currentContactUsername: LiveData<String?> = chatRepository.currentContactUsername

    init {
        viewModelScope.launch {
            user.value = userRepository.getUser()
        }
    }

    fun setContactInfo (contactId: String, contactUsername: String) {
        chatRepository.setContactInfo(contactId, contactUsername)
    }

    fun searchUsers(query: String): LiveData<List<User>> {
        val result = MutableLiveData<List<User>>()
        viewModelScope.launch {
            try {
                val users = chatRepository.searchUsers(query)
                result.value = users
            } catch (e: Exception) {
                result.value = emptyList()
            }
        }
        return result
    }

    fun loadMessages() {
        Log.d("Test", "loadMessages called with contactId: ${currentContactId.value}")
        viewModelScope.launch {
            chatRepository.fetchMessages()
        }
    }

    fun sendMessage(messageInput: String) {
        if (messageInput.isBlank() || currentContactId.value == null) {
            return
        }

        viewModelScope.launch {
            try {
                chatRepository.sendMessage(messageInput)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}