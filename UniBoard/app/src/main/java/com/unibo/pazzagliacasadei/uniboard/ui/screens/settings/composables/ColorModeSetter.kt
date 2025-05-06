package com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.Theme
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsParams

@Composable
fun ColorModeSetter(settingsParams: SettingsParams) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), modifier = Modifier.selectableGroup()
    ) {
        Column {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(R.string.theme),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
            )
            Theme.entries.forEach { theme ->
                RadioRow(theme, theme == settingsParams.themeState.theme, settingsParams.changeTheme)
            }
        }
    }
}