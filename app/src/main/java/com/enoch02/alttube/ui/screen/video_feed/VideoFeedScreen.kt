package com.enoch02.alttube.ui.screen.video_feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.enoch02.alttube.ui.screen.video_feed.components.VideoFeedItem

@Composable
fun VideoFeedScreen(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: VideoFeedViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.loadPublicVideoURLs()
    }

    when (val state = viewModel.feedContentState) {
        is FeedContentState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    CircularProgressIndicator()
                }
            )
        }

        is FeedContentState.Loaded -> {
            if (state.videos.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        Text(text = "No Video on the server, Try uploading yours 🥲")
                    }
                )
            }
            val feedPagerState =
                rememberPagerState(initialPage = 0) { state.videos.size }

            VerticalPager(
                state = feedPagerState,
                pageContent = { pageIndex ->
                    var pageVisibility by remember { mutableStateOf(false) }

                    LaunchedEffect(pageIndex, feedPagerState.currentPage) {
                        pageVisibility = (pageIndex == feedPagerState.currentPage)
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        if (pageVisibility) {
                            VideoFeedItem(
                                videoURL = state.videos[pageIndex],
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                },
                modifier = modifier.fillMaxSize()
            )
        }

        is FeedContentState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text("An Error has Occurred")

                    Text(text = state.message)

                    Button(
                        onClick = { viewModel.loadPublicVideoURLs() },
                        content = { Text(text = "Retry") }
                    )
                }
            )
        }
    }
}