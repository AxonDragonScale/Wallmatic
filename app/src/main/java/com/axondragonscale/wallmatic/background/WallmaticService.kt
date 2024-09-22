package com.axondragonscale.wallmatic.background

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallmaticService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_NOT_STICKY
    }

}
