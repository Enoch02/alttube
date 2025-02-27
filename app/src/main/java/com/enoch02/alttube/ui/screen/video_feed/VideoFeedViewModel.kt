package com.enoch02.alttube.ui.screen.video_feed

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "VideoFeed"

@HiltViewModel
class VideoFeedViewModel @Inject constructor(private val supabase: SupabaseClient) : ViewModel() {
    var contentState: ContentState by mutableStateOf(ContentState.Loading)

    fun loadPublicVideoURLs(bucketName: String = "videos") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                contentState = ContentState.Loading
                val bucket = supabase.storage.from(bucketName)
                val files = bucket.list()

                Log.e(TAG, "loadPublicVideoURLs: ${files.size}")

                contentState = ContentState.Loaded(
                    files.map { file ->
                        supabase.storage.from(bucketName).publicUrl(file.name)
                    }
                )
            } catch (e: HttpRequestException) {
                Log.e(TAG, "loadPublicVideoURLs: ${e.message}")
                contentState = ContentState.Error("Check Your Internet Connection")
            } catch (e: Exception) {
                Log.e(TAG, "loadPublicVideoURLs: ${e.message}")
                contentState = ContentState.Error(e.message.toString())
            }
        }
    }
}

sealed class ContentState {
    data object Loading : ContentState()
    data class Loaded(val videos: List<String>) : ContentState()
    data class Error(val message: String) : ContentState()
}
