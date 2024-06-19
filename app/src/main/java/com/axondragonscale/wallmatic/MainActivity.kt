package com.axondragonscale.wallmatic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.axondragonscale.wallmatic.ui.WallmaticApp
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WallmaticTheme {
                WallmaticApp()
            }
        }
    }

}
