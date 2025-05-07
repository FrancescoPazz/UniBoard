package com.unibo.pazzagliacasadei.uniboard.ui

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unibo.pazzagliacasadei.uniboard.data.models.Theme
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthState
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.DetailScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.DetailViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.DetailParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.PublishScreen
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
    data object Settings : UniBoardRoute

    @Serializable
    data object Publish: UniBoardRoute

    @Serializable
    data object Detail : UniBoardRoute
}

@Composable
fun UniBoardNavGraph(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val authViewModel = koinViewModel<AuthViewModel>()
    val authState by authViewModel.authState.observeAsState()
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val detailViewModel = koinViewModel<DetailViewModel>()
    val themeState by settingsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(UniBoardRoute.Home) {
                    popUpTo(UniBoardRoute.Auth) {
                        inclusive = true
                    }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate(UniBoardRoute.Auth) {
                    popUpTo(UniBoardRoute.Auth) {
                        inclusive = true
                    }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG)
                    .show()
            }

            else -> {}
        }
    }

    KoinContext {
        UniBoardTheme(
            darkTheme = when (themeState.theme) {
                Theme.Light -> false
                Theme.Dark -> true
                Theme.System -> isSystemInDarkTheme()
            }
        ) {
            val startRoute = when (authState) {
                is AuthState.Authenticated -> UniBoardRoute.Home
                else -> UniBoardRoute.Auth
            }

            NavHost(
                navController = navController,
                startDestination = startRoute,
            ) {
                composable<UniBoardRoute.Auth> {
                    val authParams = AuthParams(
                        authState = authViewModel.authState,
                        login = { email, password ->
                            authViewModel.login(email, password)
                        },
                        signUp = { email, password, username, name, surname, tel ->
                            authViewModel.signUp(
                                email = email,
                                password = password,
                                name = name,
                                surname = surname,
                                username = username,
                                tel = tel
                            )
                        },
                        resetPassword = { email ->
                            authViewModel.sendPasswordReset(email)
                        },
                        loginGoogle = { context ->
                            authViewModel.loginGoogle(context)
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
                            selectPost = { post ->
                                detailViewModel.setPost(post)
                            }
                        )
                    )
                }
                composable<UniBoardRoute.Profile> {
                    val profileViewModel = koinViewModel<ProfileViewModel>()
                    val profileParams = ProfileParams(
                        logout = { authViewModel.logout() },
                        updatePasswordWithOldPassword = { oldPassword, newPassword, onSuccess, onError ->
                            profileViewModel.updatePasswordWithOldPassword(
                                oldPassword = oldPassword,
                                newPassword = newPassword,
                                onSuccess = onSuccess,
                                onError = onError
                            )
                        }
                    )
                    ProfileScreen(navController, profileParams)
                }
                composable<UniBoardRoute.Settings> {
                    val settingsParams = SettingsParams(
                        changeTheme = { theme -> settingsViewModel.changeTheme(theme) },
                        themeState = themeState,
                    )
                    SettingsScreen(navController, settingsParams)
                }
                composable<UniBoardRoute.Publish> {
                    PublishScreen(navController)
                }
                composable<UniBoardRoute.Detail> {
                    DetailScreen(
                        navController,
                        detailParams = DetailParams(
                            post = detailViewModel.post.observeAsState(),
                            comments = detailViewModel.comments.observeAsState(),
                            addComment = { text ->
                                detailViewModel.addComment(text)
                            }
                        )
                    )
                }
            }
        }
    }
}