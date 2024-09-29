@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.axondragonscale.wallmatic.ui.folder

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.axondragonscale.wallmatic.ui.Route
import com.axondragonscale.wallmatic.ui.common.LocalAnimatedContentScope
import com.axondragonscale.wallmatic.ui.common.LocalSharedTransitionScope
import com.axondragonscale.wallmatic.ui.common.Wallpaper
import com.axondragonscale.wallmatic.ui.theme.SystemBars
import com.axondragonscale.wallmatic.ui.theme.WallmaticTheme

/**
 * Created by Ronak Harkhani on 17/09/24
 */

@Composable
fun Folder(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    folderId: Int,
) {
    val vm: FolderVM = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    SystemBars(statusBarColor = MaterialTheme.colorScheme.primaryContainer)
    Folder(
        modifier = modifier.statusBarsPadding(),
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is FolderUiEvent.NavigateToWallpaper ->
                    navController.navigate(Route.Wallpaper(event.wallpaperId))

                else -> vm.onEvent(event)
            }
        }
    )
}

@Composable
private fun Folder(
    modifier: Modifier = Modifier,
    uiState: FolderUiState,
    onEvent: (FolderUiEvent) -> Unit,
) {
    if (uiState.folder == null) return
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(folderName = uiState.folder.name)

        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize(),
            columns = StaggeredGridCells.Fixed(uiState.gridSize),
            contentPadding = PaddingValues(8.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(uiState.folder.wallpapers) {
                with(LocalSharedTransitionScope.current!!) {
                    Wallpaper(
                        modifier = Modifier
//                            .sharedElement(
//                                state = this.rememberSharedContentState("wallpaper_${it.id}") ,
//                                animatedVisibilityScope = LocalAnimatedContentScope.current!!
//                            )
                            .sharedBounds(
                                sharedContentState = this.rememberSharedContentState("wallpaper_${it.id}"),
                                animatedVisibilityScope = LocalAnimatedContentScope.current!!
                            ),
                        uri = it.uri,
                        onClick = { onEvent(FolderUiEvent.NavigateToWallpaper(it.id)) }
                    )
                }
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}


@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    folderName: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Folder",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = folderName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoading() {
    WallmaticTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            val uiState = remember { FolderUiState() }
            Folder(
                uiState = uiState,
                onEvent = { }
            )
        }
    }
}
