package com.axondragonscale.wallmatic.ui.album

import android.app.Activity
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axondragonscale.wallmatic.model.FullAlbum
import com.axondragonscale.wallmatic.model.FullFolder
import com.axondragonscale.wallmatic.ui.common.FluidFabButton
import com.axondragonscale.wallmatic.ui.common.FluidFabButtonProperties
import com.axondragonscale.wallmatic.ui.common.Wallpaper
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
    if (uiState.loading) return
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(albumName = uiState.album?.name ?: "")

            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (folder in uiState.album!!.folders) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Folder(folder = folder)
                    }
                }

                items(uiState.album.wallpapers) {
                    Wallpaper(uri = it.uri)
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
private fun TopBar(
    modifier: Modifier = Modifier,
    albumName: String,
) {
    val window = (LocalView.current.context as Activity).window
    val prevColor = remember { window.statusBarColor }
    val newColor = MaterialTheme.colorScheme.primaryContainer
    DisposableEffect(Unit) {
        window.statusBarColor = newColor.toArgb()
        onDispose { window.statusBarColor = prevColor }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Album",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = albumName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun Folder(
    modifier: Modifier = Modifier,
    folder: FullFolder,
) {
    Card(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            text = folder.name,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "${folder.wallpapers.size} wallpapers",
            style = MaterialTheme.typography.labelSmall
        )
        LazyRow(
            modifier = Modifier.height(300.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(folder.wallpapers) {
                Wallpaper(
                    uri = it.uri,
                    cornerRadius = 8.dp,
                )
            }
        }
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
            val uiState = remember {
                AlbumUiState(
                    loading = false,
                    album = FullAlbum(
                        id = 1,
                        name = "Album Name",
                        coverUri = null,
                        folders = listOf(
                            FullFolder(
                                id = 1,
                                name = "Folder 1",
                                coverUri = null,
                                folderUri = "null",
                                wallpapers = listOf(
                                    com.axondragonscale.wallmatic.database.entity.Wallpaper(""),
                                    com.axondragonscale.wallmatic.database.entity.Wallpaper(""),
                                    com.axondragonscale.wallmatic.database.entity.Wallpaper(""),
                                    com.axondragonscale.wallmatic.database.entity.Wallpaper(""),
                                )
                            )
                        ),
                        wallpapers = listOf(
                            com.axondragonscale.wallmatic.database.entity.Wallpaper(""),
                            com.axondragonscale.wallmatic.database.entity.Wallpaper(""),
                            com.axondragonscale.wallmatic.database.entity.Wallpaper(""),
                        ),
                    )
                )
            }
            Album(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoading() {
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
