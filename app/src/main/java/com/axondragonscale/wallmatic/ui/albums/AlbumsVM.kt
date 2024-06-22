package com.axondragonscale.wallmatic.ui.albums

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 23/06/24
 */
@HiltViewModel
class AlbumsVM @Inject constructor(): ViewModel() {

    val uiState = MutableStateFlow(AlbumsUiState())

    fun onEvent(event: AlbumsUiEvent) {

    }

}
