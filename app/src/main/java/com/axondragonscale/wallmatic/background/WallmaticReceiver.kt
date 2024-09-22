package com.axondragonscale.wallmatic.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint

/**
 * This receiver is intended to reschedule wallpaper changes when:
 * 1. The device is rebooted
 * 2. The time is changed
 * 3. The timezone is changed
 */
@AndroidEntryPoint
class WallmaticReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

    }

}
