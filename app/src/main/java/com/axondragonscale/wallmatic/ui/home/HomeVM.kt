package com.axondragonscale.wallmatic.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.model.copy
import com.axondragonscale.wallmatic.repository.WallmaticRepository
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
    private val wallmaticRepository: WallmaticRepository,
    private val appPrefsRepository: AppPrefsRepository,
) : ViewModel() {

    val uiState = MutableStateFlow(HomeUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                appPrefsRepository.configFlow,
                wallmaticRepository.getAlbums()
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
                    if (event.target.isHome())
                        homeConfig = homeConfig.copy {
                            albumId = event.albumId
                            autoCycleEnabled = true
                        }
                    if (event.target.isLock())
                        lockConfig = lockConfig.copy {
                            albumId = event.albumId
                            autoCycleEnabled = true
                        }
                }
            )

            is HomeUiEvent.AutoCycleToggle -> appPrefsRepository.setConfig(
                uiState.value.config.copy {
                    if (event.target.isHome())
                        homeConfig = homeConfig.copy {
                            autoCycleEnabled = event.enabled
                        }
                    if (event.target.isLock())
                        lockConfig = lockConfig.copy {
                            autoCycleEnabled = event.enabled
                        }
                }
            )

            HomeUiEvent.MirrorHomeConfigForLockToggle -> appPrefsRepository.setConfig(
                uiState.value.config.copy {
                    mirrorHomeConfigForLock = !mirrorHomeConfigForLock
                }
            )

            // Handled in Compose
            HomeUiEvent.CreateAlbumClick -> Unit
            is HomeUiEvent.SelectAlbumClick -> Unit
        }
    }
}
