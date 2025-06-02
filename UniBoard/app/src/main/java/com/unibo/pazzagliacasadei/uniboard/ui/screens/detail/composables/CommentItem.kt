package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.CommentWithAuthor

@Composable
fun CommentItem(comment: CommentWithAuthor) {
    Row(Modifier.padding(vertical = 8.dp)) {
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                comment.author.username,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(comment.commentData.content, style = MaterialTheme.typography.bodyMedium)
            Text(
                comment.commentData.sentTime,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
