package com.axondragonscale.wallmatic.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.axondragonscale.wallmatic.database.entity.Album
import com.axondragonscale.wallmatic.database.entity.AlbumWithFolders
import com.axondragonscale.wallmatic.database.entity.Folder
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ronak Harkhani on 26/06/24
 */
@Dao
interface AlbumDao {

    @Query("SELECT * FROM album")
    suspend fun getAlbumsWithFolders(): Flow<List<AlbumWithFolders>>

    @Query("SELECT * FROM album WHERE id = :id")
    suspend fun getAlbumWithFolders(id: Int): AlbumWithFolders?

    @Upsert
    suspend fun upsertAlbum(album: Album)

    @Upsert
    suspend fun upsertFolders(folders: List<Folder>)

    @Query("DELETE FROM album WHERE id = :id")
    suspend fun deleteAlbum(id: Int)

    @Query("DELETE FROM folder WHERE id IN (:ids)")
    suspend fun deleteFolders(ids: List<Int>)

    @Query("DELETE FROM album")
    suspend fun deleteAllAlbums()

    @Query("DELETE FROM folder")
    suspend fun deleteAllFolders()

    @Transaction
    suspend fun deleteAllData() {
        deleteAllAlbums()
        deleteAllFolders()
    }
}
