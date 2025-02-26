package com.enoch02.alttube.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enoch02.alttube.R
import com.enoch02.alttube.ui.MainScaffold

@Composable
fun AltTubeNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Destination.MainScaffold.route,
        builder = {
            composable(Destination.MainScaffold.route) {
                MainScaffold(navController = navController)
            }
        }
    )
}