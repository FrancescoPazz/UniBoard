package com.unibo.pazzagliacasadei.uniboard.ui.composables.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.Theme

@Composable
fun RadioRow(theme: Theme, selected: Boolean, onOptionSelected: (Theme) -> Unit) {
    val themeName = when (theme) {
        Theme.Light -> stringResource(R.string.lightMode)
        Theme.Dark -> stringResource(R.string.darkMode)
        Theme.System -> stringResource(R.string.systemMode)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                onClick = { onOptionSelected(theme) },
                selected = selected,
                role = Role.RadioButton
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(themeName)
        RadioButton(
            selected = selected,
            onClick = null
        )
    }
}