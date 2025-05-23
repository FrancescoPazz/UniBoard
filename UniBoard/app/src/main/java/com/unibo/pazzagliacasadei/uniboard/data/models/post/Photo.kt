package com.unibo.pazzagliacasadei.uniboard.data.models.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    @SerialName("post_id") val postId: String,
    @SerialName("name") val name: String,
)
