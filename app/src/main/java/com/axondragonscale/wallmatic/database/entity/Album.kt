package com.axondragonscale.wallmatic.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Ronak Harkhani on 26/06/24
 */
@Entity(tableName = "album")
data class Album(
    val name: String,
    val coverUri: String?,
    val wallpaperUris: List<String>,
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
}
