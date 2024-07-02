package com.axondragonscale.wallmatic.repository

import android.content.Context
import com.axondragonscale.wallmatic.database.dao.AlbumDao
import com.axondragonscale.wallmatic.database.entity.Album
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 26/06/24
 */
class AlbumRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val albumDao: AlbumDao,
) {

    suspend fun createAlbum(albumName: String): Int {
        val albumId = albumDao.upsertAlbum(Album(name = albumName))
        return albumId.toInt()
    }

    suspend fun getAlbum(albumId: Int): Album {
        return albumDao.getAlbum(albumId)
    }

    fun getAlbums() = albumDao.getAlbums()

}
