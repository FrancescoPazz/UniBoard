package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Announcement(
    val title: String, val price: String, val location: String, val imageRes: Int
)

data class Message(val sender: String, val preview: String)

@Composable
fun AnnouncementsTabContent(announcements: List<Announcement>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(announcements.size) { idx ->
            AnnouncementCard(announcement = announcements[idx])
        }
    }
}
