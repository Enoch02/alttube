package com.enoch02.alttube.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.enoch02.alttube.ui.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
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
            /*Text(text = "${viewModel.userInfo?.uploads}")*/

            ProfileHeader(
                userID = "anonymous",
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
                contentColor = Color.White
            )

            //TODO
            when (selectedTab) {
                0 -> {
                    if (viewModel.userInfo?.uploads != null) {
                        VideoList(
                            videoUrls = viewModel.userInfo!!.uploads!!,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                1 -> {
                    if (viewModel.userInfo?.favorites != null) {
                        VideoList(
                            videoUrls = viewModel.userInfo!!.uploads!!,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                2 -> {
                    if (viewModel.userInfo?.liked != null) {
                        VideoList(
                            videoUrls = viewModel.userInfo!!.uploads!!,
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
fun VideoList(modifier: Modifier = Modifier, videoUrls: List<String>) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(videoUrls) { videoUrl ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                ) {
                    VideoThumbnail(videoUrl = videoUrl)
                }
            }
        }
    )
}

@Composable
fun VideoThumbnail(videoUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    // Load the thumbnail asynchronously
    LaunchedEffect(videoUrl) {
        bitmap = withContext(Dispatchers.IO) {
            Glide.with(context)
                .asBitmap()
                .load(videoUrl)
                .apply(RequestOptions().frame(5000000))
                .submit()
                .get()
        }
    }

    // Show the image if loaded, otherwise show a placeholder
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
        )
    }
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