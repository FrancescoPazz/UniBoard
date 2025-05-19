package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location.gps.AddressAutocomplete
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location.gps.GPSScreen
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location.gps.geocodeAddress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment

@Composable
fun LocationComponent(
    position: MutableState<LatLng?>,
) {
    val context = LocalContext.current
    Column (verticalArrangement = Arrangement.spacedBy(16.dp)){
        val suggestions = remember { mutableStateListOf<String>() }
        fun onSuggestionClicked(suggestion: String) {
            CoroutineScope(Dispatchers.Main).launch {
                val gPos = geocodeAddress(suggestion)
                if (gPos != null) {
                    position.value = gPos
                }
            }
            suggestions.clear()
        }

        Card(elevation = CardDefaults.cardElevation(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                AddressAutocomplete(suggestions, modifier = Modifier.weight(4f))
                GPSScreen(position, modifier = Modifier.weight(1f))
            }

            LazyColumn {
                items(suggestions) { suggestion ->
                    Text(
                        text = suggestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSuggestionClicked(suggestion)
                            }.padding(8.dp)
                    )
                }
            }
        }

        if (position.value != null) {
            Text(text = "${position.value!!.latitude} ,  ${position.value!!.longitude}")
        }

        MapSelector(
            position = position,
            context = context
        )
    }
}
