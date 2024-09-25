package com.axondragonscale.wallmatic.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.core.WallpaperUpdater
import com.axondragonscale.wallmatic.model.config
import com.axondragonscale.wallmatic.model.copy
import com.axondragonscale.wallmatic.model.wallpaperConfig
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
import kotlin.time.Duration.Companion.minutes

/**
 * Created by Ronak Harkhani on 22/06/24
 */
@HiltViewModel
class HomeVM @Inject constructor(
    private val wallmaticRepository: WallmaticRepository,
    private val appPrefsRepository: AppPrefsRepository,
    private val wallpaperUpdater: WallpaperUpdater,
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
            is HomeUiEvent.AlbumSelected -> onAlbumSelected(event)
            is HomeUiEvent.AutoCycleToggled -> onAutoCycleToggled(event)
            HomeUiEvent.MirrorHomeConfigForLockToggled -> onMirrorHomeConfigForLockToggled()
            is HomeUiEvent.ChangeWallpaper -> wallpaperUpdater.updateWallpaper(event.target)

            // Handled in Compose
            HomeUiEvent.CreateAlbumClick -> Unit
            is HomeUiEvent.SelectAlbumClick -> Unit
        }
    }

    private suspend fun onAlbumSelected(event: HomeUiEvent.AlbumSelected) {
        appPrefsRepository.setConfig(
            if (!uiState.value.config.isInit) {
                // First time initialization
                config {
                    isInit = true
                    mirrorHomeConfigForLock = true
                    homeConfig = wallpaperConfig {
                        albumId = event.albumId
                        autoCycleEnabled = true
                        currentWallpaperId = -1 // TODO: Randomly choose a wallpaper from album. Use id here and do async wallpaper set
                        updateInterval = 15.minutes.inWholeMilliseconds
                        lastUpdated = System.currentTimeMillis()
                    }
                    lockConfig = homeConfig // TODO: Verify this works
                }
            } else {
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
            }
        )
    }

    private suspend fun onAutoCycleToggled(event: HomeUiEvent.AutoCycleToggled) {
        appPrefsRepository.setConfig(
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
    }

    private suspend fun onMirrorHomeConfigForLockToggled() {
        appPrefsRepository.setConfig(
            uiState.value.config.copy {
                mirrorHomeConfigForLock = !mirrorHomeConfigForLock
            }
        )
    }
}
