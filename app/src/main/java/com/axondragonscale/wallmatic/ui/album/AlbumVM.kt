package com.axondragonscale.wallmatic.ui.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.database.entity.Folder
import com.axondragonscale.wallmatic.database.entity.Wallpaper
import com.axondragonscale.wallmatic.repository.AlbumRepository
import com.axondragonscale.wallmatic.util.FileUtil
import com.axondragonscale.wallmatic.util.takePersistableUriPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 23/06/24
 */
@HiltViewModel
internal class AlbumVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: AlbumRepository,
) : ViewModel() {

    private val albumId: Int = savedStateHandle.get<Int>("albumId")!!
    val uiState = MutableStateFlow(AlbumUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            syncUiStateWithAlbum()
        }
    }

    fun onEvent(event: AlbumUiEvent): Any = when (event) {
        is AlbumUiEvent.FolderSelected -> onFolderSelected(event)
        is AlbumUiEvent.ImagesSelected -> onImagesSelected(event)
        is AlbumUiEvent.RenameAlbum -> onRenameAlbum(event)
        AlbumUiEvent.DeleteAlbum -> onDeleteAlbum()

        // Handled by View
        is AlbumUiEvent.NavigateToWallpaper -> Unit
    }

    private suspend fun syncUiStateWithAlbum() {
        val album = repository.getFullAlbum(albumId)
        uiState.update { it.copy(album = album, loading = false) }
    }

    private fun onFolderSelected(
        event: AlbumUiEvent.FolderSelected,
    ) = viewModelScope.launch(Dispatchers.IO) {
        // TODO: Move main logic to repository? Event won't need context then.

        val (context, uri) = event
        context.takePersistableUriPermission(uri)

        val fullAlbum = uiState.value.album ?: return@launch
        if (uri.toString() in fullAlbum.folders.map { it.folderUri }) return@launch

        val wallpapers = FileUtil.getAllWallpapersInFolder(context, uri)
        if (wallpapers.isEmpty()) return@launch

        val wallpaperEntities = wallpapers.map { Wallpaper(it.toString()) }
        val wallpaperIds = repository.saveWallpapers(wallpaperEntities)

        val folder = Folder(
            name = FileUtil.getFolderName(context, uri),
            coverUri = wallpapers.random().toString(),
            folderUri = uri.toString(),
            wallpapers = wallpaperIds,
        )
        val folderId = repository.saveFolder(folder)

        repository.updateAlbum(albumId) {
            folderIds = folderIds + folderId
            if (coverUri == null) coverUri = wallpapers.random().toString()
        }

        syncUiStateWithAlbum()
    }

    private fun onImagesSelected(
        event: AlbumUiEvent.ImagesSelected,
    ) = viewModelScope.launch(Dispatchers.IO) {
        // TODO: Move main logic to repository? Event won't need context then.

        val (context, uris) = event
        uris.forEach { uri ->
            context.takePersistableUriPermission(uri)
        }

        val fullAlbum = uiState.value.album ?: return@launch
        val newWallpapers = uris.filter { it.toString() !in fullAlbum.wallpapers.map { it.uri } }
        val newWallpaperEntities = newWallpapers.map { Wallpaper(it.toString()) }
        val newWallpaperIds = repository.saveWallpapers(newWallpaperEntities)

        repository.updateAlbum(albumId) {
            wallpaperIds = wallpaperIds + newWallpaperIds
            if (coverUri == null) coverUri = newWallpapers.random().toString()
        }

        syncUiStateWithAlbum()
    }

    private fun onRenameAlbum(
        event: AlbumUiEvent.RenameAlbum,
    ) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateAlbum(albumId) {
            name = event.albumName
        }
        syncUiStateWithAlbum()
    }

    private fun onDeleteAlbum() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlbum(albumId)
        }
}
