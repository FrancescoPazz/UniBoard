package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User

@Composable
fun ProfileHeaderSection(user: User) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ProfileHeader(
            username = user.username,
            email = user.email,
            moreInfo = "${user.name} ${user.surname}"
        )
    }
}
