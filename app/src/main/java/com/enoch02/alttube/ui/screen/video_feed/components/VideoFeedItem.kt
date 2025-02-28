package com.enoch02.alttube.ui.screen.video_feed.components

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.enoch02.alttube.R
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun VideoFeedItem(
    modifier: Modifier = Modifier,
    videoURL: String,
    isFavorite: Boolean,
    onFavoriteAction: () -> Unit,
    onLikeAction: () -> Unit
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setRenderersFactory(
                DefaultRenderersFactory(context)
                    .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
                    .setEnableDecoderFallback(true)
            )
            .build()
            .apply {
                repeatMode = Player.REPEAT_MODE_ONE
                playWhenReady = true
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT

                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        errorMessage = when (error.errorCode) {
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> "Network connection error"
                            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> "File not found"
                            PlaybackException.ERROR_CODE_DECODER_INIT_FAILED -> "Decoder initialization error"
                            else -> "Playback error: ${error.message}"
                        }
                    }
                }
                )

                val mediaItem = MediaItem.fromUri(Uri.parse(videoURL))
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)

                setMediaSource(mediaSource)
                prepare()
            }
    }
    val reloadVideo = {
        try {
            errorMessage = null
            val mediaItem = MediaItem.fromUri(Uri.parse(videoURL))
            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
        } catch (e: Exception) {
            errorMessage = e.message
        }
    }

    var showSeekBackIndicator by remember { mutableStateOf(false) }
    var showPauseIndicator by remember { mutableStateOf(false) }
    var showSeekForwardIndicator by remember { mutableStateOf(false) }

    // prevent animations from getting stuck when tapping repeatedly
    var clickCount by remember { mutableIntStateOf(0) }
    var isMuted by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        AndroidView(
            modifier = modifier
                .fillMaxSize()
            /*.aspectRatio(16 / 9f)*/,
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    useController = false
                    setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Left tap area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { },
                            onDoubleTap = {
                                clickCount++
                                showSeekBackIndicator = true
                                exoPlayer.seekTo(exoPlayer.currentPosition - 5000)
                            }
                        )
                    },
                contentAlignment = Alignment.Center,
                content = {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showSeekBackIndicator,
                        enter = fadeIn(tween(150)) + scaleIn(tween(300)),
                        exit = fadeOut(tween(150)) + scaleOut(tween(300))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.seek_back),
                            contentDescription = null
                        )

                        LaunchedEffect(clickCount) {
                            delay(500)
                            showSeekBackIndicator = false
                        }
                    }
                }
            )

            // Center tap area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                clickCount++
                                showPauseIndicator = true
                                exoPlayer.playWhenReady = !exoPlayer.playWhenReady
                            },
                            onDoubleTap = {
                                //TODO: add like functionality here
                            }
                        )
                    },
                contentAlignment = Alignment.Center,
                content = {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showPauseIndicator,
                        enter = fadeIn(tween(150)) + scaleIn(tween(300)),
                        exit = fadeOut(tween(150)) + scaleOut(tween(300))
                    ) {
                        val icon =
                            if (exoPlayer.isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause

                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )

                        LaunchedEffect(clickCount) {
                            delay(500)
                            showPauseIndicator = false
                        }
                    }
                }
            )

            // Right tap area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { },
                            onDoubleTap = {
                                clickCount++
                                showSeekForwardIndicator = true
                                exoPlayer.seekTo(exoPlayer.currentPosition + 5000)
                            }
                        )
                    },
                contentAlignment = Alignment.Center,
                content = {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showSeekForwardIndicator,
                        enter = fadeIn(tween(150)) + scaleIn(tween(300)),
                        exit = fadeOut(tween(150)) + scaleOut(tween(300))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.seek_forward),
                            contentDescription = null
                        )

                        LaunchedEffect(clickCount) {
                            delay(500)
                            showSeekForwardIndicator = false
                        }
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(0.85f),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonPin,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(text = "Anonymous", color = Color.White)

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            content = {
                                Text(
                                    text = "Follow",
                                    fontSize = 12.sp,
                                )
                            },
                            onClick = { },
                            modifier = Modifier
                                .height(30.dp),
                            contentPadding = PaddingValues(0.dp)
                        )
                    }

                    Text("Yet another video", color = Color.White)
                }
            )

            Column(
                modifier = Modifier
                    .weight(0.15f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
                content = {
                    IconButton(
                        onClick = {
                            isMuted = !isMuted
                            exoPlayer.volume = if (isMuted) 0f else 1f
                        },
                        content = {
                            val icon =
                                if (isMuted) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff

                            Icon(
                                imageVector = icon,
                                contentDescription = "Mute",
                                tint = Color.White
                            )
                        }
                    )

                    IconButton(
                        onClick = onFavoriteAction,
                        content = {
                            val icon = if (isFavorite) {
                                painterResource(R.drawable.favorite_fill)
                            } else {
                                painterResource(id = R.drawable.favorite)
                            }

                            Icon(
                                painter = icon,
                                contentDescription = "Favorite",
                                tint = Color.White
                            )
                        }
                    )


                    IconButton(
                        onClick = onLikeAction,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.upvote),
                                contentDescription = "Like",
                                tint = Color.White
                            )
                        }
                    )

                    /* IconButton(
                         onClick = { },
                         content = {
                             Icon(
                                 painter = painterResource(id = R.drawable.downvote),
                                 contentDescription = "Dislike",
                                 tint = Color.White
                             )
                         }
                     )*/


                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, videoURL)
                            }

                            val shareIntent = Intent.createChooser(intent, "Share")
                            context.startActivity(shareIntent)
                        },
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }
                    )

                    /*IconButton(
                        onClick = { },
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.comment),
                                contentDescription = "Comment",
                                tint = Color.White
                            )
                        }
                    )*/
                }
            )
        }

        if (errorMessage != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = errorMessage!!,
                    color = Color.White
                )

                Button(
                    onClick = {
                        reloadVideo()
                    },
                    content = {
                        Text("Retry")
                    }
                )
            }
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}