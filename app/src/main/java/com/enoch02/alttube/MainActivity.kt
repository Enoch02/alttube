package com.enoch02.alttube

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.enoch02.alttube.navigation.AltTubeNavHost
import com.enoch02.alttube.ui.theme.AltTubeTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authInstance: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logIn()
        enableEdgeToEdge()
        setContent {
            AltTubeTheme {
                AltTubeNavHost()
            }
        }
    }

    private fun logIn() {
        authInstance.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = authInstance.currentUser
                    Toast.makeText(this, "Logged in anonymously!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}