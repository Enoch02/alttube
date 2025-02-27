package com.enoch02.alttube.ui.screen.video_feed

import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoFeedViewModel @Inject constructor(private val player: ExoPlayer) : ViewModel() {
    val videos = listOf(
        "https://videos.pexels.com/video-files/4678261/4678261-hd_1080_1920_25fps.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://videos.pexels.com/video-files/4434242/4434242-uhd_1440_2560_24fps.mp4",
        "https://videos.pexels.com/video-files/2785536/2785536-uhd_1440_2560_25fps.mp4",
        "https://videos.pexels.com/video-files/4678261/4678261-hd_1080_1920_25fps.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://videos.pexels.com/video-files/4678261/4678261-hd_1080_1920_25fps.mp4"
    )
}
