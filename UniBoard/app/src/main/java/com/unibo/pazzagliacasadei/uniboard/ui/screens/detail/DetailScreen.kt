package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables.DetailDescription
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables.DetailHeader
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables.DetailMapSection
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.ui.composables.BottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables.CommentItem

@Composable
fun DetailScreen(
    navController: NavHostController, detailParams: DetailParams
) {
    val post by detailParams.post
    val author by detailParams.author
    val photos by detailParams.photos
    val position by detailParams.position
    val comments by detailParams.comments
    var commentText by remember { mutableStateOf("") }

    LaunchedEffect(position) {
        Log.d("test DetailScreen", "Position: $position")
    }

    Scaffold(topBar = { TopBar(navController) }, bottomBar = { BottomBar(navController) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            post?.let {
                item {
                    DetailHeader(it, author)
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    DetailDescription(it, photos)
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    DetailMapSection(position)
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        if (!comments.isNullOrEmpty()) {
                            Text(
                                "${stringResource(R.string.comments)} (${comments!!.size})",
                                style = MaterialTheme.typography.titleMedium
                            )
                        } else {
                            Text(
                                stringResource(R.string.no_commented_yet),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (!comments.isNullOrEmpty()) {
                    items(comments!!) { comment ->
                        CommentItem(comment)
                        HorizontalDivider()
                    }
                }

                item {
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                value = commentText,
                                onValueChange = { commentText = it },
                                placeholder = { Text(stringResource(R.string.write_comment)) },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                enabled = commentText.isNotBlank(),
                                onClick = {
                                    detailParams.addComment(commentText)
                                    commentText = ""
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Send,
                                    contentDescription = stringResource(R.string.send)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}
