package com.unibo.pazzagliacasadei.uniboard.ui.screens.chat

import androidx.compose.runtime.State
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Message

data class ChatParams(
    val contactId: State<String?>,
    val contactUsername: State<String?>,
    val messages: State<List<Message>?>,
    val loadMessages: () -> Unit,
    val sendMessage: (messageInput: String) -> Unit,
)
