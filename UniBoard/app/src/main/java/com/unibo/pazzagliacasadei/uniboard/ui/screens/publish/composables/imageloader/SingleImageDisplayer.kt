package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.imageloader

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleImageDisplay(
    uri: Uri,
    remove: (uri: Uri) -> Unit,
) {
    val ctx = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(ctx).data(uri).crossfade(true).build(),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                remove(uri)
            }
    )
}