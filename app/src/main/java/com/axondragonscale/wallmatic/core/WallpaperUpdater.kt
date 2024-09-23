package com.axondragonscale.wallmatic.core

import android.content.Context
import android.app.WallpaperManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ronak Harkhani on 23/09/24
 */
@Singleton
class WallpaperUpdater @Inject constructor(
    @ApplicationContext val context: Context,
) {

    private val manager = WallpaperManager.getInstance(context)

    fun updateWallpaper() {

    }

}
