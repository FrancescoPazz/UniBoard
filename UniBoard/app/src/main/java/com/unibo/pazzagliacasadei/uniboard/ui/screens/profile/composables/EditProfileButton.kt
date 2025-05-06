package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun EditProfileButton(onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier.fillMaxWidth(0.5f)
    ) {
        Text(
            text = stringResource(R.string.edit_profile).uppercase(),
            style = MaterialTheme.typography.labelLarge
        )
    }
}