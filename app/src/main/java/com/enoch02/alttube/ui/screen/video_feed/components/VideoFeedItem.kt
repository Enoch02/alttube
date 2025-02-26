package com.enoch02.alttube.ui.screen.video_feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.enoch02.alttube.ui.components.VideoPlayerView

@Composable
fun VideoFeedItem(modifier: Modifier = Modifier, videoUrl: String) {
    Box(modifier = modifier) {
        VideoPlayerView(
            videoUrl = videoUrl,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
        )

        //TODO: remove
        Text(videoUrl)
    }
}

@Preview
@Composable
private fun Preview() {
    VideoFeedItem(videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
}