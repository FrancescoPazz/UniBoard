package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun MessagesTabContent(
    messages: List<Message>, onSearch: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
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
                keyboardActions = KeyboardActions(onSearch = { onSearch(query) }))
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { onSearch(query) }, modifier = Modifier.height(56.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
            }
        }

        val filtered = if (query.isBlank()) messages
        else messages.filter {
            it.sender.contains(query, ignoreCase = true)
        }

        LazyColumn {
            items(filtered) { msg ->
                ListItem(headlineContent = { Text(msg.sender) },
                    supportingContent = { Text(msg.preview) },
                    modifier = Modifier.clickable { /* apri conversazione */ })
                HorizontalDivider()
            }
        }
    }
}
