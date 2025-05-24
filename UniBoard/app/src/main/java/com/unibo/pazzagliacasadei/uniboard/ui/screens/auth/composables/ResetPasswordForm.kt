package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun ResetPasswordForm(
    email: String,
    onEmailChange: (String) -> Unit,
    otp: String,
    onOtpChange: (String) -> Unit,
    sendEmail: () -> Unit,
    sendOtp: () -> Unit,
    isLoading: Boolean,
    onBack: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showOTPFields by remember { mutableStateOf(false) }

    Column {
        Text(
            text = stringResource(R.string.reset_password_title),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        AuthButton(
            text = stringResource(R.string.send_reset_email), onClick = {
                showOTPFields = true
                sendEmail()
            }, enabled = email.isNotBlank() && !isLoading
        )

        Spacer(Modifier.height(8.dp))

        if (showOTPFields) {
            Text(
                text = stringResource(R.string.enter_otp_code),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            TextField(
                value = otp,
                onValueChange = onOtpChange,
                label = { Text(stringResource(R.string.otp_code)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.new_password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            AuthButton(
                text = stringResource(R.string.send), onClick = {
                    sendOtp()
                },
            )
        }

        TextButton(onClick = onBack) {
            Text(stringResource(R.string.back_to_login))
        }
    }
}
