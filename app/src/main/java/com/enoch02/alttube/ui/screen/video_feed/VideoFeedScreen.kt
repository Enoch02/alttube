package com.enoch02.alttube.ui.screen.video_feed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.enoch02.alttube.ui.screen.video_feed.components.VideoFeedItem

@Composable
fun VideoFeedScreen(modifier: Modifier, navController: NavHostController) {
    val feedPagerState = rememberPagerState { 10 }

    VerticalPager(
        state = feedPagerState,
        pageContent = {
            VideoFeedItem(videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        },
        modifier = Modifier
            .fillMaxSize()
    )
}