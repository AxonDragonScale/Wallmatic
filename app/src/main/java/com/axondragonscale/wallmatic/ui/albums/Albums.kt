package com.axondragonscale.wallmatic.ui.albums

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.axondragonscale.wallmatic.ui.Route
import com.axondragonscale.wallmatic.ui.bottombar.BOTTOM_BAR_HEIGHT
import com.axondragonscale.wallmatic.ui.common.AlbumNameDialog
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme

/**
 * Created by Ronak Harkhani on 09/06/24
 */

@Composable
fun Albums(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val vm: AlbumsVM = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    var showCreateAlbumDialog by rememberSaveable { mutableStateOf(false) }

    Albums(
        modifier = modifier,
        uiState = uiState,
        onEvent = {
            when (it) {
                is AlbumsUiEvent.ShowCreateAlbumDialog -> showCreateAlbumDialog = true
                else -> vm.onEvent(it)
            }
        },
    )

    if (showCreateAlbumDialog) {
        AlbumNameDialog(
            onDismiss = { showCreateAlbumDialog = false },
            onConfirm = {
                vm.onEvent(AlbumsUiEvent.CreateAlbum(it))
                navController.navigate(route = Route.Album)
            },
        )
    }
}

@Composable
private fun Albums(
    modifier: Modifier = Modifier,
    uiState: AlbumsUiState,
    onEvent: (AlbumsUiEvent) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(BOTTOM_BAR_HEIGHT))
        }

        FloatingActionButton(
            modifier = Modifier
                .align(BottomEnd)
                .padding(bottom = BOTTOM_BAR_HEIGHT)
                .padding(8.dp),
            onClick = { onEvent(AlbumsUiEvent.ShowCreateAlbumDialog) },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = ""
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
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
