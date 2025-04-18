package com.unibo.pazzagliacasadei.uniboard.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unibo.pazzagliacasadei.uniboard.data.models.Theme
import com.unibo.pazzagliacasadei.uniboard.ui.contracts.SettingsParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.theme.UniBoardTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

sealed interface UniBoardRoute {
    @Serializable
    data object Auth : UniBoardRoute

    @Serializable
    data object Home : UniBoardRoute

    @Serializable
    data object Profile : UniBoardRoute

    @Serializable
    data object Settings: UniBoardRoute
}

@Composable
fun UniBoardNavGraph(
    navController: NavHostController,
) {
    val authViewModel = koinViewModel<AuthViewModel>()
    val authState = authViewModel.authState.observeAsState()


    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val themeState by settingsViewModel.state.collectAsStateWithLifecycle()

    UniBoardTheme(
        darkTheme = when (themeState.theme) {
            Theme.Light -> false
            Theme.Dark -> true
            Theme.System -> isSystemInDarkTheme()
        }
    ) {
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
            composable<UniBoardRoute.Settings> {
                val settingsParams = SettingsParams(
                    changeTheme = { theme -> settingsViewModel.changeTheme(theme) },
                    themeState = themeState,
                    logout = { authViewModel.logout() },
                )
                SettingsScreen(navController, settingsParams)
            }
        }
    }
}