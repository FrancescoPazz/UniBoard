package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.maplibre.android.geometry.LatLng

class PublishViewModel : ViewModel() {
    val images = mutableStateListOf<Uri>()
    val postTitle = mutableStateOf("")
    val postTextContent = mutableStateOf("")
    val anonymousUser = mutableStateOf(false)
    val position = mutableStateOf<LatLng?>(null)
    val publishPhase = mutableIntStateOf(0)
}