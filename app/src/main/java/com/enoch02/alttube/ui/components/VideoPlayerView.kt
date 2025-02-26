package com.enoch02.alttube.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayerView(
    modifier: Modifier = Modifier,
    viewModel: VideoPlayerViewModel = viewModel(),
    videoUrl: String,
) {
    val context = LocalContext.current
    val player by viewModel.playerState.collectAsState()

    LaunchedEffect(videoUrl) {
        viewModel.initializePlayer(context, videoUrl)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.savePlayerState()
            viewModel.releasePlayer()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                this.player = player
                this.useController = false
            }
        },
        update = { playerView ->
            playerView.player = player
            playerView.useController = false
        }
    )
}