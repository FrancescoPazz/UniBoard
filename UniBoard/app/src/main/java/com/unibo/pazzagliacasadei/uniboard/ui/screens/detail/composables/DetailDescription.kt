package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post

@Composable
fun DetailDescription(post: Post, photos: List<ByteArray>?) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            if (!photos.isNullOrEmpty()) {
                var currentPhotoIndex by remember { mutableIntStateOf(0) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    Image(
                        bitmap = photos[currentPhotoIndex].toImageBitmap(),
                        contentDescription = stringResource(R.string.post_image),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                    ) {
                        for (i in photos.indices) {
                            val color = if (i == currentPhotoIndex)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .background(
                                        color = color,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }

                    if (currentPhotoIndex > 0) {
                        IconButton(
                            onClick = { currentPhotoIndex-- },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .background(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "arrow back"
                            )
                        }
                    }

                    if (currentPhotoIndex < photos.size - 1) {
                        IconButton(
                            onClick = { currentPhotoIndex++ },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .background(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "arrow forward"
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = post.content, style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
