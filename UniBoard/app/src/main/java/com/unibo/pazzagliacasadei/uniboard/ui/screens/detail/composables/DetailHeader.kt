package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post

@Composable
fun DetailHeader(post: Post, author : User?) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(post.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(4.dp))
        Text("${stringResource(R.string.by)} ${author?.username ?: post.author}", style = MaterialTheme.typography.bodyMedium)
        Text(post.date, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        // TODO: add image method to retrieve post images.
    }
}
