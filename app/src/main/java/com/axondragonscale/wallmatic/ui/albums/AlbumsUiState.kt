package com.axondragonscale.wallmatic.ui.albums

import com.axondragonscale.wallmatic.database.entity.Album

/**
 * Created by Ronak Harkhani on 23/06/24
 */
internal data class AlbumsUiState(
    val isLoading: Boolean = true,
    val albums: List<Album> = listOf()
)
