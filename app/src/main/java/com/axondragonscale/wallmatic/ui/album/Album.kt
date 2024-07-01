package com.axondragonscale.wallmatic.ui.album

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axondragonscale.wallmatic.ui.common.FluidFabButton
import com.axondragonscale.wallmatic.ui.common.FluidFabButtonProperties
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme

/**
 * Created by Ronak Harkhani on 23/06/24
 */

@Composable
fun Album(
    modifier: Modifier = Modifier,
    albumId: Int,
) {
    val vm: AlbumVM = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Album(
        modifier = modifier,
        uiState = uiState,
        onEvent = { vm.onEvent(it) },
    )
}

@Composable
private fun Album(
    modifier: Modifier = Modifier,
    uiState: AlbumUiState,
    onEvent: (AlbumUiEvent) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(
                text = uiState.album ?: "Loading...",
            )
        }

        var isExpanded by remember { mutableStateOf(false) }
        FluidFabButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            isExpanded = isExpanded,
            onClick = { isExpanded = !isExpanded },
            leftButton = FluidFabButtonProperties(
                icon = Icons.Default.Folder,
                onClick = {
                    isExpanded = !isExpanded
                    // TODO
                }
            ),
            rightButton = FluidFabButtonProperties(
                icon = Icons.Default.Image,
                onClick = {
                    isExpanded = !isExpanded
                    // TODO
                }
            ),
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            val uiState = remember { AlbumUiState() }
            Album(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}
