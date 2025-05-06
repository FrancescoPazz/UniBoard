package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun SettingsTabContent(
    updatePasswordWithOldPassword: (
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.change_password),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text(stringResource(R.string.old_password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
        item {
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
        item {
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
        item {
            Button(
                onClick = {
                    Log.d(
                        "SettingsRepository",
                        "Attempt change password: old=$currentPassword, new=$newPassword, confirm=$confirmPassword"
                    )
                    if (newPassword == confirmPassword) {
                        updatePasswordWithOldPassword(
                            currentPassword,
                            newPassword,
                            {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.password_change_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.password_change_fail),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.password_not_match),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary,
                    containerColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(stringResource(R.string.change_password))
            }
        }
        item {
            TextButton(onClick = onLogout) {
                Text(
                    text = stringResource(R.string.logout),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
