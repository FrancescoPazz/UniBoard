package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location.gps

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.unibo.pazzagliacasadei.uniboard.R
import kotlinx.coroutines.delay

@Composable
fun AddressAutocomplete(
    suggestions: SnapshotStateList<String>,
    modifier: Modifier
) {
    var query by remember { mutableStateOf("") }

    LaunchedEffect(query) {
        if (query.length >= 3) {
            delay(500) // debounce di 500ms
            try {
                suggestions.clear()
                suggestions.addAll(fetchAddressSuggestions(query))
            } catch (_: Exception) {
                suggestions.clear()
            }
        } else {
            suggestions.clear()
        }
    }

    TextField(
        value = query,
        modifier = modifier,
        onValueChange = {
            query = it
        },
        label = { Text(stringResource(R.string.address_search)) }
    )
}