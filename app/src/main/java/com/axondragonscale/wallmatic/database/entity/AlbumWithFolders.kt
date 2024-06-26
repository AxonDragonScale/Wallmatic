package com.axondragonscale.wallmatic.database.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Ronak Harkhani on 26/06/24
 */
data class AlbumWithFolders(
    @Embedded
    val album: Album,

    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val folders: List<Folder>
)
