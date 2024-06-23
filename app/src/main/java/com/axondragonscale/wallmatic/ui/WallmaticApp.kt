package com.axondragonscale.wallmatic.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.axondragonscale.wallmatic.ui.bottombar.Tabs
import com.axondragonscale.wallmatic.ui.dashboard.Dashboard

/**
 * Created by Ronak Harkhani on 06/06/24
 */

@Composable
fun WallmaticApp() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .navigationBarsPadding()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background),
        navController = navController,
        startDestination = Route.Dashboard(),
    ) {
        // TODO: Deeplinking to tabs
        // TODO: Create composable/NavGraph in the screen's package itself
        composable<Route.Dashboard> { backStackEntry ->
            val dashboard: Route.Dashboard = backStackEntry.toRoute()
            Dashboard(tab = Tabs[dashboard.tab])
        }
    }
}
