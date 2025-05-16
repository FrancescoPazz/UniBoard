package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.mapselector

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.unibo.pazzagliacasadei.uniboard.R
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.plugins.annotation.SymbolManager
import org.maplibre.android.plugins.annotation.SymbolOptions
import org.maplibre.android.utils.BitmapUtils

@Composable
fun LocationSelector(
    position: MutableState<LatLng?>,
    context: Context = LocalContext.current,
) {
    var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
    var symbolManager by remember { mutableStateOf<SymbolManager?>(null) }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        factory = {
            MapView(context).apply {
                getMapAsync { map ->
                    mapLibreMap = map
                    map.setStyle(
                        Style
                            .Builder()
                            .fromUri(
                                "https://tiles.openfreemap.org/styles/liberty"
                            )
                    )
                    { style ->
                        symbolManager = SymbolManager(this, map, style).apply {
                            iconAllowOverlap = true
                            textAllowOverlap = true
                        }

                        position.value?.let { latLng ->
                            addMarker(symbolManager!!, latLng)
                            moveCamera(map, latLng)
                        }

                        map.addOnMapClickListener { point ->
                            position.value = point
                            symbolManager?.deleteAll()
                            addMarker(symbolManager!!, point)
                            moveCamera(map, point)
                            true
                        }

                        map.uiSettings.isCompassEnabled = true
                        map.uiSettings.isZoomGesturesEnabled = true

                        style.addImage(
                            "marker-icon",
                            BitmapUtils.getBitmapFromDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.marker
                                )
                            )!!
                        )
                    }
                }
            }
        },
        update = {
            val latLng = position.value
            if (latLng != null && mapLibreMap != null && symbolManager != null) {
                symbolManager?.deleteAll()
                addMarker(symbolManager!!, latLng)
                moveCamera(mapLibreMap!!, latLng)
            }
        }
    )
}

private fun addMarker(symbolManager: SymbolManager, latLng: LatLng) {
    symbolManager.create(
        SymbolOptions()
            .withLatLng(latLng)
            .withIconImage("marker-icon")
            .withIconSize(0.2f)
    )
}

private fun moveCamera(map: MapLibreMap, latLng: LatLng) {
    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0))
}