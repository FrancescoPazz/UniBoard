package com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables.toImageBitmap

@Composable
fun PostCard(
    post: PostWithPreviewImage, onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }) {
        Column {
            Image(
                bitmap = post.postImagePreview.toImageBitmap(),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.postData.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = post.postData.date,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}