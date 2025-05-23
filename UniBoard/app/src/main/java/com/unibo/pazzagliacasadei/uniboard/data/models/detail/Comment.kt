package com.unibo.pazzagliacasadei.uniboard.data.models.detail

import java.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    @SerialName("post_id") val postId: String,

    @SerialName("author_id") val authorId: String,

    @SerialName("sent_time") val sentTime: String = Instant.now().toString(),

    @SerialName("content") val content: String,
)
