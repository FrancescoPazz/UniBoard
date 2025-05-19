package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Conversation
import androidx.compose.foundation.layout.Arrangement

@Composable
fun MessagesTabContent(
    conversations: State<List<Conversation>?>,
    loadConversations: () -> Unit,
    onConversationClick: (Conversation) -> Unit
) {
    var query by remember { mutableStateOf("") }

    LaunchedEffect(loadConversations) {
        loadConversations()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(value = query,
                onValueChange = { query = it },
                placeholder = { Text(stringResource(R.string.search)) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                })
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {  },
                modifier = Modifier.height(56.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
            }
        }

        val filtered: List<Conversation>? = conversations.value?.filter { conversation ->
            conversation.contactUsername.contains(query, ignoreCase = true)
        }

        if (conversations.value == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        else if (filtered.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.no_conversations))
            }
        }
        else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(filtered) { conversation ->
                    ListItem(
                        headlineContent = { Text(conversation.contactUsername) },
                        supportingContent = { Text(conversation.lastMessage ?: "No messages") },
                        modifier = Modifier.clickable { onConversationClick(conversation) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}