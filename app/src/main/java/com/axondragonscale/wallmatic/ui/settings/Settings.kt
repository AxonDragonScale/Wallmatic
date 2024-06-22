package com.axondragonscale.wallmatic.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axondragonscale.wallmatic.model.UIMode
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme

/**
 * Created by Ronak Harkhani on 09/06/24
 */

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    vm: SettingsVM = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    Settings(
        modifier = modifier,
        uiState = uiState,
        onEvent = { vm.onEvent(it) }
    )
}

@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Header(modifier = Modifier.padding(top = 16.dp))

        ThemeCard(
            modifier = Modifier.padding(top = 24.dp),
            uiMode = uiState.uiMode,
            dynamicTheme = uiState.dynamicTheme,
            onUiModeUpdate = { onEvent(SettingsUiEvent.UIModeUpdate(it)) },
            onDynamicThemeToggle = { onEvent(SettingsUiEvent.DynamicThemeToggle(it)) }
        )
    }
}

@Composable
private fun Header(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "Settings",
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun ThemeCard(
    modifier: Modifier = Modifier,
    uiMode: UIMode,
    dynamicTheme: Boolean,
    onUiModeUpdate: (UIMode) -> Unit,
    onDynamicThemeToggle: (Boolean) -> Unit,
) = Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
) {
    Text(
        modifier = Modifier.padding(top = 8.dp, start = 12.dp),
        text = "Appearance",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onPrimary,
    )

    DarkModeCard(
        modifier = Modifier.padding(top = 4.dp),
        uiMode = uiMode,
        onUiModeUpdate = onUiModeUpdate,
    )

    DynamicThemeCard(
        modifier = Modifier.padding(bottom = 4.dp),
        dynamicTheme = dynamicTheme,
        onDynamicThemeToggle = onDynamicThemeToggle,
    )
}

fun UIMode.getIcon(isActive: Boolean) = when (this) {
    UIMode.LIGHT -> if (isActive) Icons.Filled.LightMode else Icons.Outlined.LightMode
    UIMode.DARK -> if (isActive) Icons.Filled.DarkMode else Icons.Outlined.DarkMode
    UIMode.AUTO -> if (isActive) Icons.Filled.BrightnessAuto else Icons.Outlined.BrightnessAuto
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DarkModeCard(
    modifier: Modifier = Modifier,
    uiMode: UIMode,
    onUiModeUpdate: (UIMode) -> Unit,
) {
    BaseSettingCard(
        modifier = modifier,
        leadingContent = {
            Icon(
                imageVector = Icons.Filled.Brightness6,
                contentDescription = "Dark Mode",
            )
        },
        headlineContent = {
            Text(
                text = "Dark Mode",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        supportingContent = {
            Text(
                text = "Easy on the eyes",
                style = MaterialTheme.typography.labelSmall
            )
        },
        trailingContent = {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.size(width = 128.dp, height = 32.dp),
            ) {
                UIMode.entries.forEach { mode ->
                    val isActive = mode == uiMode
                    SegmentedButton(
                        selected = isActive,
                        onClick = { onUiModeUpdate(mode) },
                        label = {
                            Icon(
                                imageVector = mode.getIcon(isActive),
                                contentDescription = "",
                                tint = if (isActive) MaterialTheme.colorScheme.primary
                                else LocalContentColor.current
                            )
                        },
                        icon = { /* Don't show tick */ },
                        shape = SegmentedButtonDefaults.itemShape(
                            mode.ordinal,
                            UIMode.entries.size
                        ),
                        colors = SegmentedButtonDefaults.colors()
                    )
                }
            }
        },
    )
}

@Composable
fun DynamicThemeCard(
    modifier: Modifier = Modifier,
    dynamicTheme: Boolean,
    onDynamicThemeToggle: (Boolean) -> Unit,
) {
    BaseSettingCard(
        modifier = modifier,
        leadingContent = {
            Icon(
                imageVector = Icons.Filled.Palette,
                contentDescription = "Dynamic Theme",
            )
        },
        headlineContent = {
            Text(
                text = "Dynamic Theme",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        supportingContent = {
            Text(
                text = "Use Material You",
                style = MaterialTheme.typography.labelSmall
            )
        },
        trailingContent = {
            Switch(
                checked = dynamicTheme,
                onCheckedChange = onDynamicThemeToggle,
            )
        }
    )
}

@Composable
private fun BaseSettingCard(
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    ListItem(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp)),
        leadingContent = leadingContent,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = trailingContent,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            leadingIconColor = MaterialTheme.colorScheme.primary,
            headlineColor = MaterialTheme.colorScheme.primary,
            supportingColor = MaterialTheme.colorScheme.secondary,
        )
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsPreview() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            val uiState = remember { SettingsUiState() }
            Settings(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}
