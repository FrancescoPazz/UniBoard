package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.postandanonymity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
    Column{
        TextField(
            value = postTitle.value,
            onValueChange = { postTitle.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.post_title)) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = postContent.value,
            onValueChange = { postContent.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            label = { Text(stringResource(R.string.post_content)) }
        )
    }
}