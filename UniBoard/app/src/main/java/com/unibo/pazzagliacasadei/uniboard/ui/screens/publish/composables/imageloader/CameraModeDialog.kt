package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.imageloader

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.imageloader.image.createImageUri

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CameraModeDialog(
    showDialog: MutableState<Boolean>,
    images: SnapshotStateList<Uri>,
    context: Context = LocalContext.current
) {

    val loadedPhotoUri = remember { mutableStateOf(Uri.EMPTY) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && loadedPhotoUri.value != Uri.EMPTY) {
            images.add(loadedPhotoUri.value)
        }
        showDialog.value = false
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        run {
            if (uri != null) images.add(uri)
            showDialog.value = false
        }
    }

    BasicAlertDialog(
        onDismissRequest = { showDialog.value = false },
        content = {
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(5.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.gallery),
                            contentDescription = null,
                        )
                        Text(
                            stringResource(R.string.gallery),
                            fontSize = TextUnit(32.0F, TextUnitType.Sp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .clickable {
                                val uri = createImageUri(context)
                                loadedPhotoUri.value = uri
                                cameraLauncher.launch(uri)
                            }
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.camera),
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(R.string.camera),
                            fontSize = TextUnit(32.0F, TextUnitType.Sp)
                        )
                    }
                }
            }
        }
    )
}