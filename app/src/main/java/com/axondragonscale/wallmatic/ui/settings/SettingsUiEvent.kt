package com.axondragonscale.wallmatic.ui.settings

import com.axondragonscale.wallmatic.model.UIMode

/**
 * Created by Ronak Harkhani on 09/06/24
 */
internal sealed interface SettingsUiEvent {
    data class UIModeUpdate(val uiMode: UIMode): SettingsUiEvent
    data class DynamicThemeToggle(val dynamicTheme: Boolean): SettingsUiEvent
}
