package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post

@Composable
fun DetailHeader(post: Post) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(post.content, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(4.dp))
        Text("${R.string.by} ${post.author}", style = MaterialTheme.typography.bodyMedium)
        Text(post.publishDate, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))/* TODO AsyncImage(
            model = post.imageUrl,
            contentDescription = post.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )*/
    }
}
