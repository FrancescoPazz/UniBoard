package com.unibo.pazzagliacasadei.uniboard.data.models.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName("id") val id: String,

    @SerialName("author") val author: String,

    @SerialName("publish_date") val publishDate: String,

    @SerialName("content") val content: String
)