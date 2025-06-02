package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import androidx.compose.foundation.Image
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
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post

@Composable
fun DetailDescription(post: Post, photos: List<ByteArray>?) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = post.content, style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(8.dp))
        if (!photos.isNullOrEmpty()) {
            photos.forEach { photo ->
                Image(
                    bitmap = photo.toImageBitmap(),
                    contentDescription = stringResource(R.string.post_image),
                    modifier = Modifier.height(250.dp)
                )
            }
        }
    }
}
