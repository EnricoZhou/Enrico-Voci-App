package com.example.vociapp.ui.components.maps

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.di.LocalServiceLocator
import com.example.vociapp.ui.components.core.CustomFAB
import com.example.vociapp.ui.components.homeless.AddLocationSearchbar
import com.example.vociapp.ui.components.homeless.LocationHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions

@Composable
fun SearchBox(
    onConfirmLocation: (String) -> Unit,
    homeless: Homeless? = null
){
    //----- Region: Data Initialization -----
    val serviceLocator = LocalServiceLocator.current
    val homelessViewModel = serviceLocator.obtainHomelessViewModel()
    val locationCoordinates by homelessViewModel.locationCoordinates.collectAsState()
    val locationAddress by homelessViewModel.locationAddress.collectAsState()
    // Initialize camera location and points
    var points by remember { mutableStateOf<List<Point>>(emptyList()) }
    var address by remember { mutableStateOf("") }
    var cameraOptions by remember { mutableStateOf<CameraOptions?>(null) }
    // Current Location
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationHandler = remember {
        LocationHandler(context, fusedLocationClient, homelessViewModel)
    }
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    var showLocationSelectionDialog by remember { mutableStateOf(false) }

    if (homeless != null && homeless.location.isNotEmpty())
        homelessViewModel.mapboxForwardGeocoding(homeless.location)

    when (locationCoordinates.data) {
        null -> {}
        else -> {
            val coordinates = locationCoordinates.data!!
            val point = Point.fromLngLat(coordinates.second, coordinates.first)
            cameraOptions = CameraOptions.Builder()
                .center(point)
                .pitch(45.0)
                .zoom(15.5)
                .bearing(-17.6)
                .build()
            points += point
            Log.d("guuu", "points: $points")
        }
    }

    LaunchedEffect(Unit) {
        locationHandler.getCurrentLocation(
            callback = { location ->
                currentLocation = location
            }
        )
        homelessViewModel.mapboxReverseGeocoding(
            currentLocation?.first ?: 0.0,
            currentLocation?.second ?: 0.0
        )
    }
    //----- Region: View Composition -----
    Box(modifier = Modifier.fillMaxSize()) {
        // MapView with points and camera options
        MultiPointMap(
            points = points,
            cameraOptions = cameraOptions ?:
                CameraOptions
                    .Builder()
                    .center(Point.fromLngLat(9.19, 45.4642))
                    .pitch(45.0)
                    .zoom(10.5)
                    .bearing(-17.6)
                    .build()
        )
        // Searchbar with suggestions
        AddLocationSearchbar(
            modifier = Modifier.align(Alignment.TopCenter),
            onClick = {
                address = it
                homelessViewModel.mapboxForwardGeocoding(it)
                Log.d("SearchBox-AddLocationSearchBar", it)
            }
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ){
            // Button to save current location - LEFT
            CustomFAB(
                text = "Current Location",
                icon = Icons.Default.LocationOn,
                onClick = {
                    Log.d("SearchBox", "button: ${locationAddress.data.toString()}")
                    address = locationAddress.data ?: ""
                    homelessViewModel.mapboxForwardGeocoding(address)
                },
            )
            // Button to save the selected location - RIGHT
            CustomFAB(
                text = "Confirm Location",
                icon = Icons.Default.Check,
                onClick = { showLocationSelectionDialog = true },
            )
        }
    }

    if (showLocationSelectionDialog) {
        LocationSelectionDialog(
            location = address,
            onConfirmLocation = { onConfirmLocation(it) },
            onDismiss = { showLocationSelectionDialog = false }
        )
    }
}