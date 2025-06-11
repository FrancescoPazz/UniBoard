package com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R


@Composable
fun AuthHeader(
    isLoginMode: Boolean, onToggleMode: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Text("UniBoard", textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,)
        Text(
            text = if (isLoginMode) stringResource(R.string.login)
            else stringResource(R.string.signup),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onToggleMode) {
            Text(
                text = if (isLoginMode) stringResource(R.string.need_to_signup)
                else stringResource(R.string.already_have_account)
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}
