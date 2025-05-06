package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.ui.composables.BottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.PostAnonymitySelector
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.PostContent

@Composable
fun PublishScreen(navController: NavHostController) {
    val postTitle = remember { mutableStateOf("") }
    val postTextContent = remember { mutableStateOf("") }
    val anonymousUser = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController) }) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)) {
            PostContent(postTitle, postTextContent)
            PostAnonymitySelector(anonymousUser)
        }
    }
}