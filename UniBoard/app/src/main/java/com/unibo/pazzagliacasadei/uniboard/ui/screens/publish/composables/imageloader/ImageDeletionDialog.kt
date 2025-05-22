package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.imageloader

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun ImageDeletionDialog(
    dialog: MutableState<Boolean>,
    userResponse: MutableState<Boolean>
) {
    Surface {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.publish_page_image_deletion_warning),
                textAlign = TextAlign.Center,
                fontSize = TextUnit(24.0f, TextUnitType.Sp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = { userResponse.value = true; dialog.value = false }
                ) {
                    Text(stringResource(R.string.yes), fontSize = TextUnit(24.0f, TextUnitType.Sp))
                }
                TextButton(
                    onClick = { dialog.value = false; userResponse.value = false }
                ) {
                    Text(stringResource(R.string.no), fontSize = TextUnit(24.0f, TextUnitType.Sp))
                }
            }

        }
    }
}