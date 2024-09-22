package com.axondragonscale.wallmatic.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.QueuePlayNext
import androidx.compose.material.icons.outlined.RemoveFromQueue
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.axondragonscale.wallmatic.database.entity.Album
import com.axondragonscale.wallmatic.model.Config
import com.axondragonscale.wallmatic.model.WallpaperConfig
import com.axondragonscale.wallmatic.model.TargetScreen
import com.axondragonscale.wallmatic.model.config
import com.axondragonscale.wallmatic.model.wallpaperConfig
import com.axondragonscale.wallmatic.ui.Route
import com.axondragonscale.wallmatic.ui.bottombar.BOTTOM_BAR_HEIGHT
import com.axondragonscale.wallmatic.ui.bottombar.Tab
import com.axondragonscale.wallmatic.ui.common.SelectAlbumBottomSheet
import com.axondragonscale.wallmatic.ui.common.TabHeader
import com.axondragonscale.wallmatic.ui.common.Wallpaper
import com.axondragonscale.wallmatic.ui.common.WallpaperThumbnail
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme
import com.axondragonscale.wallmatic.ui.util.countSummary
import com.axondragonscale.wallmatic.ui.util.getAspectRatio

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
    var selectAlbumBottomSheetTarget by rememberSaveable {
        mutableStateOf<TargetScreen?>(null)
    }

    Home(
        modifier = modifier,
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                HomeUiEvent.CreateAlbumClick ->
                    navController.navigate(Route.Dashboard(tab = Tab.Albums.position))

                is HomeUiEvent.SelectAlbumClick ->
                    selectAlbumBottomSheetTarget = event.target

                else -> vm.onEvent(event)
            }
        }
    )

    if (selectAlbumBottomSheetTarget != null) {
        SelectAlbumBottomSheet(
            albums = uiState.albums,
            selectedAlbumId = uiState.config.let {
                if (it.hasHomeConfig()) it.homeConfig.albumId else -1
            },
            onSelectAlbum = {
                vm.onEvent(HomeUiEvent.SelectAlbum(it.id, selectAlbumBottomSheetTarget!!))
            },
            onDismiss = { selectAlbumBottomSheetTarget = null },
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
            Text("TODO: Loading")
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
                        onClick = { onEvent(HomeUiEvent.SelectAlbumClick(TargetScreen.Both)) }
                    )

                else -> {
                    HomeScreenCard(
                        modifier = Modifier.padding(vertical = 8.dp),
                        homeConfig = uiState.config.homeConfig,
                        homeAlbum = uiState.homeAlbum!!,
                        onEvent = onEvent,
                    )

                    LockScreenCard(
                        modifier = Modifier.padding(vertical = 8.dp),
                        config = uiState.config,
                        lockAlbum = uiState.lockAlbum,
                        onEvent = onEvent,
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
    onEvent: (HomeUiEvent) -> Unit,
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

        AlbumConfigCard(
            album = homeAlbum,
            autoCycleEnabled = homeConfig.autoCycleEnabled,
            onSelectAlbumClick = { onEvent(HomeUiEvent.SelectAlbumClick(TargetScreen.Home)) },
            onAutoCycleToggle = { onEvent(HomeUiEvent.AutoCycleToggle(it, TargetScreen.Home)) },
        )

        Spacer(modifier = Modifier.height(8.dp))

        WallpaperPreviewCard()
    }
}

@Composable
private fun LockScreenCard(
    modifier: Modifier = Modifier,
    config: Config,
    lockAlbum: Album?,
    onEvent: (HomeUiEvent) -> Unit,
) = Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Lock Screen",
            style = MaterialTheme.typography.labelLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))

        MirrorHomeScreenCard(
            mirrorHomeConfigForLock = config.mirrorHomeConfigForLock,
            onToggle = { onEvent(HomeUiEvent.MirrorHomeConfigForLockToggle) },
        )

        if (!config.mirrorHomeConfigForLock && lockAlbum != null) {
            Spacer(modifier = Modifier.height(8.dp))

            AlbumConfigCard(
                album = lockAlbum,
                autoCycleEnabled = config.lockConfig.autoCycleEnabled,
                onSelectAlbumClick = { onEvent(HomeUiEvent.SelectAlbumClick(TargetScreen.Lock)) },
                onAutoCycleToggle = { onEvent(HomeUiEvent.AutoCycleToggle(it, TargetScreen.Lock)) },
            )

            Spacer(modifier = Modifier.height(8.dp))

            WallpaperPreviewCard()
        }
    }
}

@Composable
private fun MirrorHomeScreenCard(
    modifier: Modifier = Modifier,
    mirrorHomeConfigForLock: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onToggle() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = if (mirrorHomeConfigForLock) "Set different Album for Lock Screen"
            else "Mirror Home Screen to Lock Screen",
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier
                .padding(12.dp)
                .size(28.dp),
            imageVector = if (mirrorHomeConfigForLock) Icons.Outlined.AddCircle else Icons.Outlined.Delete,
            contentDescription = null
        )
    }
}

@Composable
private fun AlbumConfigCard(
    modifier: Modifier = Modifier,
    album: Album,
    autoCycleEnabled: Boolean,
    onSelectAlbumClick: () -> Unit,
    onAutoCycleToggle: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onSelectAlbumClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WallpaperThumbnail(uri = album.coverUri)

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = album.name,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = album.countSummary(),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Switch(
            modifier = Modifier.padding(horizontal = 8.dp),
            checked = autoCycleEnabled,
            onCheckedChange = onAutoCycleToggle
        )
    }
}

@Composable
private fun WallpaperPreviewCard(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // TODO: Navigate to Wallpaper on click
        Wallpaper(
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .aspectRatio(getAspectRatio())
                .border(2.dp, Color.Black, RoundedCornerShape(12.dp)),
            uri = "",
            cornerRadius = 12.dp,
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp)
        ) {
            WallpaperInfo(
                icon = Icons.Outlined.RemoveFromQueue,
                title = "Current Wallpaper",
                time = "Date - Time"
            )

            Spacer(modifier = Modifier.height(12.dp))

            WallpaperInfo(
                icon = Icons.Outlined.QueuePlayNext,
                title = "Next Wallpaper",
                time = "Date - Time"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Change Wallpaper")
            }
        }
    }
}

@Composable
fun WallpaperInfo(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    time: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = icon,
            contentDescription = null,
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = time,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun IntervalSliderCard(modifier: Modifier = Modifier) {

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
//                        lockConfig = wallpaperConfig {
//                            albumId = 1
//                        }
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
