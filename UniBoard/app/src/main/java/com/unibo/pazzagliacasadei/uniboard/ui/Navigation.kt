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
import com.unibo.pazzagliacasadei.uniboard.ui.contracts.AuthParams
import com.unibo.pazzagliacasadei.uniboard.ui.contracts.ProfileParams
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
                val authParams = AuthParams(
                    authState = authViewModel.authState,
                    login = { email, password ->
                        authViewModel.login(email, password)
                    },
                    signUp = { name, surname, email, password ->
                        authViewModel.signUp(name, surname, email, password)
                    },
                )
                AuthScreen(navController, authParams)
            }
            composable<UniBoardRoute.Home> {
                HomeScreen(navController)
            }
            composable<UniBoardRoute.Profile> {
                val profileParams = ProfileParams(
                    logout = { authViewModel.logout() },
                )
                ProfileScreen(navController, profileParams)
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