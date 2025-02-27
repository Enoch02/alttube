package com.enoch02.alttube.ui.screen.video_feed

import com.enoch02.alttube.ui.screen.video_feed.components.VideoFeedItem
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun VideoFeedScreen(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: VideoFeedViewModel = hiltViewModel(),
) {
    val feedPagerState = rememberPagerState(initialPage = 0) { viewModel.videos.size }

    VerticalPager(
        state = feedPagerState,
        pageContent = { pageIndex ->
            VideoFeedItem(
                videoURL = viewModel.videos[pageIndex],
                modifier = Modifier.fillMaxSize()
            )
        },
        modifier = modifier.fillMaxSize()
    )
}