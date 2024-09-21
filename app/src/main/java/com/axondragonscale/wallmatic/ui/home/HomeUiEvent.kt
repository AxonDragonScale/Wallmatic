package com.axondragonscale.wallmatic.ui.home

/**
 * Created by Ronak Harkhani on 22/06/24
 */
sealed interface HomeUiEvent {
    data object CreateAlbumClick : HomeUiEvent
    data object SelectAlbumClick : HomeUiEvent
    data class SelectAlbum(val albumId: Int): HomeUiEvent
}
