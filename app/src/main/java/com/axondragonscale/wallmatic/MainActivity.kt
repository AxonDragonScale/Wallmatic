package com.axondragonscale.wallmatic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axondragonscale.wallmatic.model.UIMode
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import com.axondragonscale.wallmatic.ui.WallmaticApp
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appPrefsRepository: AppPrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        val (initialUiMode, initialDynamicTheme) = runBlocking {
            appPrefsRepository.uiModeFlow.first() to appPrefsRepository.dynamicThemeFlow.first()
        }
        keepSplashScreen = false

        setContent {
            val uiMode by appPrefsRepository.uiModeFlow
                .collectAsStateWithLifecycle(initialValue = initialUiMode)
            val dynamicTheme by appPrefsRepository.dynamicThemeFlow
                .collectAsStateWithLifecycle(initialValue = initialDynamicTheme)

            WallmaticTheme(
                darkTheme = isDarkTheme(uiMode),
                dynamicColor = dynamicTheme,
            ) {
                WallmaticApp()
            }
        }
    }

    @Composable
    private fun isDarkTheme(uiMode: UIMode) = when (uiMode) {
        UIMode.LIGHT -> false
        UIMode.DARK -> true
        UIMode.AUTO -> isSystemInDarkTheme()
    }

}
