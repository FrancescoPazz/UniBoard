package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.ui.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.AuthButton
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables.GoogleButton

@Composable
fun AuthScreen(
    navController: NavHostController, authParams: AuthParams
) {
    val context = LocalContext.current
    val isLoginMode = remember { mutableStateOf(true) }
    val authState = authParams.authState.observeAsState()

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val surname = remember { mutableStateOf("") }
    val tel = remember { mutableStateOf("") }
    val rememberMe = remember { mutableStateOf(false) }
    var isResetMode by remember { mutableStateOf(false) }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate(UniBoardRoute.Home) {
                    popUpTo(UniBoardRoute.Auth) { inclusive = true }
                    launchSingleTop = true
                }
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    Scaffold(content = { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isLoginMode.value) stringResource(id = R.string.login)
                else stringResource(id = R.string.signup),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (!isResetMode) {
                if (!isLoginMode.value) {
                    TextField(value = username.value,
                        onValueChange = { username.value = it },
                        label = { Text(text = stringResource(id = R.string.username)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text(text = stringResource(id = R.string.name)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(value = surname.value,
                        onValueChange = { surname.value = it },
                        label = { Text(text = stringResource(id = R.string.surname)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(
                        value = tel.value,
                        onValueChange = { tel.value = it.filter { char -> char.isDigit() } },
                        label = { Text(text = stringResource(id = R.string.tel)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                TextField(value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text(text = stringResource(id = R.string.email)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(text = stringResource(id = R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (!isLoginMode.value) {
                    TextField(
                        value = confirmPassword.value,
                        onValueChange = { confirmPassword.value = it },
                        label = { Text(text = stringResource(id = R.string.confirm_password)) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = rememberMe.value,
                            onCheckedChange = { rememberMe.value = it })
                        Text(
                            text = stringResource(R.string.remember_me),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                AuthButton(
                    text = if (isLoginMode.value) stringResource(R.string.login)
                    else stringResource(R.string.signup), onClick = {
                        if (isLoginMode.value) {
                            authParams.login(email.value, password.value)
                            email.value = ""
                            password.value = ""
                        } else {
                            if (password.value != confirmPassword.value) {
                                Toast.makeText(
                                    context, "Le password non corrispondono!", Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                authParams.signUp(
                                    email.value,
                                    password.value,
                                    username.value,
                                    name.value,
                                    surname.value,
                                    tel.value
                                )
                                isLoginMode.value = true
                                email.value = ""
                                password.value = ""
                                confirmPassword.value = ""
                                username.value = ""
                                name.value = ""
                                surname.value = ""
                                tel.value = ""
                            }
                        }
                    }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (isLoginMode.value) {
                    TextButton(onClick = { isResetMode = true }) {
                        Text(text = stringResource(R.string.forgot_password))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Text(
                    text = stringResource(R.string.or_text),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                GoogleButton(
                    text = stringResource(R.string.login_with_google),
                    onClick = { authParams.loginGoogle(context) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.terms_and_privacy),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .alpha(0.8f),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    isLoginMode.value = !isLoginMode.value
                    email.value = ""
                    password.value = ""
                    confirmPassword.value = ""
                    name.value = ""
                    surname.value = ""
                }) {
                    Text(
                        text = if (isLoginMode.value) stringResource(R.string.need_to_signup)
                        else stringResource(R.string.already_have_account)
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.reset_password_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(16.dp))
                TextField(value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text(text = stringResource(id = R.string.email)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                AuthButton(
                    text = stringResource(R.string.send_reset_email), onClick = {
                        authParams.resetPassword(email.value.trim())
                    }, enabled = email.value.isNotBlank() && authState.value != AuthState.Loading
                )
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { isResetMode = false }, colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(stringResource(R.string.back_to_login))
                }
            }
        }
    })
}
