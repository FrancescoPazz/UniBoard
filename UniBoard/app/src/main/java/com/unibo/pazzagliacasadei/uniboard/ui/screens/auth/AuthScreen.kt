package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.ui.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.AuthHeader
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.LoginForm
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.ResetPasswordForm
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.SignUpForm

@Composable
fun AuthScreen(
    navController: NavHostController, authParams: AuthParams
) {
    val context = LocalContext.current
    var isLoginMode by remember { mutableStateOf(true) }
    var isResetMode by remember { mutableStateOf(false) }
    val authState by authParams.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var tel by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(UniBoardRoute.Home) {
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

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader(isLoginMode) {
                isLoginMode = !isLoginMode
                isResetMode = false
            }

            if (isResetMode) {
                ResetPasswordForm(email = email,
                    onEmailChange = { email = it },
                    onSubmit = { authParams.resetPassword(email) },
                    isLoading = authState == AuthState.Loading,
                    onBack = { isResetMode = false })
            } else if (isLoginMode) {
                LoginForm(email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    rememberMe = rememberMe,
                    onRememberMeChange = { rememberMe = it },
                    onSubmit = { authParams.login(email, password) },
                    onForgotPassword = { isResetMode = true },
                    onGoogleLogin = { authParams.loginGoogle(context) },
                    isLoading = authState == AuthState.Loading
                )
            } else {
                SignUpForm(name = name,
                    onNameChange = { name = it },
                    surname = surname,
                    onSurnameChange = { surname = it },
                    username = username,
                    onUsernameChange = { username = it },
                    email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = { confirmPassword = it },
                    passwordsMatch = password.isNotBlank() && password == confirmPassword,
                    onSubmit = {
                        authParams.signUp(
                            email, password, username, name, surname, tel
                        )
                        isLoginMode = true
                        email = ""; password = ""; confirmPassword = ""
                        username = ""; name = ""; surname = ""; tel = ""
                    },
                    isLoading = authState == AuthState.Loading
                )
            }
        }
    }
}

