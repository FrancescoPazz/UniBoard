package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.phases

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.postandanonymity.PostAnonymitySelector
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.postandanonymity.PostContent

@Composable
fun PostAndAnonymitySelector(
    postTitle: MutableState<String>,
    postContent: MutableState<String>,
    anonymousState: MutableState<Boolean>
) {
    Column {
        PostContent(
            postTitle = postTitle,
            postContent = postContent
        )
        PostAnonymitySelector(anonymousState)
    }
}