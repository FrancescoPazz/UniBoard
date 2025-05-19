package com.unibo.pazzagliacasadei.uniboard.data.models.profile

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("id") val id: String,

    @SerialName("sent_time") val sentTime: Instant,

    @SerialName("content") val content: String,

    @SerialName("sender_id") val senderId: String,

    @SerialName("receiver_id") val receiverId: String
)