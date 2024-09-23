package com.axondragonscale.wallmatic.background

import android.app.AlarmManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ronak Harkhani on 22/09/24
 */
@Singleton
class WallmaticScheduler @Inject constructor(
    @ApplicationContext val context: Context,
) {
    private val alarmManager = context.getSystemService<AlarmManager>()

    fun schedule() {

    }
}
