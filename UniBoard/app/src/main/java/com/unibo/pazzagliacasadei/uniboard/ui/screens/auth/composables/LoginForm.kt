package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun LoginForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onForgotPassword: () -> Unit,
    onGoogleLogin: () -> Unit,
    isLoading: Boolean,
    loginAsGuest: () -> Unit
) {
    Column {
        TextField(
            value = email, onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        TextField(
            value = password, onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        AuthButton(
            text = stringResource(R.string.login),
            onClick = onSubmit,
            enabled = !isLoading
        )
        Spacer(Modifier.height(8.dp))
        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(textAlign = TextAlign.Center, text = stringResource(R.string.forgot_password))
        }
        Spacer(Modifier.height(16.dp))
        DividerWithText(text = stringResource(R.string.or_text))
        Spacer(Modifier.height(16.dp))
        GoogleButton(
            text = stringResource(R.string.login_with_google),
            onClick = onGoogleLogin
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))
        TextButton(
            onClick = { loginAsGuest() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(textAlign = TextAlign.Center, text = stringResource(R.string.enter_as_guest))
        }
    }
}
