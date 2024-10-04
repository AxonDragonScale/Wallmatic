package com.axondragonscale.wallmatic.background

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.axondragonscale.wallmatic.core.SyncManager
import com.axondragonscale.wallmatic.core.WallpaperUpdater
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import com.axondragonscale.wallmatic.util.logD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WallmaticService : Service() {

    companion object {
        private const val TAG = "WallmaticService"

        fun getIntent(context: Context): Intent =
            Intent(context, WallmaticService::class.java)
    }

    @Inject lateinit var wallpaperUpdater: WallpaperUpdater
    @Inject lateinit var appPrefsRepository: AppPrefsRepository
    @Inject lateinit var syncManager: SyncManager

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        this.logD("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        this.logD("onStartCommand")

        CoroutineScope(Dispatchers.IO).launch {
            syncManager.syncAlbums()
            wallpaperUpdater.updateWallpaper()

            TAG.logD("Stopping service")
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        this.logD("onDestroy")
        super.onDestroy()
    }

}
