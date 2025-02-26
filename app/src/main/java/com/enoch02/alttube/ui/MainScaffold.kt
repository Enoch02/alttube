package com.enoch02.alttube.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.enoch02.alttube.R
import com.enoch02.alttube.navigation.MainDestination
import com.enoch02.alttube.navigation.MainDestinationInfo
import com.enoch02.alttube.ui.screen.video_feed.VideoFeedScreen

@Composable
fun MainScaffold(navController: NavHostController) {
    val destinations =
        mapOf(
            MainDestination.FEED to MainDestinationInfo("Feed", R.drawable.feed),
            MainDestination.SEARCH to MainDestinationInfo("Search", R.drawable.search),
            MainDestination.UPLOAD to MainDestinationInfo("Upload", R.drawable.upload),
            MainDestination.FAVORITE to MainDestinationInfo("Favorite", R.drawable.favorite),
            MainDestination.PROFILE to MainDestinationInfo("Profile", R.drawable.profile)
        )
    var selectedDestination by rememberSaveable { mutableStateOf(MainDestination.FEED) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            when (selectedDestination) {
                MainDestination.FEED -> {
                    VideoFeedScreen(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                MainDestination.SEARCH -> {

                }

                MainDestination.UPLOAD -> {

                }

                MainDestination.FAVORITE -> {

                }

                MainDestination.PROFILE -> {

                }
            }
        },
        bottomBar = {
            NavigationBar {
                destinations.forEach { (destination, destinationInfo) ->
                    NavigationBarItem(
                        label = { Text(destinationInfo.name) },
                        icon = {
                            Icon(
                                painter = painterResource(destinationInfo.icon),
                                contentDescription = "Navigate to $destinationInfo screen"
                            )
                        },
                        onClick = {
                            selectedDestination = destination
                        },
                        selected = destination == selectedDestination
                    )
                }
            }
        }
    )
}