package com.axondragonscale.wallmatic.ui.wallpaper

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.database.entity.Wallpaper
import com.axondragonscale.wallmatic.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 11/07/24
 */
@HiltViewModel
class WallpaperVM @Inject constructor(
    private val repository: AlbumRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val wallpaperId = savedStateHandle.get<Int>("wallpaperId")!!
    val uiState = MutableStateFlow(WallpaperUiState(Wallpaper("")))

    init {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.update {
                it.copy(wallpaper = repository.getWallpaper(wallpaperId))
            }
        }
    }

    fun onEvent(event: WallpaperUiEvent) = when (event) {

        else -> {}
    }
}
