package com.unibo.pazzagliacasadei.uniboard.ui.contracts

import com.unibo.pazzagliacasadei.uniboard.data.models.Theme
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.ThemeState

data class SettingsParams(
    val changeTheme: (Theme) -> Unit,
    val themeState: ThemeState,
    val logout: () -> Unit,
)