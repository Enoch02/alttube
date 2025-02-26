package com.enoch02.alttube.ui.screen.video_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.enoch02.alttube.ui.screen.video_feed.components.VideoFeedItem

@Composable
fun VideoFeedScreen(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: VideoFeedViewModel = hiltViewModel(),
) {
    val player by viewModel.playerState.collectAsState()
    val feedPagerState = rememberPagerState(initialPage = 0) { viewModel.videos.size }

    LaunchedEffect(feedPagerState.currentPage) {
        viewModel.setCurrentVideo(feedPagerState.currentPage)
    }

    VerticalPager(
        state = feedPagerState,
        pageContent = { pageIndex ->
            Box(modifier = Modifier.fillMaxSize()) {
                if (pageIndex == feedPagerState.currentPage) {
                    VideoFeedItem(player = player)
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    )
}