package com.unibo.pazzagliacasadei.uniboard.data.models.post

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostToPublish(
    @SerialName("author") val author: String,
    @SerialName("publish_date") val date: Instant,
    @SerialName("content") val content: String,
    @SerialName("title") val title: String,
    @SerialName("anonymous") val anonymous: Boolean
)

@Serializable
data class PostRetrieved(
    @SerialName("id") val id: String
)