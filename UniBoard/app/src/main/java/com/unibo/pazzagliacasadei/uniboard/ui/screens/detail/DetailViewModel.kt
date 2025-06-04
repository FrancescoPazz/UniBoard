package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.detail.CommentWithAuthor
import com.unibo.pazzagliacasadei.uniboard.data.models.home.Post
import com.unibo.pazzagliacasadei.uniboard.data.models.post.Photo
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PositionLatLon
import com.unibo.pazzagliacasadei.uniboard.data.repositories.detail.DetailRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val detailRepository: DetailRepository,
) : ViewModel() {
    val post = mutableStateOf<Post?>(null)
    val author = mutableStateOf<User?>(null)
    val comments = mutableStateListOf<CommentWithAuthor>()
    val position = mutableStateOf<PositionLatLon?>(null)
    val photos = mutableStateListOf<Photo>()
    val convertedPhotos = mutableStateListOf<ByteArray>()

    fun setPost(postPassed: Post) {
        viewModelScope.launch {
            try {
                post.value = postPassed
                if (post.value == null) return@launch

                author.value = detailRepository.getAuthor(post.value!!.author)
                comments.addAll(
                    detailRepository.getComments(post.value!!.id)
                )
                position.value = detailRepository.getPostPosition(post.value!!.id)

                photos.clear()
                convertedPhotos.clear()
                photos.addAll(detailRepository.getPhotos(post.value!!.id))
                convertedPhotos.addAll(detailRepository.convertPhotos(photos))
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
