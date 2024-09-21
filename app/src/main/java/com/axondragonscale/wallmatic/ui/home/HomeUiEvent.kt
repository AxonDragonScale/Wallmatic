package com.axondragonscale.wallmatic.ui.home

import com.axondragonscale.wallmatic.model.TargetScreen

/**
 * Created by Ronak Harkhani on 22/06/24
 */
sealed interface HomeUiEvent {
    data object MirrorHomeConfigForLockToggle : HomeUiEvent
    data object CreateAlbumClick : HomeUiEvent
    data class SelectAlbumClick(val target: TargetScreen) : HomeUiEvent
    data class SelectAlbum(val albumId: Int, val target: TargetScreen): HomeUiEvent
    data class AutoCycleToggle(val enabled: Boolean, val target: TargetScreen): HomeUiEvent
}
