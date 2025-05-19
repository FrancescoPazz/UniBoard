package com.unibo.pazzagliacasadei.uniboard.ui.screens.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Conversation
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Message
import com.unibo.pazzagliacasadei.uniboard.data.repositories.chat.ChatRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.profile.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {
    val user: LiveData<User?> = userRepository.currentUserLiveData

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    val currentContactId: LiveData<String?> = chatRepository.currentContactId
    val currentContactUsername: LiveData<String?> = chatRepository.currentContactUsername

    fun setContactInfo (contactId: String, contactUsername: String) {
        chatRepository.setContactInfo(contactId, contactUsername)
    }

    fun loadMessages() {
        viewModelScope.launch {
            chatRepository.fetchmessagesWith()
                .collect { messageList ->
                    _messages.value = messageList
                }
        }
    }

    fun sendMessage(messageInput: String) {
        if (currentContactId.value == null) {
            throw IllegalStateException("No contact selected")
        }

        viewModelScope.launch {
            chatRepository.sendMessage(messageInput)
        }
    }

}