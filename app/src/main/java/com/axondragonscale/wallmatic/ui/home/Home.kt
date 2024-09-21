package com.axondragonscale.wallmatic.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.axondragonscale.wallmatic.database.entity.Album
import com.axondragonscale.wallmatic.model.WallpaperConfig
import com.axondragonscale.wallmatic.model.config
import com.axondragonscale.wallmatic.model.wallpaperConfig
import com.axondragonscale.wallmatic.ui.Route
import com.axondragonscale.wallmatic.ui.bottombar.BOTTOM_BAR_HEIGHT
import com.axondragonscale.wallmatic.ui.bottombar.Tab
import com.axondragonscale.wallmatic.ui.common.SelectAlbumBottomSheet
import com.axondragonscale.wallmatic.ui.common.TabHeader
import com.axondragonscale.wallmatic.ui.common.WallpaperThumbnail
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme
import com.axondragonscale.wallmatic.ui.util.countSummary

/**
 * Created by Ronak Harkhani on 06/06/24
 */

@Composable
fun Home(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val vm: HomeVM = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    var showSelectAlbumBottomSheet by rememberSaveable { mutableStateOf(false) }

    Home(
        modifier = modifier,
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                HomeUiEvent.CreateAlbumClick ->
                    navController.navigate(Route.Dashboard(tab = Tab.Albums.position))

                HomeUiEvent.SelectAlbumClick ->
                    showSelectAlbumBottomSheet = true

                else -> vm.onEvent(event)
            }
        }
    )

    if (showSelectAlbumBottomSheet) {
        SelectAlbumBottomSheet(
            albums = uiState.albums,
            selectedAlbumId = uiState.config.let {
                if (it.hasHomeConfig()) it.homeConfig.albumId else -1
            },
            onSelectAlbum = { vm.onEvent(HomeUiEvent.SelectAlbum(it.id)) },
            onDismiss = { showSelectAlbumBottomSheet = false },
        )
    }
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
            .verticalScroll(rememberScrollState()),
    ) {
        TabHeader(
            modifier = Modifier.padding(vertical = 16.dp),
            text = "Wallmatic"
        )

        if (uiState.isLoading) {

        } else {
            when {
                uiState.albums.isEmpty() ->
                    TextAndButton(
                        text = """
                            Auto cycle wallpapers.
                            Choose individual images
                            Or entire folders.
                        """.trimIndent(),
                        buttonText = "CREATE ALBUM",
                        buttonIcon = Icons.Outlined.AddCircle,
                        onClick = { onEvent(HomeUiEvent.CreateAlbumClick) }
                    )

                !uiState.config.hasHomeConfig() && !uiState.config.hasLockConfig() ->
                    TextAndButton(
                        text = """
                            Auto cycle wallpapers.
                            Choose individual images
                            Or entire folders.
                        """.trimIndent(),
                        buttonText = "SELECT ALBUM",
                        buttonIcon = Icons.Outlined.CheckCircle,
                        onClick = { onEvent(HomeUiEvent.SelectAlbumClick) }
                    )

                else -> {
                    HomeScreenCard(
                        modifier = Modifier.padding(vertical = 8.dp),
                        homeConfig = uiState.config.homeConfig,
                        homeAlbum = uiState.homeAlbum!!
                    )

                    LockScreenCard(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }


        }

        Spacer(modifier = Modifier.height(BOTTOM_BAR_HEIGHT))
    }
}

@Composable
private fun TextAndButton(
    modifier: Modifier = Modifier,
    text: String,
    buttonText: String,
    buttonIcon: ImageVector,
    onClick: () -> Unit,
) = Card(modifier = modifier) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onClick() }
                .height(48.dp)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold,
            )

            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = buttonIcon,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun HomeScreenCard(
    modifier: Modifier = Modifier,
    homeConfig: WallpaperConfig,
    homeAlbum: Album,
) = Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Home Screen",
            style = MaterialTheme.typography.labelLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WallpaperThumbnail(uri = homeAlbum.coverUri)

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = homeAlbum.name,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = homeAlbum.countSummary(),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = true,
                onCheckedChange = {}
            )
        }
    }
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
            .padding(horizontal = 8.dp, vertical = 8.dp)
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    )
}


@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCreateAlbum() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            val uiState = remember { HomeUiState(isLoading = false) }
            Home(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewSelectAlbum() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            val uiState = remember {
                HomeUiState(isLoading = false, albums = listOf(Album(name = "a")))
            }
            Home(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            val uiState = remember {
                HomeUiState(
                    isLoading = false,
                    albums = listOf(Album(name = "Album 1").apply { id = 1 }),
                    config = config {
                        mirrorHomeConfigForLock = true
                        homeConfig = wallpaperConfig {
                            albumId = 1
                        }
                    }
                )
            }
            Home(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}
