package com.unibo.pazzagliacasadei.uniboard.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unibo.pazzagliacasadei.uniboard.data.models.Theme
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.theme.UniBoardTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext

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

    KoinContext {
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
                        signUp = { email, password, username, name, surname, tel ->
                            authViewModel.signUp(email = email,
                                password = password,
                                name = name,
                                surname = surname,
                                username = username,
                                tel = tel)
                        },
                        resetPassword = { email ->
                            authViewModel.sendPasswordReset(email)
                        }
                    )
                    AuthScreen(navController, authParams)
                }
                composable<UniBoardRoute.Home> {
                    val homeViewModel: HomeViewModel = koinViewModel()
                    val posts by homeViewModel.posts.collectAsState()

                    HomeScreen(
                        navController,
                        HomeParams(
                            posts = posts,
                            searchPosts = { query ->
                                homeViewModel.searchPosts(query)
                            },
                            filterPosts = { filterIndex ->
                                homeViewModel.filterPosts(filterIndex)
                            },
                            getPostDetails = { postId ->
                                navController.navigate("post/$postId")
                            }
                        )
                    )
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
}