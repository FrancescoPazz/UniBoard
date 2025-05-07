package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.imageloader

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
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun ImageDeletionDialog(dialog: MutableState<Boolean>, removeFunction: () -> Unit) {
    Surface {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding((16 * 2).dp)
        ) {
            Text(
                text = stringResource(R.string.publish_page_image_deletion_warning),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { removeFunction(); dialog.value = false }
                ) {
                    Text(stringResource(R.string.yes))
                }
                TextButton(
                    onClick = { dialog.value = false }
                ) {
                    Text(stringResource(R.string.no))
                }
            }

        }
    }
}