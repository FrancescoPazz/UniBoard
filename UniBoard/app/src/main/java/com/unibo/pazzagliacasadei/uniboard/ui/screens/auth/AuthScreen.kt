package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.AuthHeader
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.LoginForm
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.ResetPasswordForm
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.SignUpForm

@SuppressLint("DiscouragedApi")
@Composable
fun AuthScreen(
    authParams: AuthParams
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

    var otp by remember { mutableStateOf("") }
    var newForgotPassword by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var dialogId by remember { mutableStateOf("") }
    var dialogError by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            showDialog = true
            dialogId = (authState as AuthState.Error).message.split(" ")[0]
            dialogError = (authState as AuthState.Error).message
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(R.string.auth_error)) },
            text = {
                val resId = remember(dialogId) {
                    context.resources.getIdentifier(
                        dialogId,
                        "string",
                        context.packageName
                    )
                }
                val titleText = if (resId != 0) {
                    stringResource(resId)
                } else {
                    dialogError
                }
                Text(titleText) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(id = android.R.string.ok))
                }
            },
            icon = { Icon(Icons.Filled.Info, contentDescription = "Error Icon") }
        )
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AuthHeader(isLoginMode) {
                isLoginMode = !isLoginMode
                isResetMode = false
            }

            if (isResetMode) {
                ResetPasswordForm(
                    email = email,
                    onEmailChange = { email = it },
                    otp = otp,
                    onOtpChange = { otp = it },
                    onNewForgotPasswordChange = { newForgotPassword = it },
                    sendEmailResetPassword = { authParams.resetPassword(email) },
                    sendOtp = { authParams.sendOtp(email, otp, newForgotPassword) },
                    isLoading = authState == AuthState.Loading,
                    onBack = { isResetMode = false })
            } else if (isLoginMode) {
                LoginForm(email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    onSubmit = { authParams.login(email, password) },
                    onForgotPassword = { isResetMode = true },
                    onGoogleLogin = { authParams.loginGoogle(context) },
                    isLoading = authState == AuthState.Loading,
                    loginAsGuest = { authParams.loginAsGuest() }
                )
            } else {
                SignUpForm(name = name,
                    onNameChange = { name = it },
                    surname = surname,
                    onSurnameChange = { surname = it },
                    tel = tel,
                    onTelChange = { tel = it },
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
                        confirmPassword = ""
                    },
                    isLoading = authState == AuthState.Loading
                )
            }
        }
    }
}

