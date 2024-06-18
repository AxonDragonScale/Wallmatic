package com.axondragonscale.wallmatic.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 09/06/24
 */
@HiltViewModel
class SettingsVM @Inject constructor() : ViewModel() {

    val uiState = MutableStateFlow(SettingsUiState(1, true))

    fun onEvent(event: SettingsUiEvent) = viewModelScope.launch {

    }

}
