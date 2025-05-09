package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.imageloader

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun ImageLoadButton(loadedUri: SnapshotStateList<Uri>) {
    val showModeChangeDialog = remember { mutableStateOf(false) }

    if (showModeChangeDialog.value) {
        CameraModeDialog(showModeChangeDialog, loadedUri)
    }

    OutlinedIconButton(
        onClick = { showModeChangeDialog.value = true },
        modifier = Modifier
            .height(100.dp)
            .width(100.dp),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
    ) {
        Icon(
            painter = painterResource(R.drawable.camera),
            modifier = Modifier.size(50.dp),
            contentDescription = stringResource(R.string.camera_icon)
        )
    }
}