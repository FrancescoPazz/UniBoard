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
import com.unibo.pazzagliacasadei.uniboard.ui.navigation.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.composables.BottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.screens.loading.LoadingScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.PostsTabContent
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.MessagesTabContent
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.ProfileHeaderSection
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.ProfileTabs
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables.SettingsTabContent

@Composable
fun ProfileScreen(
    navController: NavHostController, profileParams: ProfileParams
) {
    val tabs = listOf(
        stringResource(R.string.my_posts),
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
            if (profileParams.user.value == null) {
                LoadingScreen()
            } else {
                ProfileHeaderSection(user = profileParams.user.value!!)
                Spacer(Modifier.height(8.dp))
                ProfileTabs(tabs, selectedTab) { selectedTab = it }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (selectedTab) {
                        0 -> PostsTabContent(navController, profileParams.userPosts, profileParams.loadUserPosts, profileParams.selectUserPost)
                        1 -> MessagesTabContent(
                            profileParams.conversations, profileParams.loadConversations
                        ) {
                            profileParams.setContactInfo(it.contactId, it.contactUsername)
                            navController.navigate(UniBoardRoute.Chat)
                        }

                        2 -> SettingsTabContent(
                            updatePasswordWithOldPassword = profileParams.updatePasswordWithOldPassword,
                            onLogout = {
                                profileParams.logout()
                                navController.navigate(UniBoardRoute.Auth) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
