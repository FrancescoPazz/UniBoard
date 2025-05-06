package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.Announcement
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.AnnouncementsTabContent
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.Message
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.MessagesTabContent
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.ProfileHeaderSection
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.ProfileTabs
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.SettingsTabContent

@Composable
fun ProfileScreen(
    navController: NavHostController, profileParams: ProfileParams
) {
    val tabs = listOf(
        stringResource(R.string.my_announces),
        stringResource(R.string.messages),
        stringResource(R.string.user_settings)
    )
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeaderSection()
            Spacer(Modifier.height(8.dp))
            ProfileTabs(tabs, selectedTab) { selectedTab = it }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                val sampleAnnouncements = listOf(
                    Announcement("Brr Brr Patamim?", "Luca Pazzadei", "Cesena", R.drawable.logo),
                    Announcement("Aiuto ho molta baura", "Anonimo", "Napoli", R.drawable.logo),
                    Announcement("Lampada Sospensione", "€450", "Milano Est", R.drawable.logo),
                    Announcement("Poltrona Nordica", "€680", "Milano Sud", R.drawable.logo)
                )
                val sampleMessages = listOf(
                    Message("Mario Rossi", "Ciao, il divano è ancora disponibile?"),
                    Message("Giulia Bianchi", "Posso venire a vedere la lampada domani?"),
                )
                when (selectedTab) {
                    0 -> AnnouncementsTabContent(sampleAnnouncements)
                    1 -> MessagesTabContent(sampleMessages)
                    2 -> SettingsTabContent(updatePasswordWithOldPassword = profileParams.updatePasswordWithOldPassword,
                        onLogout = {
                            navController.navigate(UniBoardRoute.Auth) {
                                popUpTo(UniBoardRoute.Auth) { inclusive = true }
                                launchSingleTop = true
                            }
                            profileParams.logout()
                        })
                }
            }
        }
    }
}

