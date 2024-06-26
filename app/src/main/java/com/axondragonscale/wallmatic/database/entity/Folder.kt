package com.axondragonscale.wallmatic.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Created by Ronak Harkhani on 26/06/24
 */
@Entity(
    tableName = "folder",
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
)
data class Folder(
    val albumId: Int,
    val name: String,
    val coverUri: String?,
    val folderUri: String,
    val wallpaperUris: List<String>,
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
}
