package com.enoch02.alttube.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.enoch02.alttube.ui.MainViewModel
import com.enoch02.alttube.ui.component.VideoThumbnail

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var selectedTab by remember {
        mutableIntStateOf(0)
    }
    val titles = listOf("My Posts", "Favorites", "Likes")

    LaunchedEffect(Unit) {
        viewModel.getUserInfo()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        content = {
            ProfileHeader(
                userID = "${viewModel.userInfo?.supabase_user_id}",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                tabs = {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                },
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.background
            )

            when (selectedTab) {
                0 -> {
                    if (viewModel.userInfo?.uploads != null) {
                        VideoList(
                            videoUrls = viewModel.userInfo!!.uploads!!,
                            modifier = Modifier.fillMaxSize(),
                            showDeleteIcon = true,
                            onDelete = { videoUrl ->
                                viewModel.deleteVideo(context = context, url = videoUrl)
                            }
                        )
                    }
                }

                1 -> {
                    if (viewModel.userInfo?.favorites != null) {
                        VideoList(
                            videoUrls = viewModel.userInfo!!.favorites!!,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                2 -> {
                    if (viewModel.userInfo?.liked != null) {
                        VideoList(
                            videoUrls = viewModel.userInfo!!.liked!!,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                else -> {}
            }
        }
    )
}

@Composable
fun VideoList(
    modifier: Modifier = Modifier,
    videoUrls: List<String>,
    showDeleteIcon: Boolean = false,
    onDelete: (video: String) -> Unit = {},
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(videoUrls) { videoUrl ->
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                ) {
                    VideoThumbnail(
                        videoUrl = videoUrl,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    if (showDeleteIcon) {
                        IconButton(
                            onClick = {
                                onDelete(videoUrl)
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Video"
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun ProfileHeader(modifier: Modifier = Modifier, userID: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "Anonymous", style = MaterialTheme.typography.titleLarge)

        Text(text = "@$userID", style = MaterialTheme.typography.bodySmall)
    }
}