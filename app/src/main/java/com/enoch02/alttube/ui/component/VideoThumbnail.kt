package com.enoch02.alttube.ui.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoThumbnail(videoUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var showVideoPlayerModal by remember { mutableStateOf(false) }

    LaunchedEffect(videoUrl) {
        try {
            bitmap = withContext(Dispatchers.IO) {
                Glide.with(context)
                    .asBitmap()
                    .load(videoUrl)
                    .apply(RequestOptions().frame(5000000))
                    .submit()
                    .get()
            }
        } catch (e: Exception) {
            Log.e("VideoThumbnailComposable", "${e.message}")
        }
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { showVideoPlayerModal = true }
        )
    }

    if (showVideoPlayerModal) {
        ModalBottomSheet(
            onDismissRequest = { showVideoPlayerModal = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            content = {
                val player =
                    ExoPlayer.Builder(context)
                        .setRenderersFactory(
                            DefaultRenderersFactory(context)
                                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
                                .setEnableDecoderFallback(true)
                        )
                        .build()
                        .apply {
                            setMediaItem(MediaItem.fromUri(videoUrl))
                            prepare()
                        }

                DisposableEffect(Unit) {
                    onDispose {
                        player.release()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    content = {
                        IconButton(
                            onClick = { showVideoPlayerModal = false },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Modal"
                                )
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                )

                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            this.player = player
                            player.play()
                            this.setShowNextButton(false)
                            this.setShowPreviousButton(false)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.9f)
                )
            }
        )
    }
}