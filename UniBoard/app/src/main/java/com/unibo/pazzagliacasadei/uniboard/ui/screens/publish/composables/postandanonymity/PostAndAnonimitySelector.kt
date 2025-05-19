package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.postandanonymity

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

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