package com.unibo.pazzagliacasadei.uniboard.ui.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import kotlinx.coroutines.launch
import com.unibo.pazzagliacasadei.uniboard.data.repositories.home.HomeRepository
import org.maplibre.android.geometry.LatLng

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    val posts = mutableStateListOf<PostWithPreviewImage>()
    val isLoading = mutableStateOf(true)
    val currentLocation = mutableStateOf<LatLng?>(null)

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            isLoading.value = true
            posts.clear()
            posts.addAll(homeRepository.getAllPosts())
            isLoading.value = false
        }
    }

    fun searchPosts(query: String) {
        viewModelScope.launch {
            isLoading.value = true
            posts.clear()
            posts.addAll(homeRepository.searchPosts(query))
            isLoading.value = false
        }
    }

    fun filterPosts(filterIndex: Int) {
        viewModelScope.launch {
            isLoading.value = true
            posts.clear()
            posts.addAll(
                when (filterIndex) {
                    1 -> homeRepository.getRecentPosts()
                    2 -> {
                        if (currentLocation.value != null){
                            homeRepository.getNearbyPosts(currentLocation.value!!)
                        }
                        else emptyList()
                    }
                    else -> homeRepository.getAllPosts()
                }
            )
            isLoading.value = false
        }
    }
}