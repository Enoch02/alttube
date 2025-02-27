package com.enoch02.alttube.ui.screen.upload

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

private const val TAG = "Upload"

@HiltViewModel
class UploadViewModel @Inject constructor(private val supabase: SupabaseClient) : ViewModel() {
    var isUploading by mutableStateOf(false)
    var showErrorDialog by mutableStateOf(false)

    fun dismissErrorDialog() {
        isUploading = false
        showErrorDialog = false
    }

    fun uploadVideo(
        context: Context,
        uri: Uri,
        bucketName: String = "videos",
        onUploadComplete: () -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            isUploading = true

            getFileFromUri(context, uri)
                .onSuccess {
                    try {
                        val storage = supabase.storage
                        val bucket = storage[bucketName]

                        bucket.upload(
                            path = it.name,
                            data = it.readBytes(),
                        )

                        onUploadComplete()
                        isUploading = false
                    } catch (e: Exception) {
                        isUploading = false
                        showErrorDialog = true
                        Log.e(TAG, "uploadVideo: Failed to upload video: ${e.message}")
                    }
                }
        }
    }

    private fun getFileFromUri(context: Context, uri: Uri): Result<File> {
        val contentResolver = context.contentResolver
        val fileName = contentResolver.query(uri, null, null, null, null).use { cursor ->
            val name = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor?.moveToFirst()
            name?.let { cursor.getString(it) } ?: ""
        }

        try {
            val input = contentResolver.openInputStream(uri)
            if (input != null) {
                val outputDir = File(context.cacheDir, "temp")
                outputDir.mkdirs()

                val outputFile = File(outputDir, fileName)
                val outputStream = FileOutputStream(outputFile)

                val buffer = ByteArray(4 * 1024)
                var bytesRead: Int

                while (input.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.flush()
                outputStream.close()
                input.close()

                return Result.success(outputFile)
            }
        } catch (e: IOException) {
            return Result.failure(e)
        }

        return Result.failure(IOException("File not found"))
    }
}
