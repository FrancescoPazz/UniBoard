package com.unibo.pazzagliacasadei.uniboard.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

sealed interface UniBoardRoute {
    @Serializable
    data object Auth : UniBoardRoute
    @Serializable
    data object Home : UniBoardRoute
    @Serializable
    data object Profile : UniBoardRoute
}

@Composable
fun UniBoardNavGraph(
    navController: androidx.navigation.NavHostController,
) {
    val authViewModel = koinViewModel<AuthViewModel>()

    NavHost(
        navController = navController,
        startDestination = UniBoardRoute.Auth,
    ) {
        composable<UniBoardRoute.Auth> {
            AuthScreen(navController, authViewModel)
        }
        composable<UniBoardRoute.Home> {
            HomeScreen(navController)
        }
        composable<UniBoardRoute.Profile> {
            ProfileScreen(navController, authViewModel)
        }
    }
}