package com.enoch02.alttube.ui.screen.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.enoch02.alttube.ui.MainViewModel
import com.enoch02.alttube.ui.component.VideoThumbnail

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier, mainVewModel: MainViewModel = hiltViewModel()) {
    val userInfo = mainVewModel.userInfo

    LaunchedEffect(Unit) {
        mainVewModel.getUserInfo()
    }

    if (userInfo?.favorites.isNullOrEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = {
                Text(text = "No favorites added")
            }
        )
    } else {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                items(userInfo?.favorites!!) { videoUrl ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    ) {
                        VideoThumbnail(videoUrl = videoUrl)
                    }
                }
            }
        )
    }
}