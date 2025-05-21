package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.repositories.publish.IPublishRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.publish.PublishRepository
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng

class PublishViewModel(val publishRepository: IPublishRepository) : ViewModel() {
    val images = mutableStateListOf<Uri>()
    val postTitle = mutableStateOf("")
    val postTextContent = mutableStateOf("")
    val anonymousUser = mutableStateOf(false)
    val position = mutableStateOf<LatLng?>(null)
    val publishPhase = mutableIntStateOf(0)

    val removeUriFromList = fun(uri: Uri) {
        images.remove(uri)
    }

    fun publishPost(){
        viewModelScope.launch {
            publishRepository.publishPost(
                images.toList()
            )
        }
    }
}