package com.axondragonscale.wallmatic.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.axondragonscale.wallmatic.ui.albums.Albums
import com.axondragonscale.wallmatic.ui.bottombar.BOTTOM_BAR_HEIGHT
import com.axondragonscale.wallmatic.ui.bottombar.BottomBar
import com.axondragonscale.wallmatic.ui.bottombar.Tab
import com.axondragonscale.wallmatic.ui.home.Home
import com.axondragonscale.wallmatic.ui.settings.Settings
import kotlinx.coroutines.launch

/**
 * Created by Ronak Harkhani on 06/06/24
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WallmaticApp() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val pagerState = rememberPagerState { Tab.all.size }
        val scope = rememberCoroutineScope()

        HorizontalPager(state = pagerState) { position ->
            val bottomPaddingModifier = Modifier.padding(bottom = BOTTOM_BAR_HEIGHT)
            when (position) {
                Tab.Home.position -> Home(bottomPaddingModifier)
                Tab.Albums.position -> Albums(bottomPaddingModifier)
                Tab.Settings.position -> Settings(bottomPaddingModifier)
            }
        }

        BottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            tabs = Tab.all,
            activeTab = Tab.all[pagerState.currentPage],
            onTabChange = {
                scope.launch {
                    pagerState.animateScrollToPage(it.position)
                }
            }
        )
    }
}
