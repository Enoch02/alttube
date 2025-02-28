package com.enoch02.alttube.ui.screen.upload

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.enoch02.alttube.ui.MainViewModel

@OptIn(UnstableApi::class)
@Composable
fun UploadScreen(
    modifier: Modifier = Modifier,
    uploadViewModel: UploadViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var selectedVideo by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { selectedVideo = it }
    }

    LaunchedEffect(Unit) {
        mainViewModel.getUserInfo()

        if (selectedVideo == null) {
            launcher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
            )
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        content = {
            if (selectedVideo != null) {
                val player =
                    ExoPlayer.Builder(context)
                        .setRenderersFactory(
                            DefaultRenderersFactory(context)
                                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
                                .setEnableDecoderFallback(true)
                        )
                        .build()
                        .apply {
                            setMediaItem(MediaItem.fromUri(selectedVideo!!))
                            prepare()
                        }

                DisposableEffect(Unit) {
                    onDispose {
                        player.release()
                    }
                }

                AnimatedVisibility(uploadViewModel.isUploading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                if (uploadViewModel.showErrorDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            uploadViewModel.dismissErrorDialog()
                        },
                        title = { Text("Error") },
                        text = { Text("The video could not be uploaded.\nWould you like to retry?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    uploadViewModel.dismissErrorDialog()

                                    uploadViewModel.uploadVideo(
                                        context = context,
                                        uri = selectedVideo!!,
                                        onUploadComplete = {
                                            player.release()
                                            selectedVideo = null
                                            Toast.makeText(
                                                context,
                                                "Upload Complete",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                },
                                content = { Text("Retry", color = Color.White) }
                            )
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { uploadViewModel.dismissErrorDialog() },
                                content = { Text("Cancel", color = Color.White) }
                            )
                        }
                    )
                }

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

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                        .weight(0.1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        Button(
                            onClick = {
                                player.release()
                                selectedVideo = null
                            },
                            content = {
                                Text("Select Another")
                            },
                            modifier = Modifier.width(150.dp),
                            enabled = !uploadViewModel.isUploading
                        )

                        Button(
                            onClick = {
                                uploadViewModel.uploadVideo(
                                    context = context,
                                    uri = selectedVideo!!,
                                    onUploadComplete = { publicUrl ->
                                        player.release()
                                        selectedVideo = null

                                        mainViewModel.userInfo?.let { userInfo ->
                                            val updatedList =
                                                userInfo.uploads?.toMutableList() ?: mutableListOf()
                                            updatedList.add(publicUrl)

                                            mainViewModel.updateUserInfo(
                                                userInfo.copy(
                                                    uploads = updatedList
                                                )
                                            )
                                        }

                                        Toast.makeText(
                                            context,
                                            "Upload Complete",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            },
                            content = {
                                Text("Upload")
                            },
                            modifier = Modifier.width(150.dp),
                            enabled = !uploadViewModel.isUploading
                        )
                    }
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        Text("Tap the button to select a video")
                        Button(
                            onClick = {
                                selectedVideo = null
                                launcher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                            content = {
                                Text("Select")
                            }
                        )
                    }
                )
            }
        }
    )
}