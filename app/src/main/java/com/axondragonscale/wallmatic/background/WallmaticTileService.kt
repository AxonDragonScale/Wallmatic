package com.axondragonscale.wallmatic.background

import android.service.quicksettings.TileService
import com.axondragonscale.wallmatic.util.logD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 22/09/24
 */
@AndroidEntryPoint
class WallmaticTileService: TileService() {

    @Inject lateinit var scheduler: WallmaticScheduler

    override fun onClick() {
        this.logD("onClick")
        // TODO: Change wallpaper
    }

}
