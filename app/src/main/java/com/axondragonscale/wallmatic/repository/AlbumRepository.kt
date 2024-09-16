package com.axondragonscale.wallmatic.repository

import android.content.Context
import com.axondragonscale.wallmatic.database.dao.AlbumDao
import com.axondragonscale.wallmatic.database.entity.Album
import com.axondragonscale.wallmatic.database.entity.Folder
import com.axondragonscale.wallmatic.database.entity.Wallpaper
import com.axondragonscale.wallmatic.model.FullAlbum
import com.axondragonscale.wallmatic.model.FullFolder
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

    suspend fun getFullAlbum(albumId: Int): FullAlbum {
        val album = albumDao.getAlbum(albumId)
        return FullAlbum(
            id = album.id,
            name = album.name,
            coverUri = album.coverUri,
            folders = getFullFolders(album.folderIds),
            wallpapers = getWallpapers(album.wallpaperIds),
        )
    }

    suspend fun getFullFolders(folders: List<Int>): List<FullFolder> {
        return albumDao
            .getFolders(folders)
            .map { folder ->
                FullFolder(
                    id = folder.id,
                    name = folder.name,
                    coverUri = folder.coverUri,
                    folderUri = folder.folderUri,
                    wallpapers = getWallpapers(folder.wallpapers)
                )
            }
    }

    suspend fun getFullFolder(folderId: Int): FullFolder {
        return getFullFolders(listOf(folderId)).first()
    }

    suspend fun getWallpapers(wallpapers: List<Int>): List<Wallpaper> {
        return albumDao.getWallpapers(wallpapers)
    }

    suspend fun getWallpaper(wallpaperId: Int): Wallpaper {
        return albumDao.getWallpaper(wallpaperId)
    }

    fun getAlbums(): Flow<List<Album>> {
        return albumDao.getAlbums()
    }

    suspend fun updateAlbum(albumId: Int, block: Album.() -> Unit) {
        val album = albumDao.getAlbum(albumId)
        block(album)
        albumDao.upsertAlbum(album)
    }

    suspend fun updateFolder(folderId: Int, block: Folder.() -> Unit) {
        val folder = albumDao.getFolder(folderId)
        block(folder)
        albumDao.upsertFolder(folder)
    }

    suspend fun saveAlbum(album: Album): Int {
        val albumId = albumDao.upsertAlbum(album)
        return albumId.toInt()
    }

    suspend fun saveFolder(folder: Folder): Int {
        val folderId = albumDao.upsertFolder(folder)
        return folderId.toInt()
    }

    suspend fun saveWallpapers(wallpapers: List<Wallpaper>): List<Int> {
        val wallpaperIds = albumDao.upsertWallpapers(wallpapers)
        return wallpaperIds.map { it.toInt() }
    }

    suspend fun deleteAlbum(albumId: Int) {
        albumDao.deleteAlbum(albumId)
    }

}
