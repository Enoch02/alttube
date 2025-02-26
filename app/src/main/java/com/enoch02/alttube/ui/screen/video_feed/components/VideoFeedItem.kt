package com.enoch02.alttube.ui.screen.video_feed.components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoFeedItem(modifier: Modifier = Modifier, player: ExoPlayer) {
    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = false
                }
            },
            update = { playerView ->
                playerView.player = player
                playerView.useController = false
            }
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.primary),
            content = {
                Row {
                    Icon(imageVector = Icons.Default.PersonPin, contentDescription = null)
                    Text(text = "Username")
                    Button(content = { Text(text = "Follow") }, onClick = { })
                }

                Text("This is a description of something interesting...")
            }
        )

        Row {

        }
    }
}