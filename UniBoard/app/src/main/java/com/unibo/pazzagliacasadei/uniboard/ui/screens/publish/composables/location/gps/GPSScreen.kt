package com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.composables.location.gps

import android.Manifest
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.unibo.pazzagliacasadei.uniboard.R
import com.unibo.pazzagliacasadei.uniboard.utils.location.LocationService
import com.unibo.pazzagliacasadei.uniboard.utils.PermissionStatus
import com.unibo.pazzagliacasadei.uniboard.utils.rememberMultiplePermissions
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng

@Composable
fun GPSScreen(
    position: MutableState<LatLng?>,
    locationService: LocationService,
    modifier: Modifier
) {

    var showLocationDisabledAlert by remember { mutableStateOf(false) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()
    fun getCurrentLocation() = scope.launch {
        try {
            val loc = locationService.getCurrentLocation()
            position.value = loc
        } catch (_: IllegalStateException) {
            showLocationDisabledAlert = true
        }
    }

    val locationPermissions = rememberMultiplePermissions(
        listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    ) { statuses ->
        when {
            statuses.any { it.value == PermissionStatus.Granted } ->
                getCurrentLocation()

            else ->
                showPermissionDeniedAlert = true
        }
    }

    fun getLocationOrRequestPermission() {
        if (locationPermissions.statuses.any { it.value.isGranted }) {
            getCurrentLocation()
        } else {
            locationPermissions.launchPermissionRequest()
        }
    }

    IconButton(
        onClick = ::getLocationOrRequestPermission,
        modifier = modifier
    ) {
        Icon(painter = painterResource(R.drawable.marker), contentDescription = null)
    }

    if (showLocationDisabledAlert) {
        AlertDialog(
            title = { Text(stringResource(R.string.location_disabled)) },
            text = { Text(stringResource(R.string.location_must_be_enabled)) },
            confirmButton = {
                TextButton(onClick = {
                    locationService.openLocationSettings()
                    showLocationDisabledAlert = false
                }) {
                    Text("Enable")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLocationDisabledAlert = false }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { showLocationDisabledAlert = false }
        )
    }

    if (showPermissionDeniedAlert) {
        AlertDialog(
            title = { Text(stringResource(R.string.no_location_permission)) },
            text = { Text(stringResource(R.string.location_permission_required)) },
            confirmButton = {
                TextButton(onClick = {
                    locationPermissions.launchPermissionRequest()
                    showPermissionDeniedAlert = false
                }) {
                    Text(stringResource(R.string.grant))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDeniedAlert = false }) {
                    Text(stringResource(R.string.dismiss))
                }
            },
            onDismissRequest = { showPermissionDeniedAlert = false }
        )
    }
}
