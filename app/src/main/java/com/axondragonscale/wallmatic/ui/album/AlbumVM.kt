package com.axondragonscale.wallmatic.ui.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.repository.AlbumRepository
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
): ViewModel() {

    val uiState = MutableStateFlow(AlbumUiState())

    init {
        val albumId = savedStateHandle.get<Int>("albumId")!!
        viewModelScope.launch(Dispatchers.IO) {
            val album = repository.getAlbum(albumId)
            uiState.update { it.copy(album = album.name) }
        }
    }

    fun onEvent(event: AlbumUiEvent) {

    }
}
