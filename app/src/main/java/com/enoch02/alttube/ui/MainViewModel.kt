package com.enoch02.alttube.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(private val supabase: SupabaseClient) : ViewModel() {
    fun uploadVideo(file: File, bucketName: String = "videos", fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val storage = supabase.storage
                val bucket = storage[bucketName]

                bucket.upload(
                    path = fileName,
                    data = file.readBytes()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "uploadVideo: Failed to upload video: ${e.message}")
            }
        }
    }
}