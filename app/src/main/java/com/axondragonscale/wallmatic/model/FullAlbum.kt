package com.axondragonscale.wallmatic.model

import com.axondragonscale.wallmatic.database.entity.Wallpaper

/**
 * Created by Ronak Harkhani on 01/07/24
 */
data class FullAlbum(
    val id: Int,
    val name: String,
    val coverUri: String?,
    val folders: List<FullFolder>,
    val wallpapers: List<Wallpaper>,
)


