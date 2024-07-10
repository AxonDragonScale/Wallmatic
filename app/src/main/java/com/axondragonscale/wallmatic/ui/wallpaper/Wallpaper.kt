package com.axondragonscale.wallmatic.ui.wallpaper

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme

/**
 * Created by Ronak Harkhani on 11/07/24
 */

@Composable
fun Wallpaper(modifier: Modifier = Modifier) {
    val vm: WallpaperVM = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    Wallpaper(
        modifier = modifier,
        uiState = uiState,
        onEvent = { vm.onEvent(it) }
    )
}

@Composable
private fun Wallpaper(
    modifier: Modifier = Modifier,
    uiState: WallpaperUiState,
    onEvent: (WallpaperUiEvent) -> Unit,
) {
    AsyncImage(
        modifier = modifier.fillMaxSize(),
        model = uiState.wallpaper.uri,
        contentDescription = null,
        contentScale = ContentScale.Fit
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoading() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            Wallpaper()
        }
    }
}
