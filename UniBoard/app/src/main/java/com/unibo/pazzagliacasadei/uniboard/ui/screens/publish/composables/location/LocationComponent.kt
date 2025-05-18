package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location.gps.AddressAutocomplete
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location.gps.GPSScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location.gps.geocodeAddress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng

@Composable
fun LocationComponent(
    position: MutableState<LatLng?>,
    context: Context = LocalContext.current
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GPSScreen(position)
        if (position.value != null){
            Text(text = "${position.value!!.latitude} ,  ${position.value!!.longitude}" )
        }
        AddressAutocomplete { address ->
            CoroutineScope(Dispatchers.Main).launch {
                val gPos = geocodeAddress(address)
                if (gPos != null) {
                    position.value = gPos
                }
            }
        }
        MapSelector(
            position = position,
            context = context
        )
    }
}