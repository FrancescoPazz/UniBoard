package com.unibo.pazzagliacasadei.uniboard.ui.composables.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun SettingsTabContent(onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = onLogout) {
            Text(
                text = stringResource(R.string.logout), style = MaterialTheme.typography.titleLarge
            )
        }
    }
}