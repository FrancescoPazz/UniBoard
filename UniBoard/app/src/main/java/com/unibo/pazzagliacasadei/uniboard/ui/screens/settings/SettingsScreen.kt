package com.unibo.pazzagliacasadei.uniboard.ui.screens.settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.settings.ColorModeSetter

@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar(navController) },
        content = { padding ->
        Column(
            Modifier.padding(padding)
                .padding(16.dp)
        ) {
            ColorModeSetter()
        }
    })
}



