package com.axondragonscale.wallmatic.core

import android.content.Context
import android.app.WallpaperManager
import androidx.core.net.toUri
import com.axondragonscale.wallmatic.background.WallmaticScheduler
import com.axondragonscale.wallmatic.database.entity.Wallpaper
import com.axondragonscale.wallmatic.model.TargetScreen
import com.axondragonscale.wallmatic.model.getAllWallpapers
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import com.axondragonscale.wallmatic.repository.WallmaticRepository
import com.axondragonscale.wallmatic.util.homeConfig
import com.axondragonscale.wallmatic.util.lockConfig
import com.axondragonscale.wallmatic.util.logD
import com.axondragonscale.wallmatic.util.nextUpdate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ronak Harkhani on 23/09/24
 */
@Singleton
class WallpaperUpdater @Inject constructor(
    @ApplicationContext val context: Context,
    private val appPrefsRepository: AppPrefsRepository,
    private val repository: WallmaticRepository,
    private val scheduler: WallmaticScheduler,
) {

    private val manager = WallpaperManager.getInstance(context)

    /**
     * Update one or both wallpaper if the updateInterval has passed since the last update.
     */
    suspend fun updateWallpaper() {
        val config = appPrefsRepository.configFlow.firstOrNull() ?: run {
            this.logD("Wallpaper update failed. Config is null")
            return
        }

        val now = System.currentTimeMillis()
        val updateHomeWallpaper = config.homeConfig.nextUpdate < now
        val updateLockWallpaper = config.lockConfig.nextUpdate < now

        val target = when {
            updateHomeWallpaper && updateLockWallpaper -> TargetScreen.Both
            updateHomeWallpaper && config.mirrorHomeConfigForLock -> TargetScreen.Both
            updateHomeWallpaper -> TargetScreen.Home
            updateLockWallpaper -> TargetScreen.Lock
            else -> null
        }

        if (target != null) updateWallpaper(target)
        else scheduler.scheduleNextUpdate()
    }

    /**
     * Updates the wallpaper for the given target.
     * Note: If mirroring is enabled, this will update both the home and lock wallpapers.
     */
    suspend fun updateWallpaper(target: TargetScreen) {
        this.logD("Wallpaper update start. target: $target")

        val config = appPrefsRepository.configFlow.firstOrNull() ?: run {
            this.logD("Wallpaper update failed. Config is null")
            return
        }

        val timestamp = System.currentTimeMillis()
        val homeAlbumId = config.homeConfig.albumId
        val lockAlbumId = config.lockConfig.albumId

        if (config.mirrorHomeConfigForLock) {
            this.logD("Updating wallpaper. homeAlbumId: $homeAlbumId, lockAlbumId: $lockAlbumId")
            val wallpaper = repository.getFullAlbum(homeAlbumId).getAllWallpapers().random()
            setWallpaper(wallpaper, TargetScreen.Both)
            appPrefsRepository.setConfig(
                config
                    .homeConfig {
                        currentWallpaperId = wallpaper.id
                        lastUpdated = timestamp
                    }
                    .lockConfig {
                        currentWallpaperId = wallpaper.id
                        lastUpdated = timestamp
                    }
            )
        } else {
            if (target.isHome()) {
                this.logD("Updating home wallpaper. homeAlbumId: $homeAlbumId")
                val wallpaper = repository.getFullAlbum(homeAlbumId).getAllWallpapers().random()
                setWallpaper(wallpaper, TargetScreen.Home)
                appPrefsRepository.setConfig(config.homeConfig {
                    currentWallpaperId = wallpaper.id
                    lastUpdated = timestamp
                })
            }
            if (target.isLock()) {
                this.logD("Updating lock wallpaper. lockAlbumId: $lockAlbumId")
                val wallpaper = repository.getFullAlbum(lockAlbumId).getAllWallpapers().random()
                setWallpaper(wallpaper, TargetScreen.Lock)
                appPrefsRepository.setConfig(config.lockConfig {
                    currentWallpaperId = wallpaper.id
                    lastUpdated = timestamp
                })
            }
        }
        this.logD("Wallpaper update complete")

        scheduler.scheduleNextUpdate()
    }

    private fun setWallpaper(wallpaper: Wallpaper, target: TargetScreen) {
        manager.setStream(
            context.contentResolver.openInputStream(wallpaper.uri.toUri()),
            null,
            true,
            target.toFlag(),
        )
    }

    private fun TargetScreen.toFlag() = when (this) {
        TargetScreen.Both -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
        TargetScreen.Home -> WallpaperManager.FLAG_SYSTEM
        TargetScreen.Lock -> WallpaperManager.FLAG_LOCK
    }

}
