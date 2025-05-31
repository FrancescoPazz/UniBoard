package com.unibo.pazzagliacasadei.uniboard.utils.images

import android.content.Context
import android.net.Uri
import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.NotFoundRestException
import io.github.jan.supabase.storage.storage

fun uriToBytes(uri: Uri, context: Context): ByteArray {
    return context.contentResolver.openInputStream(uri)?.readBytes() ?: ByteArray(0)
}

suspend fun getPreviewImage(supabase: SupabaseClient, postId: String): ByteArray? {
    val bucket = supabase.storage.from("post-images")
    try {
        val bytes = bucket.downloadPublic("${postId}/0.jpg") {
            transform {
                fill()
                quality = 100
            }
        }
        return bytes
    } catch (exception: Exception) {
        if (exception is NotFoundRestException) {
            return null
        }
        Log.e("TEST", "Conversione dell'immagine non riuscita")
        return null
    }
}