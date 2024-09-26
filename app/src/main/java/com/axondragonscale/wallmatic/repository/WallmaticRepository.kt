package com.axondragonscale.wallmatic.repository

import com.axondragonscale.wallmatic.database.dao.WallmaticDao
import com.axondragonscale.wallmatic.database.entity.Album
import com.axondragonscale.wallmatic.database.entity.Folder
import com.axondragonscale.wallmatic.database.entity.Wallpaper
import com.axondragonscale.wallmatic.model.FullAlbum
import com.axondragonscale.wallmatic.model.FullFolder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 26/06/24
 */
class WallmaticRepository @Inject constructor(
    private val wallmaticDao: WallmaticDao,
) {

    suspend fun getFullAlbum(albumId: Int): FullAlbum {
        val album = wallmaticDao.getAlbum(albumId)
        return FullAlbum(
            id = album.id,
            name = album.name,
            coverUri = album.coverUri,
            folders = getFullFolders(album.folderIds),
            wallpapers = getWallpapers(album.wallpaperIds),
        )
    }

    suspend fun getFullFolders(folderIds: List<Int>): List<FullFolder> {
        return wallmaticDao
            .getFolders(folderIds)
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

    suspend fun getWallpapers(wallpaperIds: List<Int>): List<Wallpaper> {
        return wallmaticDao.getWallpapers(wallpaperIds)
    }

    suspend fun getWallpaper(wallpaperId: Int): Wallpaper? {
        return wallmaticDao.getWallpaper(wallpaperId)
    }

    fun getAlbums(): Flow<List<Album>> {
        return wallmaticDao.getAlbums()
    }

    suspend fun updateAlbum(albumId: Int, block: Album.() -> Unit) {
        val album = wallmaticDao.getAlbum(albumId)
        block(album)
        wallmaticDao.upsertAlbum(album)
    }

    suspend fun updateFolder(folderId: Int, block: Folder.() -> Unit) {
        val folder = wallmaticDao.getFolder(folderId)
        block(folder)
        wallmaticDao.upsertFolder(folder)
    }

    suspend fun saveAlbum(album: Album): Int {
        val albumId = wallmaticDao.upsertAlbum(album)
        return albumId.toInt()
    }

    suspend fun saveFolder(folder: Folder): Int {
        val folderId = wallmaticDao.upsertFolder(folder)
        return folderId.toInt()
    }

    suspend fun saveWallpapers(wallpapers: List<Wallpaper>): List<Int> {
        val wallpaperIds = wallmaticDao.upsertWallpapers(wallpapers)
        return wallpaperIds.map { it.toInt() }
    }

    suspend fun deleteAlbum(albumId: Int) {
        wallmaticDao.deleteAlbum(albumId)
    }

}
