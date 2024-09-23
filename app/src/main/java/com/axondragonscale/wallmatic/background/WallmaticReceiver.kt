package com.axondragonscale.wallmatic.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * This receiver is intended to reschedule wallpaper changes when:
 * 1. The device is rebooted
 * 2. The time is changed
 * 3. The timezone is changed
 */
@AndroidEntryPoint
class WallmaticReceiver : BroadcastReceiver() {

    @Inject lateinit var scheduler: WallmaticScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (
            intent.action == Intent.ACTION_BOOT_COMPLETED
            || intent.action == Intent.ACTION_TIME_CHANGED
            || intent.action == Intent.ACTION_TIMEZONE_CHANGED
        ) {
            // TODO: Schedule the next wallpaper change
        }
    }

}
