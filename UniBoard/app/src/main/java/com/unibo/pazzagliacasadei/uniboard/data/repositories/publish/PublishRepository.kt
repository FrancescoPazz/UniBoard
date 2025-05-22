package com.unibo.pazzagliacasadei.uniboard.data.repositories.publish

import android.net.Uri
import io.github.jan.supabase.SupabaseClient

class PublishRepository(supabase: SupabaseClient) : IPublishRepository {
    override suspend fun publishPost(images: List<Uri>) {
        TODO("Not yet implemented")
    }
}