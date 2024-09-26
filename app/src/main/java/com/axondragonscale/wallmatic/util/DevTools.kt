package com.axondragonscale.wallmatic.util

import android.app.ActivityManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ronak Harkhani on 26/09/24
 */
@Singleton
class DevTools @Inject constructor(
    @ApplicationContext val context: Context,
) {
    fun clearData() {
        context.getSystemService<ActivityManager>()?.clearApplicationUserData()
    }
}
