package com.unibo.pazzagliacasadei.uniboard.ui.screens.chat

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
    val user: LiveData<User?> = userRepository.currentUserLiveData

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    val currentContactId: LiveData<String?> = chatRepository.currentContactId
    val currentContactUsername: LiveData<String?> = chatRepository.currentContactUsername

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
        viewModelScope.launch {
            chatRepository.fetchmessagesWith()
                .collect { messageList ->
                    _messages.value = messageList
                }
        }
    }

    fun sendMessage(messageInput: String) {
        if (messageInput.isBlank() || currentContactId.value == null) {
            return
        }

        viewModelScope.launch {
            try {
                val userId = userRepository.currentUserLiveData.value?.id
                    ?: throw IllegalStateException("No authenticated user")
                val contactId = currentContactId.value!!
                val newMessage = Message(
                    senderId = userId,
                    receiverId = contactId,
                    content = messageInput,
                    sentTime = kotlinx.datetime.Clock.System.now()
                )
                val currentMessages = _messages.value ?: emptyList()
                _messages.value = currentMessages + newMessage

                chatRepository.sendMessage(messageInput)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}