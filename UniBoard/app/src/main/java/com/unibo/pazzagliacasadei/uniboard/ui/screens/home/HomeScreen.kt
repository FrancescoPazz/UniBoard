package com.unibo.pazzagliacasadei.uniboard.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.ui.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.composables.BottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Ciaoo!",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
                TextButton(
                    onClick = { navController.navigate(UniBoardRoute.Profile) },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Vai alla pagina di profilo",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        })
}