package com.unibo.pazzagliacasadei.uniboard.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.ui.navigation.UniBoardRoute

@SuppressLint("DiscouragedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringAfterLast(".")?.lowercase()

    CenterAlignedTopAppBar(
        title = {
        if (currentRoute != null) {
            val resId = remember(currentRoute) {
                context.resources.getIdentifier(
                    currentRoute,
                    "string",
                    context.packageName
                )
            }
            val titleText = if (resId != 0) {
                stringResource(resId)
            } else {
                currentRoute.replaceFirstChar { it.uppercase() }
            }
            Text(
                titleText
            )
        }
    }, navigationIcon = {
        if (navController.previousBackStackEntry != null) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    }, actions = {
        IconButton(onClick = { navController.navigate(UniBoardRoute.Settings) }) {
            Icon(
                Icons.Filled.Settings, contentDescription = stringResource(R.string.settings)
            )
        }
    })
}

data class BottomNavItem(
    val name: String, val route: UniBoardRoute, val icon: ImageVector
)

@Composable
fun BottomBar(navController: NavController) {
    val bottomNavItems = listOf(
        BottomNavItem(
            name = stringResource(R.string.home),
            route = UniBoardRoute.Home,
            icon = Icons.Filled.Home
        ), BottomNavItem(
            name = stringResource(R.string.profile),
            route = UniBoardRoute.Profile,
            icon = Icons.Default.Person
        ), BottomNavItem(
            name = stringResource(R.string.publish),
            route = UniBoardRoute.Publish,
            icon = Icons.Default.Add
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringAfterLast(".")

    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                NavigationBarItem(icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name
                    )
                }, label = {
                    Text(
                        text = item.name,
                    )
                }, selected = currentRoute == item.route.toString(), onClick = {
                    if (currentRoute != item.route.toString()) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                })
            }
        }
    }
}