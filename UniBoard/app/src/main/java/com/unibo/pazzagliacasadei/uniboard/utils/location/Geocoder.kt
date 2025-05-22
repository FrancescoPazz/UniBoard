package com.unibo.pazzagliacasadei.uniboard.utils.location

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.maplibre.android.geometry.LatLng
import java.net.URLEncoder

suspend fun fetchAddressSuggestions(query: String): List<String> {
    if (query.length < 3) return emptyList()

    val url = "https://nominatim.openstreetmap.org/search?format=json&q=${
        URLEncoder.encode(
            query,
            "UTF-8"
        )
    }"
    val request = Request.Builder()
        .url(url)
        .header("User-Agent", "UniBoard")
        .build()

    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        if (body != null) {
            val jsonArray = JSONArray(body)
            (0 until jsonArray.length()).mapNotNull { i ->
                jsonArray.getJSONObject(i).optString("display_name")
            }
        } else {
            emptyList()
        }
    }
}

suspend fun geocodeAddress(address: String): LatLng? {
    val url = "https://nominatim.openstreetmap.org/search?format=json&q=${
        URLEncoder.encode(
            address,
            "UTF-8"
        )
    }"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .header("User-Agent", "UniBoard")
        .build()

    return withContext(Dispatchers.IO) {
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        if (body != null) {
            val jsonArray = JSONArray(body)
            if (jsonArray.length() > 0) {
                val obj = jsonArray.getJSONObject(0)
                val lat = obj.getDouble("lat")
                val lon = obj.getDouble("lon")
                LatLng(lat, lon)
            } else {
                null
            }
        } else {
            null
        }
    }
}