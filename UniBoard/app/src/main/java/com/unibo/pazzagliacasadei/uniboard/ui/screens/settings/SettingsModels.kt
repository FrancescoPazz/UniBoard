package com.unibo.pazzagliacasadei.uniboard.ui.screens.settings

import com.unibo.pazzagliacasadei.uniboard.data.models.Theme

data class SettingsParams(
    val changeTheme: (Theme) -> Unit,
    val themeState: ThemeState,
    val logout: () -> Unit,
)