package com.unibo.pazzagliacasadei.uniboard.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.unibo.pazzagliacasadei.uniboard.ui.screens.chat.ChatParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.chat.ChatScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.chat.ChatViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.DetailParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.DetailScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.DetailViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeParams
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.loading.LoadingScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.PublishScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.PublishViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.theme.AppTheme
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
    data object ForgotPassword : UniBoardRoute

    @Serializable
    data object Publish : UniBoardRoute

    @Serializable
    data object Detail : UniBoardRoute

    @Serializable
    data object Chat : UniBoardRoute
}

@Composable
fun UniBoardNavGraph(
    navController: NavHostController,
) {
    val authViewModel = koinViewModel<AuthViewModel>()
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val detailViewModel = koinViewModel<DetailViewModel>()

    val authState = authViewModel.authState.observeAsState()
    val themeState by settingsViewModel.state.collectAsStateWithLifecycle()

    KoinContext {
        AppTheme(
            darkTheme = when (themeState.theme) {
                Theme.Light -> false
                Theme.Dark -> true
                Theme.System -> isSystemInDarkTheme()
            }
        ) {
            if (authState.value == null || authState.value == AuthState.Loading) {
                LoadingScreen()
                return@AppTheme
            }
            val startRoute = when (authState.value) {
                is AuthState.Authenticated -> UniBoardRoute.Home
                is AuthState.ForgotPassword -> UniBoardRoute.ForgotPassword
                else -> UniBoardRoute.Auth
            }

            NavHost(
                navController = navController,
                startDestination = startRoute,
            ) {
                composable<UniBoardRoute.Auth> {
                    val authParams =
                        AuthParams(authState = authViewModel.authState, login = { email, password ->
                            authViewModel.login(email, password)
                        }, signUp = { email, password, username, name, surname, tel ->
                            authViewModel.signUp(
                                email = email,
                                password = password,
                                name = name,
                                surname = surname,
                                username = username,
                                tel = tel
                            )
                        }, resetPassword = { email ->
                            authViewModel.sendPasswordReset(email)
                        }, loginGoogle = { context ->
                            authViewModel.loginGoogle(context)
                        },
                        sendOtp = { email, otp ->
                            authViewModel.sendOTPCode(email, otp)
                        },
                        changeForgottenPassword = { newPassword ->
                            authViewModel.changeForgottenPassword(
                                newPassword = newPassword
                            )
                        }
                    )
                    AuthScreen(authParams)
                }
                composable<UniBoardRoute.Home> {
                    val homeViewModel: HomeViewModel = koinViewModel()
                    val posts by homeViewModel.posts.observeAsState()

                    HomeScreen(navController, HomeParams(posts = posts, searchPosts = { query ->
                        homeViewModel.searchPosts(query)
                    }, filterPosts = { filterIndex ->
                        homeViewModel.filterPosts(filterIndex)
                    }, selectPost = { post ->
                        detailViewModel.setPost(post.postData)
                    }))
                }
                composable<UniBoardRoute.Profile> {
                    val profileViewModel = koinViewModel<ProfileViewModel>()
                    val chatViewModel = koinViewModel<ChatViewModel>()

                    val profileParams = ProfileParams(
                        user = profileViewModel.user.observeAsState(),
                        logout = { authViewModel.logout() },
                        updatePasswordWithOldPassword = { oldPassword, newPassword, onSuccess, onError ->
                            profileViewModel.updatePasswordWithOldPassword(
                                oldPassword = oldPassword,
                                newPassword = newPassword,
                                onSuccess = onSuccess,
                                onError = onError
                            )
                        },
                        userPosts = profileViewModel.userPosts.observeAsState(),
                        loadUserPosts = {
                            profileViewModel.loadUserPosts()
                        },
                        selectUserPost = { post ->
                            detailViewModel.setPost(post.postData)
                        },
                        conversations = profileViewModel.conversations.observeAsState(),
                        loadConversations = {
                            profileViewModel.loadConversations()
                        },
                        setContactInfo = { contactId, contactUsername ->
                            chatViewModel.setContactInfo(contactId, contactUsername)
                        },
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
                    val publishViewModel = koinViewModel<PublishViewModel>()
                    PublishScreen(publishViewModel, navController)
                }
                composable<UniBoardRoute.Detail> {
                    DetailScreen(navController,
                        detailParams = DetailParams(
                            post = detailViewModel.post.observeAsState(),
                            author = detailViewModel.author.observeAsState(),
                            photos = detailViewModel.photos.observeAsState(),
                            comments = detailViewModel.comments.observeAsState(),
                            addComment = { text ->
                                detailViewModel.addComment(text)
                            }))
                }
                composable<UniBoardRoute.Chat> {
                    val chatViewModel = koinViewModel<ChatViewModel>()
                    ChatScreen(navController, ChatParams(
                        contactId = chatViewModel.currentContactId.observeAsState(),
                        contactUsername = chatViewModel.currentContactUsername.observeAsState(),
                        messages = chatViewModel.messages.observeAsState(),
                        loadMessages = {
                            chatViewModel.loadMessages()
                        },
                        sendMessage = { messageInput ->
                            chatViewModel.sendMessage(messageInput)
                        }
                    ))
                }
            }
        }
    }
}