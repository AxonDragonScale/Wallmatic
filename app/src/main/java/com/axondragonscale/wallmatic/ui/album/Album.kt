package com.axondragonscale.wallmatic.ui.album

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axondragonscale.wallmatic.ui.common.FluidFabButton
import com.axondragonscale.wallmatic.ui.common.FluidFabButtonProperties
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme
import com.axondragonscale.wallmatic.util.takePersistableUriPermission

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
    if (uiState.loading) return
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = uiState.album?.name ?: "Loading...")

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
            ) {
                items(uiState.album!!.folders) {
                    Text(modifier = Modifier.fillMaxWidth().height(50.dp), text = it.name)
                }

                items(uiState.album.wallpapers) {
                    Text(modifier = Modifier.fillMaxWidth().height(50.dp), text = it.uri)
                }
            }
        }

        val context = LocalContext.current
        PickerButton(
            onFolderSelected = { onEvent(AlbumUiEvent.FolderSelected(context, it)) },
            onImagesSelected = { onEvent(AlbumUiEvent.ImagesSelected(context, it)) },
        )
    }
}

@Composable
fun BoxScope.PickerButton(
    modifier: Modifier = Modifier,
    onFolderSelected: (Uri) -> Unit,
    onImagesSelected: (List<Uri>) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri -> if (uri != null) onFolderSelected(uri) }
    )

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris -> if (uris.isNotEmpty()) onImagesSelected(uris) }
    )

//    // Photo Picker. Provided by Photos app. Albums available.
//    val photoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickMultipleVisualMedia(),
//        onResult = {
//            println("zeref $it")
//        }
//    )

    FluidFabButton(
        modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 24.dp),
        isExpanded = isExpanded,
        duration = 500,
        onClick = { isExpanded = !isExpanded },
        leftButton = FluidFabButtonProperties(
            icon = Icons.Default.Folder,
            onClick = {
                isExpanded = !isExpanded
                folderPickerLauncher.launch(null)
            }
        ),
        rightButton = FluidFabButtonProperties(
            icon = Icons.Default.Image,
            onClick = {
                isExpanded = !isExpanded
                imagePickerLauncher.launch(arrayOf("image/*"))
            }
        ),
    )
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
