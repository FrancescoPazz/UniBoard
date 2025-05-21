package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.ui.composables.BottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.imageloader.ImageLoader
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location.LocationComponent
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.postandanonymity.PostAndAnonymitySelector

@Composable
fun PublishScreen(viewModel: PublishViewModel, navController: NavHostController) {
    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (viewModel.publishPhase.intValue) {
                0 -> PostAndAnonymitySelector(
                    viewModel.postTitle,
                    viewModel.postTextContent,
                    viewModel.anonymousUser
                )

                1 -> ImageLoader(viewModel.images, viewModel.removeUriFromList )
                2 -> LocationComponent(viewModel.position)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { viewModel.publishPhase.intValue-- },
                    enabled = viewModel.publishPhase.intValue > 0
                ) { Text(stringResource(R.string.back)) }
                if (viewModel.publishPhase.intValue < 2) {
                    Button(onClick = { viewModel.publishPhase.intValue++ })
                    { Text(stringResource(R.string.forward)) }
                } else {
                    Button(onClick = { }) { Text(stringResource(R.string.publish)) }
                }
            }
        }
    }
}