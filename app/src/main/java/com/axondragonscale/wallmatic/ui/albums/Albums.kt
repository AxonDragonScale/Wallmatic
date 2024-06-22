package com.axondragonscale.wallmatic.ui.albums

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme

/**
 * Created by Ronak Harkhani on 09/06/24
 */

@Composable
fun Albums(
    modifier: Modifier = Modifier,
    vm: AlbumsVM = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    Albums(
        modifier = modifier,
        uiState = uiState,
        onEvent = { vm.onEvent(it) },
    )
}

@Composable
fun Albums(
    modifier: Modifier = Modifier,
    uiState: AlbumsUiState,
    onEvent: (AlbumsUiEvent) -> Unit,
) {

}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsPreview() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            val uiState = remember { AlbumsUiState() }
            Albums(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}
