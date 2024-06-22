package com.axondragonscale.wallmatic.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 09/06/24
 */
@HiltViewModel
class SettingsVM @Inject constructor(
    private val appPrefsRepository: AppPrefsRepository,
) : ViewModel() {

    val uiState = MutableStateFlow(SettingsUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appPrefsRepository.uiModeFlow.collect { uiMode ->
                uiState.update { it.copy(uiMode = uiMode) }
            }
        }
        // This flow is not collected if its in the same coroutine.
        viewModelScope.launch(Dispatchers.IO) {
            appPrefsRepository.dynamicThemeFlow.collect { dynamicTheme ->
                uiState.update { it.copy(dynamicTheme = dynamicTheme) }
            }
        }
    }

    fun onEvent(event: SettingsUiEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is SettingsUiEvent.UIModeUpdate ->
                appPrefsRepository.setUiMode(event.uiMode)

            is SettingsUiEvent.DynamicThemeToggle ->
                appPrefsRepository.setDynamicTheme(event.dynamicTheme)
        }
    }

}
