package com.axondragonscale.wallmatic.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.axondragonscale.wallmatic.model.UIMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ronak Harkhani on 19/06/24
 */


private const val APP_PREFS_STORE = "app_prefs"
private val Context.appPrefs: DataStore<Preferences> by preferencesDataStore(APP_PREFS_STORE)

@Singleton
class AppPrefsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val uiModeKey = intPreferencesKey("uiMode")
    val uiModeFlow = context.appPrefs.data
        .map { prefs -> UIMode.fromOrdinal(prefs[uiModeKey] ?: UIMode.AUTO.ordinal) }
        .distinctUntilChanged()

    suspend fun setUiMode(uiMode: UIMode) {
        context.appPrefs.edit { prefs ->
            prefs[uiModeKey] = uiMode.ordinal
        }
    }

    private val dynamicThemeKey = booleanPreferencesKey("dynamicTheme")
    val dynamicThemeFlow = context.appPrefs.data
        .map { prefs -> prefs[dynamicThemeKey] ?: false }
        .distinctUntilChanged()

    suspend fun setDynamicTheme(dynamicTheme: Boolean) {
        context.appPrefs.edit { prefs ->
            prefs[dynamicThemeKey] = dynamicTheme
        }
    }
}
