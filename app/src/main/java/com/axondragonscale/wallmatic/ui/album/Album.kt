package com.axondragonscale.wallmatic.ui.album

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.axondragonscale.wallmatic.model.FullAlbum
import com.axondragonscale.wallmatic.model.FullFolder
import com.axondragonscale.wallmatic.ui.Route
import com.axondragonscale.wallmatic.ui.common.AlbumNameDialog
import com.axondragonscale.wallmatic.ui.common.FluidFabButton
import com.axondragonscale.wallmatic.ui.common.FluidFabButtonProperties
import com.axondragonscale.wallmatic.ui.common.Wallpaper
import com.axondragonscale.wallmatic.ui.theme.SystemBars
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme

/**
 * Created by Ronak Harkhani on 23/06/24
 */

@Composable
fun Album(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    albumId: Int,
) {
    val vm: AlbumVM = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    SystemBars(statusBarColor = MaterialTheme.colorScheme.primaryContainer)
    Album(
        modifier = modifier.statusBarsPadding(),
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is AlbumUiEvent.NavigateToFolder ->
                    navController.navigate(Route.Folder(event.folderId))
                is AlbumUiEvent.NavigateToWallpaper ->
                    navController.navigate(Route.Wallpaper(event.wallpaperId))
                is AlbumUiEvent.DeleteAlbum ->
                    navController.popBackStack()

                else -> vm.onEvent(event)
            }
        },
    )
}

@Composable
private fun Album(
    modifier: Modifier = Modifier,
    uiState: AlbumUiState,
    onEvent: (AlbumUiEvent) -> Unit,
) {
    if (uiState.album == null) return
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                albumName = uiState.album.name,
                onRename = { onEvent(AlbumUiEvent.RenameAlbum(it)) },
                onDelete = { onEvent(AlbumUiEvent.DeleteAlbum) },
            )

            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (folder in uiState.album.folders) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Folder(
                            folder = folder,
                            onClick = { onEvent(AlbumUiEvent.NavigateToFolder(folder.id)) },
                            onWallpaperClick = { onEvent(AlbumUiEvent.NavigateToWallpaper(it)) }
                        )
                    }
                }

                items(uiState.album.wallpapers) {
                    Wallpaper(
                        uri = it.uri,
                        onClick = { onEvent(AlbumUiEvent.NavigateToWallpaper(it.id)) }
                    )
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        }

        val context = LocalContext.current
        PickerButton(
            modifier = Modifier.navigationBarsPadding(),
            onFolderSelected = { onEvent(AlbumUiEvent.FolderSelected(context, it)) },
            onImagesSelected = { onEvent(AlbumUiEvent.ImagesSelected(context, it)) },
        )
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    albumName: String,
    onRename: (String) -> Unit,
    onDelete: () -> Unit,
) {
    var showRenameAlbumDialog by remember { mutableStateOf(false) }
    if (showRenameAlbumDialog) {
        AlbumNameDialog(
            currentAlbumName = albumName,
            onDismiss = { showRenameAlbumDialog = false },
            onConfirm = { newAlbumName ->
                onRename(newAlbumName)
                showRenameAlbumDialog = false
            }
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.clip(CircleShape)
        ) {
            var isExpanded by remember { mutableStateOf(false) }
            Icon(
                modifier = Modifier
                    .clickable { isExpanded = true }
                    .padding(16.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
            ) {
                DropdownMenu(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    offset = DpOffset(x = -8.dp, y = 0.dp),
                ) {
                    DropdownMenuItem(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                        onClick = {
                            showRenameAlbumDialog = true
                            isExpanded = false
                        },
                        text = { Text("Rename") },
                    )

                    DropdownMenuItem(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                        onClick = {
                            onDelete()
                            isExpanded = false
                        },
                        text = { Text("Delete") },
                    )
                }
            }
        }
    }
}

@Composable
private fun Folder(
    modifier: Modifier = Modifier,
    folder: FullFolder,
    onClick: () -> Unit,
    onWallpaperClick: (wallpaperId: Int) -> Unit,
) {
    Card(modifier = modifier.clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = folder.name,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${folder.wallpapers.size} wallpapers",
                style = MaterialTheme.typography.labelSmall
            )
        }
        LazyRow(
            modifier = Modifier.height(300.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(folder.wallpapers) {
                Wallpaper(
                    uri = it.uri,
                    onClick = { onWallpaperClick(it.id) },
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
