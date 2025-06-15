package com.unibo.pazzagliacasadei.uniboard.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import com.unibo.pazzagliacasadei.uniboard.ui.composables.BottomBar
import com.unibo.pazzagliacasadei.uniboard.ui.composables.TopBar
import com.unibo.pazzagliacasadei.uniboard.ui.navigation.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthState
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.FilterTabs
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.PostCard
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.SearchBar
import com.unibo.pazzagliacasadei.uniboard.utils.location.LocationService
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeVM: HomeViewModel,
    selectPost: (post: PostWithPreviewImage) -> Unit,
    authState: State<AuthState?>,
    logout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val locationService = LocationService(LocalContext.current)

    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController,
            authState.value == AuthState.AnonymousAuthenticated,
            logout) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { homeVM.searchPosts(it) })
            Spacer(modifier = Modifier.height(8.dp))
            FilterTabs(
                titles = listOf(
                    stringResource(R.string.all),
                    stringResource(R.string.recents),
                    stringResource(R.string.nearby)
                ), selectedIndex = selectedTab,

                onTabSelected = { index ->
                    homeVM.showLocationDisabledDialog.value = false
                    selectedTab = index
                    if (index == 2) {
                        scope.launch {
                            try {
                                val loc = locationService.getCurrentLocation()
                                homeVM.currentLocation.value = loc
                                homeVM.filterPosts(index)
                            } catch (e: SecurityException) {
                                Log.e("TEST", e.toString())
                                homeVM.showLocationDisabledDialog.value = true
                            }
                        }
                    } else {
                        homeVM.filterPosts(index)
                    }
                })
            Spacer(modifier = Modifier.height(16.dp))
            if (homeVM.showLocationDisabledDialog.value) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.location_permission_required),
                        textAlign = TextAlign.Center
                    )
                }
            } else {

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {

                    if (homeVM.isLoading.value) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else if (homeVM.posts.isEmpty()) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(stringResource(R.string.no_posts_found))
                            }
                        }
                    } else {
                        items(homeVM.posts) { post ->
                            PostCard(post = post) {
                                selectPost(post)
                                navController.navigate(UniBoardRoute.Detail)
                            }
                        }
                    }
                }
            }
        }
    }
}
