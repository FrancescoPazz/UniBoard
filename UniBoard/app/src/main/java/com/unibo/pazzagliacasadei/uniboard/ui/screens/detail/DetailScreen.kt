package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables.DetailCommentsSection
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables.DetailDescription
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables.DetailHeader
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables.DetailMapSection

@Composable
fun DetailScreen(
    navController: NavHostController, detailParams: DetailParams
) {
    val post by detailParams.post
    val author by detailParams.author
    val photos by detailParams.photos
    val position by detailParams.position
    val comments by detailParams.comments

    Scaffold(topBar = { TopBar(navController) }) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            post?.let {
                DetailHeader(it, author)
                Spacer(Modifier.height(16.dp))
                DetailDescription(it, photos)
                Spacer(Modifier.height(16.dp))
                DetailMapSection(position)
                Spacer(Modifier.height(16.dp))
                DetailCommentsSection(
                    comments = comments ?: emptyList(), onSend = detailParams.addComment
                )
            }
        }
    }
}
