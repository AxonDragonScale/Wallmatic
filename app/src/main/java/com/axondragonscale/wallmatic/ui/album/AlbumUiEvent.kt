package com.axondragonscale.wallmatic.ui.album

import android.content.Context
import android.net.Uri

/**
 * Created by Ronak Harkhani on 23/06/24
 */
sealed interface AlbumUiEvent {
    data class FolderSelected(val uri: Uri) : AlbumUiEvent
    data class ImagesSelected(val uris: List<Uri>): AlbumUiEvent
    data class NavigateToFolder(val folderId: Int): AlbumUiEvent
    data class NavigateToWallpaper(val wallpaperId: Int): AlbumUiEvent
    data class RenameAlbum(val albumName: String): AlbumUiEvent
    data object DeleteAlbum : AlbumUiEvent
}
