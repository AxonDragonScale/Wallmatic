package com.axondragonscale.wallmatic.ui.album

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 23/06/24
 */
@HiltViewModel
internal class AlbumVM @Inject constructor(): ViewModel() {

    val uiState = MutableStateFlow(AlbumUiState())

    fun onEvent(event: AlbumUiEvent) {

    }
}
