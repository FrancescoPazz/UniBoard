package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun ProfileHeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ProfileHeader(
            name = "Luca Pazzadei",
            subtitle = "Insegnante • Villamerina",
            bio = "Appassionato di kebab e chitarre. Amo la storia e Lenin.",
            imageRes = R.drawable.logo
        )
        Spacer(Modifier.height(8.dp))
        EditProfileButton(onClick = { /* TODO */ })
    }
}
