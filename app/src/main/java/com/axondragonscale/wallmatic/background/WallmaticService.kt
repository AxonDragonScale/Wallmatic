package com.axondragonscale.wallmatic.background

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.axondragonscale.wallmatic.util.logD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallmaticService : Service() {

    // TODO: Is this needed?
    enum class Action {

    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        this.logD("onCreate")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        this.logD("onStartCommand")

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        this.logD("onDestroy")
        super.onDestroy()
    }

}
