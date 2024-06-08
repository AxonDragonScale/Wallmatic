package com.axondragonscale.wallmatic.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.axondragonscale.wallmatic.ui.bottombar.BottomBar
import com.axondragonscale.wallmatic.ui.bottombar.Tab
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        val pagerState = rememberPagerState { Tab.all.size }
        val scope = rememberCoroutineScope()

        HorizontalPager(state = pagerState) { position ->
            when (position) {
                Tab.Home.position -> Test(text = "1")
                Tab.Albums.position -> Test(text = "2")
                Tab.Settings.position -> Test(text = "3")
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

@Composable
fun Test(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, color = Color.White)
    }
}
