package com.unibo.pazzagliacasadei.uniboard.data.models.profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    @SerialName("user_id") val userId: String,

    @SerialName("contact_id") val contactId: String,

    @SerialName("contact_username") val contactUsername: String,

    @SerialName("last_message") val lastMessage: String?,

    @SerialName("last_time") val lastTime: String?
)