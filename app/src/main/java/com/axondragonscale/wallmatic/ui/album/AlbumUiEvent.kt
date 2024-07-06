package com.axondragonscale.wallmatic.ui.album

import android.content.Context
import android.net.Uri

/**
 * Created by Ronak Harkhani on 23/06/24
 */
sealed interface AlbumUiEvent {
    data class FolderSelected(val context: Context, val uri: Uri) : AlbumUiEvent
    data class ImagesSelected(val context: Context, val uris: List<Uri>): AlbumUiEvent
}
