package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.postandanonymity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun PostAnonymitySelector(anonymous: MutableState<Boolean>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.anonymous_post)
        )
        Switch(
            checked = anonymous.value,
            onCheckedChange = { anonymous.value = it }
        )
    }
}