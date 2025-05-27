package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.repositories.publish.PublicationErrors
import com.unibo.pazzagliacasadei.uniboard.data.repositories.publish.PublishRepository
import com.unibo.pazzagliacasadei.uniboard.utils.images.uriToBytes
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng

class PublishViewModel(val publishRepository: PublishRepository) : ViewModel() {
    val images = mutableStateListOf<Uri>()
    val postTitle = mutableStateOf("")
    val postTextContent = mutableStateOf("")
    val anonymousUser = mutableStateOf(false)
    val position = mutableStateOf<LatLng?>(null)
    val publishPhase = mutableIntStateOf(0)

    val removeUriFromList = fun(uri: Uri) {
        images.remove(uri)
    }

    fun clearAllFields() {
        images.clear()
        postTitle.value = ""
        postTextContent.value = ""
        anonymousUser.value = false
        position.value = null
        publishPhase.intValue = 0
    }

    fun publishPost(
        context: Context, onSuccess: () -> Unit,
        onFailure: (error: PublicationErrors) -> Unit
    ) =
        viewModelScope.launch {
            try {
                val imagesBytesList = mutableListOf<ByteArray>()
                for (imgUri in images) {
                    imagesBytesList.add(uriToBytes(imgUri, context))
                }
                publishRepository.publishPost(
                    postTitle.value,
                    postTextContent.value,
                    anonymousUser.value,
                    position.value,
                    imagesBytesList.toList()
                )
                onSuccess()
            } catch (error: Exception) {
                when (error) {
                    is PostgrestRestException -> {
                        onFailure(PublicationErrors.EMPTY_STRINGS)
                        return@launch
                    }

                    else -> onFailure(PublicationErrors.GENERIC)
                }
            }
        }
}