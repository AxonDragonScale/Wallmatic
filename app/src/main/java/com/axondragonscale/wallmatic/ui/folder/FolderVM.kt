package com.axondragonscale.wallmatic.ui.folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import com.axondragonscale.wallmatic.repository.WallmaticRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 17/09/24
 */
@HiltViewModel
class FolderVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WallmaticRepository,
    private val appPrefsRepository: AppPrefsRepository,
) : ViewModel() {

    private val folderId: Int = savedStateHandle.get<Int>("folderId")!!
    val uiState = MutableStateFlow(FolderUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appPrefsRepository.gridSizeFlow.collect { gridSize ->
                uiState.value = uiState.value.copy(gridSize = gridSize)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            syncUiStateWithFolder()
        }
    }

    fun onEvent(event: FolderUiEvent): Any = when (event) {
        is FolderUiEvent.BlacklistWallpaper -> onBlacklistWallpaper(event)
        is FolderUiEvent.WhitelistWallpaper -> onWhitelistWallpaper(event)

        // Handled by view
        is FolderUiEvent.NavigateToWallpaper -> Unit
        is FolderUiEvent.ShowWallpaperActions -> Unit
    }

    private suspend fun syncUiStateWithFolder() {
        val folder = repository.getFullFolder(folderId)
        uiState.value = uiState.value.copy(folder = folder)
    }

    private inline fun runAndSyncState(
        crossinline block: suspend () -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        block()
        syncUiStateWithFolder()
    }

    private fun onBlacklistWallpaper(
        event: FolderUiEvent.BlacklistWallpaper,
    ) = runAndSyncState {
        repository.updateWallpaper(event.wallpaper.id) {
            isBlacklisted = true
        }
        // TODO: Update Cover
    }

    private fun onWhitelistWallpaper(
        event: FolderUiEvent.WhitelistWallpaper,
    ) = runAndSyncState {
        repository.updateWallpaper(event.wallpaper.id) {
            isBlacklisted = false
        }
        // TODO: Update Cover
    }

}
