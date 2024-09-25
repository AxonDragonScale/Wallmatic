package com.axondragonscale.wallmatic.background

import android.service.quicksettings.TileService
import com.axondragonscale.wallmatic.core.WallpaperUpdater
import com.axondragonscale.wallmatic.model.TargetScreen
import com.axondragonscale.wallmatic.util.logD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 22/09/24
 */
@AndroidEntryPoint
class LockTileService: TileService() {

    @Inject lateinit var wallpaperUpdater: WallpaperUpdater

    override fun onClick() {
        this.logD("onClick")
        CoroutineScope(Dispatchers.IO).launch {
            wallpaperUpdater.updateWallpaper(TargetScreen.Lock)
        }
    }

}
