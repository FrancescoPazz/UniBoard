package com.unibo.pazzagliacasadei.uniboard.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import kotlinx.coroutines.launch
import com.unibo.pazzagliacasadei.uniboard.data.repositories.home.HomeRepository

class HomeViewModel(
    private val repository: HomeRepository,
) : ViewModel() {
    private val _posts = MutableLiveData<List<PostWithPreviewImage>?>()
    val posts: LiveData<List<PostWithPreviewImage>?> = _posts

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _posts.value = repository.getAllPosts()
        }
    }

    fun searchPosts(query: String) {
        viewModelScope.launch {
            _posts.value = repository.searchPosts(query)
        }
    }

    fun filterPosts(filterIndex: Int) {
        viewModelScope.launch {
            _posts.value = when (filterIndex) {
                1 -> repository.getRecentPosts()
                2 -> repository.getPopularPosts()
                3 -> repository.getNearbyPosts()
                else -> repository.getAllPosts()
            }
        }
    }
}