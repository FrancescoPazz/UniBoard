package com.unibo.pazzagliacasadei.uniboard.utils.images

import android.content.Context
import android.net.Uri
import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage

fun uriToBytes(uri: Uri, context: Context): ByteArray {
    return context.contentResolver.openInputStream(uri)?.readBytes() ?: ByteArray(0)
}

suspend fun getPreviewImage(supabase: SupabaseClient, postId: String): ByteArray {
    try {
        val bucket = supabase.storage.from("post-images")
        val bytes = bucket.downloadPublic("${postId}/0.jpg") {
            transform {
                fill()
                quality = 100
            }
        }
        return bytes
    } catch (e: Exception) {
        Log.e("DetailRepository", "convertPhotos failed", e)
        throw e
    }
}