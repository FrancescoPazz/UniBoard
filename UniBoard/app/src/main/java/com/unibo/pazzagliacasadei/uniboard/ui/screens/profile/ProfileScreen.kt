package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.ui.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.composables.BottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.profile.Announcement
import com.unibo.pazzagliacasadei.uniboard.ui.composables.profile.AnnouncementsTabContent
import com.unibo.pazzagliacasadei.uniboard.ui.composables.profile.EditProfileButton
import com.unibo.pazzagliacasadei.uniboard.ui.composables.profile.Message
import com.unibo.pazzagliacasadei.uniboard.ui.composables.profile.MessagesTabContent
import com.unibo.pazzagliacasadei.uniboard.ui.composables.profile.ProfileHeader
import com.unibo.pazzagliacasadei.uniboard.ui.composables.profile.ProfileTabs
import com.unibo.pazzagliacasadei.uniboard.ui.composables.profile.SettingsTabContent
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController, authViewModel: AuthViewModel
) {
    val tabs = listOf(
        stringResource(R.string.my_announces),
        stringResource(R.string.messages),
        stringResource(R.string.user_settings)
    )
    var selectedTab by remember { mutableIntStateOf(0) }

    val sampleAnnouncements = listOf( // TODO
        Announcement("Brr Brr Patamim?", "Luca Pazzadei", "Cesena", R.drawable.logo),
        Announcement("Aiuto ho molta baura", "Anonimo", "Naboli", R.drawable.logo),
        Announcement("Lampada Sospensione", "€450", "Milano Est", R.drawable.logo),
        Announcement("Poltrona Nordica", "€680", "Milano Sud", R.drawable.logo)
    )

    val sampleMessages = listOf(
        Message("Mario Rossi", "Ciao, il divano è ancora disponibile?"),
        Message("Giulia Bianchi", "Posso venire a vedere la lampada domani?"),
    )

    Scaffold(topBar = { TopBar(navController) }, bottomBar = { BottomBar(navController) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader( // TODO
                name = "Luca Pazzadei",
                subtitle = "Insegnante • Villamerina",
                bio = "Appassionato di kebab e chitarre. Amo la storia e Lenin.",
                imageRes = R.drawable.logo
            )

            Spacer(modifier = Modifier.height(16.dp))

            EditProfileButton(onClick = {
                // TODO
            })

            Spacer(modifier = Modifier.height(24.dp))

            ProfileTabs(
                tabs = tabs,
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it })

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> AnnouncementsTabContent(announcements = sampleAnnouncements)
                1 -> MessagesTabContent(messages = sampleMessages)
                2 -> SettingsTabContent(onLogout = {
                    authViewModel.logout()
                    navController.navigate(UniBoardRoute.Auth) {
                        popUpTo(UniBoardRoute.Auth) { inclusive = true }
                        launchSingleTop = true
                    }
                })
            }
        }
    }
}