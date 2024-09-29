package com.axondragonscale.wallmatic.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.axondragonscale.wallmatic.model.Config
import com.axondragonscale.wallmatic.model.UIMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ronak Harkhani on 19/06/24
 */

object ConfigSerializer: Serializer<Config> {
    override val defaultValue: Config
        get() = Config.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Config = Config.parseFrom(input)
    override suspend fun writeTo(t: Config, output: OutputStream) = t.writeTo(output)
}

private const val CONFIG_STORE = "config_store"
private val Context.configStore: DataStore<Config> by dataStore(
    fileName = CONFIG_STORE,
    serializer = ConfigSerializer,
)

private const val APP_PREFS_STORE = "app_prefs_store"
private val Context.appPrefs: DataStore<Preferences> by preferencesDataStore(APP_PREFS_STORE)

@Singleton
class AppPrefsRepository @Inject constructor(
    @ApplicationContext val context: Context,
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

    private val gridSizeKey = intPreferencesKey("gridSize")
    val gridSizeFlow = context.appPrefs.data
        .map { prefs -> prefs[gridSizeKey] ?: 2 }
        .distinctUntilChanged()

    suspend fun setGridSize(gridSize: Int) {
        context.appPrefs.edit { prefs ->
            prefs[gridSizeKey] = gridSize
        }
    }

    val configFlow = context.configStore.data

    suspend fun setConfig(config: Config) {
        context.configStore.updateData { config }
    }

    // ------------------------ Dev Tools Prefs ------------------------

    private val fastAutoCycleKey = booleanPreferencesKey("fastAutoCycle")
    val fastAutoCycleFlow = context.appPrefs.data
        .map { prefs -> prefs[fastAutoCycleKey] ?: false }
        .distinctUntilChanged()

    suspend fun setFastAutoCycle(fastAutoCycle: Boolean) {
        context.appPrefs.edit { prefs ->
            prefs[fastAutoCycleKey] = fastAutoCycle
        }
    }

}
