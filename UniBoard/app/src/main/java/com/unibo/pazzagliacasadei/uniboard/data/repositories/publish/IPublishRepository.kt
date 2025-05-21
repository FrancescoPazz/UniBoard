package com.unibo.pazzagliacasadei.uniboard.data.repositories.publish

import android.net.Uri

interface IPublishRepository {
    suspend fun publishPost(
        images: List<Uri>
    )
}