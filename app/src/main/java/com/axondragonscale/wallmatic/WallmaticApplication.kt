package com.axondragonscale.wallmatic

import android.app.Application
import com.axondragonscale.wallmatic.util.logD
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Ronak Harkhani on 05/06/24
 */
@HiltAndroidApp
class WallmaticApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        this.logD("onCreate")
    }

}
