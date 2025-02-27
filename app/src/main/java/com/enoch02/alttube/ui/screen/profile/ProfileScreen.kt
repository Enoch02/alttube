package com.enoch02.alttube.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.enoch02.alttube.ui.MainViewModel

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    var selectedTab by remember {
        mutableIntStateOf(0)
    }
    val titles = listOf("Posts", "Favorites", "Likes")

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        content = {
            item {
                ProfileHeader(
                    userID = viewModel.userID ?: "",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                        0 -> Text("Tab 1")
                        1 -> Text("Tab 2")
                        2 -> Text("Tab 3")
                        else -> Text("Select a tab")
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