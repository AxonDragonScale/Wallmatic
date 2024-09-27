package com.axondragonscale.wallmatic.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.background.WallmaticScheduler
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import com.axondragonscale.wallmatic.util.DevTools
import com.axondragonscale.wallmatic.util.collect
import com.axondragonscale.wallmatic.util.homeConfig
import com.axondragonscale.wallmatic.util.lockConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

/**
 * Created by Ronak Harkhani on 09/06/24
 */
@HiltViewModel
internal class SettingsVM @Inject constructor(
    private val appPrefsRepository: AppPrefsRepository,
    private val devTools: DevTools,
    private val scheduler: WallmaticScheduler,
) : ViewModel() {

    val uiState = MutableStateFlow(SettingsUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                appPrefsRepository.uiModeFlow,
                appPrefsRepository.dynamicThemeFlow,
                appPrefsRepository.fastAutoCycleFlow,
            ) { uiMode, dynamicTheme, fastAutoCycle ->
                uiState.update {
                    it.copy(
                        uiMode = uiMode,
                        dynamicTheme = dynamicTheme,
                        fastAutoCycle = fastAutoCycle,
                    )
                }
            }.collect()
        }
    }

    fun onEvent(event: SettingsUiEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is SettingsUiEvent.UIModeUpdate ->
                appPrefsRepository.setUiMode(event.uiMode)

            is SettingsUiEvent.DynamicThemeToggle ->
                appPrefsRepository.setDynamicTheme(event.dynamicTheme)

            is SettingsUiEvent.ClearData ->
                devTools.clearData()

            is SettingsUiEvent.FastAutoCycleToggled -> {
                appPrefsRepository.setFastAutoCycle(event.fastAutoCycle)
                val config = appPrefsRepository.configFlow.firstOrNull() ?: return@launch
                val newUpdateInterval = if (event.fastAutoCycle) 1.minutes else 15.minutes
                appPrefsRepository.setConfig(
                    config
                        .homeConfig { updateInterval = newUpdateInterval.inWholeMilliseconds }
                        .lockConfig { updateInterval = newUpdateInterval.inWholeMilliseconds }
                )
                scheduler.scheduleNextUpdate()
            }
        }
    }

}
