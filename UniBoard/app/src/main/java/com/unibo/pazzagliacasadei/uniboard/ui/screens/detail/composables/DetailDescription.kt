package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post

@Composable
fun DetailDescription(post: Post) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = post.content, style = MaterialTheme.typography.bodyLarge
        )
    }
}
