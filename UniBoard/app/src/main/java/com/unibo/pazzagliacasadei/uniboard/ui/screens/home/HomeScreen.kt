package com.unibo.pazzagliacasadei.uniboard.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.ui.navigation.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.composables.BottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.FilterTabs
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.PostCard
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.SearchBar

@Composable
fun HomeScreen(
    navController: NavHostController, params: HomeParams
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }

    Scaffold(topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchBar(query = query,
                onQueryChange = { query = it },
                onSearch = { params.searchPosts(it) })
            Spacer(modifier = Modifier.height(8.dp))
            FilterTabs(
                titles = listOf("Tutti", "Recenti", "Popolari", "Vicino a te"),
                selectedIndex = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    params.filterPosts(index)
                })
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(params.posts) { post ->
                    PostCard(post = post, onClick = {
                        params.selectPost(post)
                        navController.navigate(UniBoardRoute.Detail)
                    })
                }
            }
        }
    }
}
