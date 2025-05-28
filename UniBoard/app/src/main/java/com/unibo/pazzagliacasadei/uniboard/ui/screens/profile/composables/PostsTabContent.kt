package com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.data.models.home.PostWithPreviewImage
import com.unibo.pazzagliacasadei.uniboard.ui.navigation.UniBoardRoute
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.composables.PostCard
import com.unibo.pazzagliacasadei.uniboard.ui.screens.loading.LoadingScreen

data class Announcement(
    val title: String, val price: String, val location: String, val imageRes: Int
)

@Composable
fun PostsTabContent(
    navController: NavHostController,
    posts: State<List<PostWithPreviewImage>?>,
    loadUserPosts: () -> Unit,
    selectUserPost: (post: PostWithPreviewImage) -> Unit
) {

    LaunchedEffect(loadUserPosts) {
        loadUserPosts()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        if (posts.value.isNullOrEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.no_posts_published_yet),
                    modifier = Modifier.fillMaxHeight(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            items(posts.value!!) { post ->
                PostCard(
                    post = post,
                    onClick = {
                        selectUserPost(post)
                        navController.navigate(UniBoardRoute.Detail)
                    }
                )
            }
        }
    }
}
