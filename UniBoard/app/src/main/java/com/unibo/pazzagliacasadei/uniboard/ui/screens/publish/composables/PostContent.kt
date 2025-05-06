package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun PostContent(postTitle: MutableState<String>, postContent: MutableState<String>) {
    Column {
        Column {
            Text(
                text = stringResource(R.string.post_title),
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = postTitle.value,
                onValueChange = { postTitle.value = it },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.post_content),
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = postContent.value,
                onValueChange = { postContent.value = it },
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
        }
    }
}