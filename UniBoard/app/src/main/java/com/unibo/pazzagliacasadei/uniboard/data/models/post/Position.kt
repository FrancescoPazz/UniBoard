package com.unibo.pazzagliacasadei.uniboard.data.models.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Position(
    @SerialName("post_id") val postId: String,

    @SerialName("street") val street: String?,

    @SerialName("civic") val civic: String?,

    @SerialName("city") val city: String?,

    @SerialName("postal_code") val postal: String?,

    @SerialName("latlong") val latLng: String
)
