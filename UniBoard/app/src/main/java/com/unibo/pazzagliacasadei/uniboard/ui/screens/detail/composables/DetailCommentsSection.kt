package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.Comment

@Composable
fun DetailCommentsSection(
    comments: List<Comment>?, onSend: (String) -> Unit
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        if (!comments.isNullOrEmpty()) {
            Text(
                "${stringResource(R.string.comments)} (${comments.size})",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                items(comments) { comment ->
                    CommentItem(comment)
                    HorizontalDivider()
                }
            }
            Spacer(Modifier.height(8.dp))
        } else {
            Text(
                stringResource(R.string.no_commented_yet),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
        }
        var text by remember { mutableStateOf("") }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(value = text,
                onValueChange = { text = it },
                placeholder = { Text(stringResource(R.string.write_comment)) },
                modifier = Modifier.weight(1f)
            )
            IconButton(enabled = text.isNotBlank(), onClick = {
                onSend(text)
                text = ""
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.send)
                )
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(Modifier.padding(vertical = 8.dp)) {
        /* TODO AsyncImage(
            model = comment.avatarUrl,
            contentDescription = comment.authorId,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )*/
        Spacer(Modifier.width(8.dp))
        Column {
            Text(comment.content, style = MaterialTheme.typography.bodyMedium)
            Text(
                "${comment.authorId}\n${comment.sentTime}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
