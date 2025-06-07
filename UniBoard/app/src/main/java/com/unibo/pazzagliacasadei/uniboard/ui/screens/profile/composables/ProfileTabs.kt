package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ProfileTabs(
    tabs: List<String>, selectedIndex: Int, onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedIndex
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(selected = index == selectedIndex,
                onClick = { onTabSelected(index) },
                text = { Text(title, fontSize = 14.sp) })
        }
    }
}