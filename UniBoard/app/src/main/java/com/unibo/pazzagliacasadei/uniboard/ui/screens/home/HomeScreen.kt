package com.unibo.pazzagliacasadei.uniboard.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import com.unibo.pazzagliacasadei.uniboard.ui.composables.BottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.navigation.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.FilterTabs
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.PostCard
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.SearchBar
import com.unibo.pazzagliacasadei.uniboard.utils.location.LocationService
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeVM: HomeViewModel,
    selectPost: (post: PostWithPreviewImage) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val locationService = LocationService(LocalContext.current)


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
                onSearch = { homeVM.searchPosts(it) })
            Spacer(modifier = Modifier.height(8.dp))
            FilterTabs(
                titles = listOf(
                    stringResource(R.string.all),
                    stringResource(R.string.recents),
                    stringResource(R.string.nearby)
                ), selectedIndex = selectedTab,

                //TODO Sistemare alcune inconsistenze dovute alla coroutine
                onTabSelected = { index ->
                    selectedTab = index
                    if (index == 2) {
                        scope.launch {
                            try {
                                val loc = locationService.getCurrentLocation()
                                homeVM.currentLocation.value = loc
                            } catch (e: IllegalStateException) {
                                Log.e("TEST", e.toString())
                                //TODO showLocationDisabledAlert = true
                            }
                        }
                    }
                    homeVM.filterPosts(index)
                })
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (homeVM.isLoading.value) {
                    item(span = { GridItemSpan(2) }) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else if (homeVM.posts.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(stringResource(R.string.no_posts_found))
                        }
                    }
                } else {
                    items(homeVM.posts) { post ->
                        PostCard(post = post, onClick = {
                            selectPost(post)
                            navController.navigate(UniBoardRoute.Detail)
                        })
                    }
                }
            }
        }
    }
}
