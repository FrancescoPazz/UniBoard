package com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FilterTabs(
    titles: List<String>, selectedIndex: Int, onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedIndex
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) })
        }
    }
}