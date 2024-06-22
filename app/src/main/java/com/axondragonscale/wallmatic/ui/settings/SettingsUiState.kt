package com.axondragonscale.wallmatic.ui.settings

import com.axondragonscale.wallmatic.model.UIMode

/**
 * Created by Ronak Harkhani on 09/06/24
 */
data class SettingsUiState(
    val uiMode: UIMode = UIMode.AUTO,
    val dynamicTheme: Boolean = false,
)
