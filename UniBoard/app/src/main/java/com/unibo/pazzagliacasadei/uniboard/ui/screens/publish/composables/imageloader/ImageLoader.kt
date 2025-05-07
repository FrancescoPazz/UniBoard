package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.imageloader

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun ImageLoader(uriList: SnapshotStateList<Uri>) {
    val openDeletionDialog = remember { mutableStateOf(false) }
    Column {
        Text(text = stringResource(R.string.publish_page_photos))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            uriList.forEach { uri ->
                SingleImageDisplay(uri, remove = { uriList.remove(it) }, openDeletionDialog)
            }
            if (uriList.size < 4) {
                ImageLoadButton(uriList)
            }
        }
        Text(text = stringResource(R.string.publish_page_max_four))
    }

}