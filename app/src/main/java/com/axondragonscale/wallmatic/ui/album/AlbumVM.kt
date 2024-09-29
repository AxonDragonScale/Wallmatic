package com.axondragonscale.wallmatic.ui.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.core.AlbumManager
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import com.axondragonscale.wallmatic.repository.WallmaticRepository
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
    private val repository: WallmaticRepository,
    private val appPrefsRepository: AppPrefsRepository,
    private val albumManager: AlbumManager,
) : ViewModel() {

    private val albumId: Int = savedStateHandle.get<Int>("albumId")!!
    val uiState = MutableStateFlow(AlbumUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appPrefsRepository.gridSizeFlow.collect { gridSize ->
                uiState.update { it.copy(gridSize = gridSize) }
            }
        }
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
        is AlbumUiEvent.NavigateToFolder -> Unit
    }

    private suspend fun syncUiStateWithAlbum() {
        val album = repository.getFullAlbum(albumId)
        uiState.update { it.copy(album = album) }
    }

    private fun onFolderSelected(
        event: AlbumUiEvent.FolderSelected,
    ) = viewModelScope.launch(Dispatchers.IO) {
        val fullAlbum = uiState.value.album ?: return@launch
        albumManager.forAlbum(fullAlbum).addFolder(event.uri)
        syncUiStateWithAlbum()
    }

    private fun onImagesSelected(
        event: AlbumUiEvent.ImagesSelected,
    ) = viewModelScope.launch(Dispatchers.IO) {
        val fullAlbum = uiState.value.album ?: return@launch
        albumManager.forAlbum(fullAlbum).addWallpapers(event.uris)
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
