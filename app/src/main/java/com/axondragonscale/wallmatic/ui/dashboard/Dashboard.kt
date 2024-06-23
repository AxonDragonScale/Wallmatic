package com.axondragonscale.wallmatic.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.axondragonscale.wallmatic.ui.albums.Albums
import com.axondragonscale.wallmatic.ui.bottombar.BottomBar
import com.axondragonscale.wallmatic.ui.bottombar.Tab
import com.axondragonscale.wallmatic.ui.bottombar.Tabs
import com.axondragonscale.wallmatic.ui.home.Home
import com.axondragonscale.wallmatic.ui.settings.Settings
import kotlinx.coroutines.launch

/**
 * Created by Ronak Harkhani on 23/06/24
 */


@Composable
fun Dashboard(
    modifier: Modifier = Modifier,
    navController: NavController,
    tab: Tab,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(
            initialPage = tab.position,
            pageCount = { Tabs.size }
        )
        val scope = rememberCoroutineScope()

        HorizontalPager(state = pagerState) { position ->
            when (position) {
                Tab.Home.position -> Home()
                Tab.Albums.position -> Albums(navController = navController)
                Tab.Settings.position -> Settings()
            }
        }

        BottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            tabs = Tabs,
            activeTab = Tabs[pagerState.currentPage],
            onTabChange = {
                scope.launch {
                    pagerState.animateScrollToPage(it.position)
                }
            }
        )
    }
}
