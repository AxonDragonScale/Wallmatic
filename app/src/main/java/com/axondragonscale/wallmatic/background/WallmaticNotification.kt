package com.axondragonscale.wallmatic.background

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.axondragonscale.wallmatic.R
import com.axondragonscale.wallmatic.util.logD

/**
 * Created by Ronak Harkhani on 27/09/24
 */
object WallmaticNotification {

    private const val CHANNEL_ID = "wallmatic_wallpaper_updater"
    private const val CHANNEL_NAME = "Wallpaper Update"
    private const val CHANNEL_DESC = "Receive notifications when wallpaper are updated"

    const val ID = 9562
    private const val TITLE = "Wallpaper Update"
    private const val TEXT = "Updating your wallpaper"

    fun getUpdateNotification(context: Context): Notification {
        this.logD("Creating Wallpaper Update Notification Channel")
        val channel = NotificationChannelCompat
            .Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_MAX)
            .setName(CHANNEL_NAME)
            .setDescription(CHANNEL_DESC)
            .setSound(null, null)
            .build()

        NotificationManagerCompat.from(context).createNotificationChannel(channel)

        this.logD("Creating Wallpaper Update Notification")
        val notification = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSilent(true)
            .setContentTitle(TITLE)
            .setContentText(TEXT)
            .build()
        return notification
    }

}
