package com.axondragonscale.wallmatic.ui.albums

/**
 * Created by Ronak Harkhani on 23/06/24
 */
internal sealed interface AlbumsUiEvent {
    object ShowCreateAlbumDialog : AlbumsUiEvent
    data class CreateAlbum(val albumName: String) : AlbumsUiEvent
}
