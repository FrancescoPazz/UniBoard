package com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    query: String, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit
) {
    OutlinedTextField(value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Cerca annunci...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}