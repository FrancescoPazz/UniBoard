package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.ui.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController, authViewModel: AuthViewModel
) {
    Scaffold(topBar = { TopBar(navController) }, content = { paddingValues ->
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
                onClick = {
                    authViewModel.logout()
                    navController.navigate(UniBoardRoute.Auth) {
                        popUpTo(UniBoardRoute.Auth) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }, modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.logout),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    })
}
