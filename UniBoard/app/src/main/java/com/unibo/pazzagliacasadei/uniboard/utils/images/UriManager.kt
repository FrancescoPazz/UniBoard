package com.unibo.pazzagliacasadei.uniboard.utils.images

import android.content.Context
import android.net.Uri

fun uriToBytes(uri: Uri, context: Context): ByteArray {
    return context.contentResolver.openInputStream(uri)?.readBytes() ?: ByteArray(0)
}