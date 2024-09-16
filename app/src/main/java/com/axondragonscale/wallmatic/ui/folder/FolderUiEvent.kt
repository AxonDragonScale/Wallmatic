package com.axondragonscale.wallmatic.ui.folder

/**
 * Created by Ronak Harkhani on 17/09/24
 */
sealed interface FolderUiEvent {
    data class NavigateToWallpaper(val wallpaperId: Int) : FolderUiEvent
}
