package com.unibo.pazzagliacasadei.uniboard.ui.screens.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.screens.chat.composables.ChatBottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.screens.chat.composables.MessageItem

@Composable
fun ChatScreen(
    navController: NavHostController, params: ChatParams
) {
    val messages by params.messages
    val listState = rememberLazyListState()

    LaunchedEffect(params.contactUsername.value) {
        params.contactUsername.value?.let {
            params.loadMessages()
        }
    }

    LaunchedEffect(messages?.size) {
        if (!messages.isNullOrEmpty()) {
            listState.animateScrollToItem(messages!!.size - 1)
        }
    }

    Scaffold(topBar = {
        TopBar(navController)
    }, bottomBar = {
        ChatBottomBar(params)
    }) { paddingValues ->
        if (!messages.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), state = listState
            ) {
                items(messages!!) { msg ->
                    MessageItem(msg = msg, contactUsername = params.contactUsername.value ?: "")
                }
            }
        }
    }
}

