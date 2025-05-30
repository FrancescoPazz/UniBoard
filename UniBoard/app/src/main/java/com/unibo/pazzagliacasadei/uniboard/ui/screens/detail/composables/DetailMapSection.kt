package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.post.Position
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.plugins.annotation.SymbolManager
import org.maplibre.android.utils.BitmapUtils

fun Position.toLatLng(): LatLng? {
    return try {
        val parts = latLng.split("|")
        if (parts.size == 2) {
            val lat = parts[0].toDouble()
            val lng = parts[1].toDouble()
            LatLng(lat, lng)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

@Composable
fun DetailMapSection(position: Position?) {
    if (position == null) return

    val context = LocalContext.current
    val latLng = position.toLatLng()

    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(stringResource(R.string.position), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
        var symbolManager by remember { mutableStateOf<SymbolManager?>(null) }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            factory = {
                MapView(context).apply {
                    getMapAsync { map ->
                        mapLibreMap = map
                        map.setStyle(Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty"))
                        { style ->
                            symbolManager = SymbolManager(this, map, style).apply {
                                iconAllowOverlap = true
                                textAllowOverlap = true
                            }

                            style.addImage(
                                "marker-icon",
                                BitmapUtils.getBitmapFromDrawable(
                                    ContextCompat.getDrawable(context, R.drawable.marker)
                                )!!
                            )

                            map.uiSettings.apply {
                                isScrollGesturesEnabled = false
                                isZoomGesturesEnabled = true
                                isRotateGesturesEnabled = false
                                isTiltGesturesEnabled = false
                                isDoubleTapGesturesEnabled = false
                                isCompassEnabled = false
                            }
                        }
                    }
                }
            },
            update = { mapView ->
                if (mapLibreMap != null && symbolManager != null && latLng != null) {
                    symbolManager?.deleteAll()
                    symbolManager?.create(
                        org.maplibre.android.plugins.annotation.SymbolOptions()
                            .withLatLng(latLng)
                            .withIconImage("marker-icon")
                            .withIconSize(0.2f)
                    )
                    mapLibreMap?.moveCamera(
                        org.maplibre.android.camera.CameraUpdateFactory.newLatLngZoom(latLng, 10.0)
                    )
                }
            }
        )

        if (position.street != null || position.city != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                buildString {
                    position.street?.let { append(it) }
                    position.civic?.let { civic -> append(", $civic") }
                    position.city?.let { city ->
                        if (isNotEmpty()) append(", ")
                        append(city)
                    }
                    position.postal?.let { postal -> append(" - $postal") }
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}