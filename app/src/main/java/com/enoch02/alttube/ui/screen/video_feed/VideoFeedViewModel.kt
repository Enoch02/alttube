package com.enoch02.alttube.ui.screen.video_feed

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VideoFeedViewModel @Inject constructor(private val player: ExoPlayer) : ViewModel() {
    private val _playerState = MutableStateFlow(player)
    val playerState: StateFlow<ExoPlayer> = _playerState

    private val _currentVideoIndex = MutableStateFlow(0)
    val currentVideoIndex: StateFlow<Int> = _currentVideoIndex

    private val _playbackError = MutableStateFlow<String?>(null)
    val playbackError: StateFlow<String?> = _playbackError

    val videos = listOf(
        "https://videos.pexels.com/video-files/4678261/4678261-hd_1080_1920_25fps.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://videos.pexels.com/video-files/4434242/4434242-uhd_1440_2560_24fps.mp4",
        "https://videos.pexels.com/video-files/2785536/2785536-uhd_1440_2560_25fps.mp4",
        "https://videos.pexels.com/video-files/4678261/4678261-hd_1080_1920_25fps.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://videos.pexels.com/video-files/4678261/4678261-hd_1080_1920_25fps.mp4"
    )

    init {
        addErrorListener()
        // Prepare all videos as a playlist
        preparePlaylist()
    }

    private fun preparePlaylist() {
        // Clear any previous media items
        player.clearMediaItems()

        // Add all videos to the playlist
        videos.forEach { url ->
            val mediaItem = MediaItem.fromUri(url)
            player.addMediaItem(mediaItem)
        }

        // Prepare the player once with all items
        player.prepare()
        player.playWhenReady = true
    }

    private fun addErrorListener() {
        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                handleError(error)
            }
        })
    }

    fun setCurrentVideo(index: Int) {
        if (index in videos.indices && index != _currentVideoIndex.value) {
            // Seek to the selected media item
            player.seekTo(index, 0)
            _currentVideoIndex.value = index
        }
    }

    private fun handleError(error: PlaybackException) {
        val errorMessage = when (error.errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> "Network connection error"
            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> "File not found"
            PlaybackException.ERROR_CODE_DECODER_INIT_FAILED -> "Decoder initialization error"
            else -> "Playback error: ${error.message}"
        }

        _playbackError.value = errorMessage
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
