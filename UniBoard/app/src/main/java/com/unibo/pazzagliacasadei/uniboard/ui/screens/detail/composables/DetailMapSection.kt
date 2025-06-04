package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PositionLatLon
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.plugins.annotation.SymbolManager
import org.maplibre.android.plugins.annotation.SymbolOptions
import org.maplibre.android.utils.BitmapUtils
import java.util.Locale

@Composable
fun DetailMapSection(position: PositionLatLon?) {
    if (position == null) return
    val context = LocalContext.current
    val initialLatLng = LatLng(position.lat, position.lon)

    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(stringResource(R.string.position), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
            var symbolManager by remember { mutableStateOf<SymbolManager?>(null) }

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                factory = { ctx ->
                    MapView(ctx).apply {
                        getMapAsync { map ->
                            mapLibreMap = map
                            map.setStyle(Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")) { style ->
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

                                map.addOnMapClickListener {
                                    val latStr = String.format(Locale.US, "%.8f", position.lat)
                                    val lonStr = String.format(Locale.US, "%.8f", position.lon)

                                    val uri = Uri.parse("geo:$latStr,$lonStr?q=$latStr,$lonStr")
                                    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                        setPackage("com.google.android.apps.maps")
                                    }
                                    if (intent.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(intent)
                                    } else {
                                        val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latStr,$lonStr")
                                        context.startActivity(
                                            Intent(Intent.ACTION_VIEW, webUri)
                                        )
                                    }
                                    true
                                }
                            }
                        }
                    }
                },
                update = { _ ->
                    if (mapLibreMap != null && symbolManager != null) {
                        symbolManager?.deleteAll()
                        symbolManager?.create(
                            SymbolOptions()
                                .withLatLng(initialLatLng)
                                .withIconImage("marker-icon")
                                .withIconSize(0.2f)
                        )
                        mapLibreMap?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(initialLatLng, 10.0)
                        )
                    }
                }
            )
        }
    }
}
