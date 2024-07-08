package com.axondragonscale.wallmatic.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axondragonscale.wallmatic.ui.bottombar.BOTTOM_BAR_HEIGHT
import com.axondragonscale.wallmatic.ui.common.TabHeader
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme

/**
 * Created by Ronak Harkhani on 06/06/24
 */

@Composable
fun Home(
    modifier: Modifier = Modifier,
    vm: HomeVM = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    Home(
        modifier = modifier,
        uiState = uiState,
        onEvent = { vm.onEvent(it) }
    )
}

@Composable
private fun Home(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TabHeader(
            modifier = Modifier.padding(vertical = 16.dp),
            text = "Wallmatic"
        )

        HomeScreenCard(
            modifier = Modifier
        )

        LockScreenCard(
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(BOTTOM_BAR_HEIGHT))
    }
}

@Composable
private fun HomeScreenCard(
    modifier: Modifier = Modifier,
) = Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
) {
    Text(
        modifier = Modifier.padding(top = 8.dp, start = 12.dp),
        text = "Home Screen",
        style = MaterialTheme.typography.labelLarge,
    )
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp))
}

@Composable
private fun LockScreenCard(
    modifier: Modifier = Modifier,
) = Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
) {
    Text(
        modifier = Modifier.padding(top = 8.dp, start = 12.dp),
        text = "Lock Screen",
        style = MaterialTheme.typography.labelLarge,
    )

    ListItem(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp)),
        leadingContent = null,
        headlineContent = {
            Text("Same as Home Screen")
        },
        supportingContent = null,
        trailingContent = {
            Switch(
                checked = true,
                onCheckedChange = {}
            )
        },
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp))
}


@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            val uiState = remember { HomeUiState() }
            Home(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}
