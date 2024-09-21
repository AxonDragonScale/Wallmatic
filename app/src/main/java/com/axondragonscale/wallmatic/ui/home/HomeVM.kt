package com.axondragonscale.wallmatic.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.model.copy
import com.axondragonscale.wallmatic.repository.AlbumRepository
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import com.axondragonscale.wallmatic.util.collect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 22/06/24
 */
@HiltViewModel
class HomeVM @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val appPrefsRepository: AppPrefsRepository,
) : ViewModel() {

    val uiState = MutableStateFlow(HomeUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                appPrefsRepository.configFlow,
                albumRepository.getAlbums()
            ) { config, albums ->
                uiState.update {
                    it.copy(
                        isLoading = false,
                        config = config,
                        albums = albums,
                    )
                }
            }.collect()
        }
    }

    fun onEvent(event: HomeUiEvent) = viewModelScope.launch {
        when (event) {
            is HomeUiEvent.SelectAlbum -> appPrefsRepository.setConfig(
                uiState.value.config.copy {
                    homeConfig = homeConfig.copy { albumId = event.albumId }
                }
            )

            // Handled in Compose
            HomeUiEvent.CreateAlbumClick -> Unit
            HomeUiEvent.SelectAlbumClick -> Unit
        }
    }
}
