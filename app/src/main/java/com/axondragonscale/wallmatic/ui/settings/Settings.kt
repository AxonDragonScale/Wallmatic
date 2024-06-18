package com.axondragonscale.wallmatic.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
fun Settings(
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
        ThemeCard(modifier = Modifier.padding(top = 24.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeCard(modifier: Modifier = Modifier) = Card(
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

    ListItem(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp)),
        leadingContent = {
            Icon(
                modifier = Modifier,
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
            val selected = 1
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .width(128.dp)
                    .height(32.dp)
            ) {
                (0 until 3).forEach { i ->
                    SegmentedButton(
                        selected = i == selected,
                        onClick = {},
                        shape = SegmentedButtonDefaults.itemShape(i, 3),
                        label = {
                            Icon(
                                imageVector = Icons.Filled.Brightness6,
                                contentDescription = "",
                            )
                        },
                        icon = {}
                    )
                }
            }
        },

        )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmptyJokeListPreview() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            val uiState = remember { SettingsUiState(1, true) }
            Settings(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}
