package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.mapselector.gps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AddressAutocomplete(
    onAddressSelected: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(listOf<String>()) }
    val showSuggestions = remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (query.length >= 3) {
            delay(500) // debounce di 500ms
            suggestions = try {
                fetchAddressSuggestions(query)
            } catch (_: Exception) {
                emptyList()
            }
        } else {
            suggestions = emptyList()
        }
    }

    TextField(
        value = query,
        onValueChange = {
            query = it
            showSuggestions.value
        },
        label = { Text("Cerca indirizzo") },
        modifier = Modifier.fillMaxWidth()
    )

    if (showSuggestions.value) {
        Column {
            suggestions.forEach { suggestion ->
                Text(
                    text = suggestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAddressSelected(suggestion)
                            showSuggestions.value = false
                        }
                        .padding(8.dp)
                )
            }
        }
    }


}