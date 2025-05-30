package com.unibo.pazzagliacasadei.uniboard.data.models.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PositionLatLon(
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double
)
