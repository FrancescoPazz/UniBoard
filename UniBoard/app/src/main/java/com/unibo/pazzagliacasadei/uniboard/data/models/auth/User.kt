package com.unibo.pazzagliacasadei.uniboard.data.models.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("name") val name: String?,
    @SerialName("surname") val surname: String?,
    @SerialName("username") val username: String,
    @SerialName("tel") val tel: String?
)
