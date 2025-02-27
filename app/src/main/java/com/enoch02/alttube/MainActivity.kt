package com.enoch02.alttube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.enoch02.alttube.navigation.AltTubeNavHost
import com.enoch02.alttube.ui.MainViewModel
import com.enoch02.alttube.ui.theme.AltTubeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.signIn(this@MainActivity)
        setContent {
            AltTubeTheme {
                AltTubeNavHost()
            }
        }
    }
}