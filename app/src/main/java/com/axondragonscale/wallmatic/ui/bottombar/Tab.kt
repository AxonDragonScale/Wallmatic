package com.axondragonscale.wallmatic.ui.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PhotoAlbum
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Created by Ronak Harkhani on 06/06/24
 */

sealed class Tab(
    val name: String,
    val position: Int,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
) {

    companion object {
        val all = listOf(Home, Albums, Settings)
    }

    data object Home : Tab(
        name = "HOME",
        position = 0,
        activeIcon = Icons.Filled.Home,
        inactiveIcon = Icons.Outlined.Home,
    )

    data object Albums : Tab(
        name = "ALBUMS",
        position = 1,
        activeIcon = Icons.Filled.PhotoAlbum,
        inactiveIcon = Icons.Outlined.PhotoAlbum,
    )

    data object Settings : Tab(
        name = "SETTINGS",
        position = 2,
        activeIcon = Icons.Filled.Settings,
        inactiveIcon = Icons.Outlined.Settings,
    )

}
