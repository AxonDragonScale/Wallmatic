package com.axondragonscale.wallmatic.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.content.getSystemService
import com.axondragonscale.wallmatic.repository.AppPrefsRepository
import com.axondragonscale.wallmatic.ui.util.toTimestampString
import com.axondragonscale.wallmatic.util.logD
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ronak Harkhani on 22/09/24
 */
@Singleton
class WallmaticScheduler @Inject constructor(
    @ApplicationContext val context: Context,
    private val appPrefsRepository: AppPrefsRepository,
) {

    companion object {
        private const val ALARM_REQUEST_CODE = 13572468
    }

    private val alarmManager = context.getSystemService<AlarmManager>()

    suspend fun scheduleNextUpdate() {
        this.logD("scheduleNextUpdate start")
        val config = appPrefsRepository.configFlow.firstOrNull() ?: run {
            this.logD("Wallpaper update failed. Config is null")
            return
        }

        if (!config.isInit) {
            this.logD("schedule skipped. isInit: ${config.isInit}"); return
        }

        val homeNextUpdate = config.homeConfig.run { lastUpdated + updateInterval }
        val lockNextUpdate = config.lockConfig.run { lastUpdated + updateInterval }
        var nextUpdate = minOf(homeNextUpdate, lockNextUpdate)

        val now = System.currentTimeMillis()
        if (nextUpdate < now) {
            this.logD("nextUpdate < now. nextUpdate: $nextUpdate, now: $now")
            nextUpdate =
                now + minOf(config.homeConfig.updateInterval, config.lockConfig.updateInterval)
        }

        alarmManager ?: run { this.logD("alarmManager is null"); return }

        this.logD("Scheduling next update: ${nextUpdate.toTimestampString()}")
        val pendingIntent = getPendingIntent()
        alarmManager.cancel(pendingIntent)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextUpdate, pendingIntent)
        this.logD("Scheduled update successfully")
    }

    suspend fun cancelScheduledUpdates() {
        this.logD("Cancelling scheduled updates")
        alarmManager?.cancel(getPendingIntent())
    }

    private fun getPendingIntent() =
        PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            WallmaticReceiver.getWallpaperChangeIntent(context),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

//    private fun getPendingIntent() =
//        PendingIntent.getService(
//            context,
//            ALARM_REQUEST_CODE,
//            WallmaticService.getIntent(context),
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
//        )
}
