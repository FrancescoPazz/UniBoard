package com.unibo.pazzagliacasadei.uniboard.ui.screens.chat.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Message

@Composable
fun MessageItem(msg: Message, contactUsername: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = if (msg.senderId != contactUsername) "You" else contactUsername,
            fontWeight = FontWeight.Bold
        )
        Text(text = msg.content, style = MaterialTheme.typography.bodyMedium)
        Text(text = msg.sentTime.toString(), style = MaterialTheme.typography.bodySmall)
    }
}