package com.enoch02.alttube.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(authInstance: FirebaseAuth) : ViewModel() {
    private val currentUser = authInstance.currentUser
    var userID by mutableStateOf(currentUser?.uid)
}