package com.axondragonscale.wallmatic.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 23/06/24
 */
@HiltViewModel
internal class AlbumsVM @Inject constructor(
    private val repository: AlbumRepository,
): ViewModel() {

    val uiState = MutableStateFlow(AlbumsUiState())

    private val uiEffectChannel = Channel<AlbumsUiEffect>()
    val uiEffect = uiEffectChannel.receiveAsFlow().flowOn(Dispatchers.Main.immediate)

    fun onEvent(event: AlbumsUiEvent): Any = when (event) {
        is AlbumsUiEvent.CreateAlbum -> createAlbum(event)

        // Handled in the view
        AlbumsUiEvent.ShowCreateAlbumDialog -> Unit
    }

    private fun createAlbum(event: AlbumsUiEvent.CreateAlbum) = viewModelScope.launch(Dispatchers.IO) {
        val albumId = repository.createAlbum(event.albumName)
        uiEffectChannel.send(AlbumsUiEffect.NavigateToAlbum(albumId))
    }

}
