package com.axondragonscale.wallmatic.background

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.axondragonscale.wallmatic.core.WallpaperUpdater
import com.axondragonscale.wallmatic.model.TargetScreen
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import com.axondragonscale.wallmatic.util.logD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WallmaticService : Service() {

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, WallmaticService::class.java)
    }

    @Inject lateinit var wallpaperUpdater: WallpaperUpdater
    @Inject lateinit var appPrefsRepository: AppPrefsRepository

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        this.logD("onCreate")

        // TODO: Is foreground service required?
        // TODO: Foreground Service Type is SpecialUse. Check.
        val foregroundNotification = WallmaticNotification.getUpdateNotification(this)
        startForeground(WallmaticNotification.ID, foregroundNotification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        this.logD("onStartCommand")

        CoroutineScope(Dispatchers.IO).launch {
            val config = appPrefsRepository.configFlow.first()
            val now = System.currentTimeMillis()

            val updateHomeWallpaper = config.homeConfig.run { lastUpdated + updateInterval } > now
            val updateLockWallpaper = config.lockConfig.run { lastUpdated + updateInterval } > now

            val target = when {
                updateHomeWallpaper && updateLockWallpaper -> TargetScreen.Both
                updateHomeWallpaper -> TargetScreen.Home
                updateLockWallpaper -> TargetScreen.Lock
                else -> null
            }

            if (target != null)
                wallpaperUpdater.updateWallpaper(target)

            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        this.logD("onDestroy")
        super.onDestroy()
    }

}
