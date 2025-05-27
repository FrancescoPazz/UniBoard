package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.imageloader

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun ImageLoader(uriList: SnapshotStateList<Uri>, removeFunction: (uri: Uri) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.publish_page_photos),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            uriList.fastForEach<Uri> { uri ->
                SingleImageDisplay(uri, removeFunction)
            }
            if (uriList.size < 4) {
                ImageLoadButton(uriList)
            }
        }
        Text(
            text = stringResource(R.string.publish_page_max_four),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }

}