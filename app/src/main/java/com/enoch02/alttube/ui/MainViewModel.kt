package com.enoch02.alttube.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(private val supabase: SupabaseClient) : ViewModel() {
    var userInfo by mutableStateOf<UserInfo?>(null)

    private fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }

    /*
    fun signIn(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val storedUserId = prefs.getString("user_id", null)
                val storedRefreshToken = prefs.getString("refresh_token", null)

                if (storedUserId != null && storedRefreshToken != null) {
                    try {
                        supabase.auth.refreshSession(storedRefreshToken)
                    } catch (e: Exception) {
                        supabase.auth.signInAnonymously()
                        val newUserId = supabase.auth.currentUserOrNull()?.id
                        val newSession = supabase.auth.currentSessionOrNull()
                        prefs.edit()
                            .putString("user_id", newUserId)
                            .putString("refresh_token", newSession?.refreshToken)
                            .apply()
                    }
                } else {
                    supabase.auth.signInAnonymously()
                    val newUserId = supabase.auth.currentUserOrNull()?.id
                    val newSession = supabase.auth.currentSessionOrNull()
                    prefs.edit()
                        .putString("user_id", newUserId)
                        .putString("refresh_token", newSession?.refreshToken)
                        .apply()
                }

                getCurrentUserId()?.let {
                    saveUserInfo(userInfo = UserInfo(supabase_user_id = it))
                }
            } catch (e: Exception) {
                // Handle any errors
                Log.e("Auth", "Error during sign in: ${e.message}")
            }
        }
    }
     */

    fun signIn(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val storedUserId = prefs.getString("user_id", null)
                val storedRefreshToken = prefs.getString("refresh_token", null)

                if (storedUserId != null && storedRefreshToken != null) {
                    try {
                        // Try to refresh the session with the refresh token
                        val newSession = supabase.auth.refreshSession(storedRefreshToken)

                        // Save new access and refresh tokens
                        prefs.edit()
                            .putString("user_id", newSession.user?.id)
                            .putString("access_token", newSession.accessToken)
                            .putString("refresh_token", newSession.refreshToken)
                            .apply()
                    } catch (e: Exception) {
                        Log.e(
                            "Auth",
                            "Session refresh failed: ${e.message}. Signing in anonymously."
                        )

                        signInAnonymouslyAndSaveTokens(prefs)
                    }
                } else {
                    // No stored tokens — sign in anonymously
                    signInAnonymouslyAndSaveTokens(prefs)
                }

                // Save user info after signing in or refreshing
                getCurrentUserId()?.let {
                    saveUserInfo(userInfo = UserInfo(supabase_user_id = it))
                }
            } catch (e: Exception) {
                Log.e("Auth", "Error during sign in: ${e.message}")
            }
        }
    }

    private suspend fun signInAnonymouslyAndSaveTokens(prefs: SharedPreferences) {
        try {
            supabase.auth.signInAnonymously()
            val newUserId = supabase.auth.currentUserOrNull()?.id
            val newSession = supabase.auth.currentSessionOrNull()

            // Save new session details
            prefs.edit()
                .putString("user_id", newUserId)
                .putString("access_token", newSession?.accessToken)
                .putString("refresh_token", newSession?.refreshToken)
                .apply()
        } catch (e: Exception) {
            Log.e("Auth", "Anonymous sign-in failed: ${e.message}")
        }
    }

    private fun saveUserInfo(userInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = getCurrentUserId()
            if (userId != null) {
                try {
                    supabase
                        .from("users")
                        .upsert(userInfo) {
                            onConflict = "supabase_user_id"
                        }


                    Log.d(TAG, "User info saved successfully!")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save user info: ${e.message}")
                }
            } else {
                Log.e(TAG, "No user signed in")
            }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = getCurrentUserId()
            if (userId != null) {
                try {
                    userInfo = supabase.from("users")
                        .select() {
                            filter {
                                eq("supabase_user_id", userId)
                            }
                        }
                        .decodeSingle<UserInfo>()
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to get user info: ${e.message}")
                }
            } else {
                Log.e(TAG, "No user signed in")
            }
        }
    }

    fun updateUserInfo(theUserInfo: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = getCurrentUserId()
            if (userId != null) {
                try {
                    supabase.from("users")
                        .update(theUserInfo) {
                            filter {
                                eq("supabase_user_id", userId)
                            }
                        }

                    userInfo = theUserInfo
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to update user info: ${e.message}")
                }
            } else {
                Log.e(TAG, "No user signed in")
            }
        }
    }

    fun deleteVideo(context: Context, url: String, bucketName: String = "videos") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userInfo?.let { userInfo ->
                    val updatedUploadsList = userInfo.uploads?.toMutableList() ?: mutableListOf()
                    val updatedFavoritesList =
                        userInfo.favorites?.toMutableList() ?: mutableListOf()
                    val updatedLikesList = userInfo.liked?.toMutableList() ?: mutableListOf()
                    updatedUploadsList.remove(url)
                    updatedFavoritesList.remove(url)
                    updatedLikesList.remove(url)

                    updateUserInfo(
                        userInfo.copy(
                            uploads = updatedUploadsList,
                            favorites = updatedFavoritesList,
                            liked = updatedLikesList
                        )
                    )
                }

                val fileName = url.split("/").last()
                val bucket = supabase.storage.from(bucketName)

                bucket.delete(fileName)
                getUserInfo()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Upload deleted!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "deleteVideo: ${e.message}")
            }
        }
    }
}

@Serializable
data class UserInfo(
    val id: String? = null, // Will be autogenerated by Supabase
    val supabase_user_id: String? = null,
    val uploads: List<String>? = null,
    val liked: List<String>? = null,
    val favorites: List<String>? = null,
)