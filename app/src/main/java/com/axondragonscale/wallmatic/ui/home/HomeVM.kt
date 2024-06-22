package com.axondragonscale.wallmatic.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 22/06/24
 */
@HiltViewModel
class HomeVM @Inject constructor(): ViewModel() {

    val uiState = MutableStateFlow(HomeUiState())

    fun onEvent(event: HomeUiEvent) = viewModelScope.launch {

    }
}
