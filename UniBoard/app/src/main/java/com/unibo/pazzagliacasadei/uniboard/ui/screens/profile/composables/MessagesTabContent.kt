package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MessagesTabContent(messages: List<Message>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        messages.forEach { msg ->
            ListItem(headlineContent = { Text(msg.sender) },
                supportingContent = { Text(msg.preview) },
                modifier = Modifier.clickable { /* TODO */ })
            HorizontalDivider()
        }
    }
}