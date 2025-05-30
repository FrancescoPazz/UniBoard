package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.Comment
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PositionLatLon
import com.unibo.pazzagliacasadei.uniboard.data.repositories.detail.DetailRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val detailRepository: DetailRepository,
) : ViewModel() {
    val post: LiveData<Post?> = detailRepository.currentDetailPost
    val author: LiveData<User?> = detailRepository.currentAuthorPost
    val comments: LiveData<List<Comment>?> = detailRepository.comments
    val position: LiveData<PositionLatLon?> = detailRepository.currentPostPosition
    val photos: LiveData<List<ByteArray>?> = detailRepository.convertedPhotos

    fun setPost(post: Post) {
        viewModelScope.launch {
            try {
                detailRepository.setPost(post)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun addComment(text: String) {
        viewModelScope.launch {
            try {
                if (post.value == null) return@launch
                detailRepository.addComment(post.value!!.id, text)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
