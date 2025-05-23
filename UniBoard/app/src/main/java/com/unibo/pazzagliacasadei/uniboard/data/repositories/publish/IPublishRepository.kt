package com.unibo.pazzagliacasadei.uniboard.data.repositories.publish

import org.maplibre.android.geometry.LatLng

interface IPublishRepository {
    suspend fun publishPost(
        postTitle: String,
        postTextContent: String,
        isAnonymous: Boolean,
        position: LatLng?,
        images: List<ByteArray>
    )
}