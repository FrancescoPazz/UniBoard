package com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.composables

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.R

@Composable
fun DetailMapSection(location: Location?) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(stringResource(R.string.position), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))/* TODO AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    controller.setZoom(15.0)
                    controller.setCenter(GeoPoint(location.lat, location.lon))
                    val marker = Marker(this).apply {
                        position = GeoPoint(location.lat, location.lon)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    overlays.add(marker)
                }
            }
        )*/
    }
}
