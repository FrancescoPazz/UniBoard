package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User

@Composable
fun ProfileHeaderSection(user: User) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ProfileHeader(
            username = user.username,
            email = user.email,
            moreInfo = "${user.name} ${user.surname}",
            imageRes = R.drawable.logo
        )
        Spacer(Modifier.height(8.dp))
        EditProfileButton(onClick = { /* TODO */ })
    }
}
