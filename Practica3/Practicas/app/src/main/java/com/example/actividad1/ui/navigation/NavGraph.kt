package com.example.actividad1.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

// IMPORTA LAS CLASES DE LAS SCREENS (no el paquete)
import com.example.actividad1.ui.screens.browser.BrowserScreen
import com.example.actividad1.ui.screens.imageviewer.ImageViewerScreen
import com.example.actividad1.ui.screens.settings.SettingsScreen
import com.example.actividad1.ui.screens.textviewer.TextViewerScreen

object Destinations {
    const val Browser = "browser"
    const val Settings = "settings"
    const val TextViewer = "textviewer"
    const val ImageViewer = "imageviewer"
}

@Composable
fun AppNavGraph(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Destinations.Browser) {

        composable(Destinations.Browser) {
            BrowserScreen(
                onOpenText = { uri ->
                    nav.navigate("${Destinations.TextViewer}?uri=${Uri.encode(uri)}")
                },
                onOpenImage = { uri ->
                    nav.navigate("${Destinations.ImageViewer}?uri=${Uri.encode(uri)}")
                },
                onOpenSettings = { nav.navigate(Destinations.Settings) }
            )
        }

        composable(
            route = "${Destinations.TextViewer}?uri={uri}",
            arguments = listOf(navArgument("uri") { type = NavType.StringType })
        ) { backStack ->
            val uri = Uri.parse(backStack.arguments?.getString("uri"))
            TextViewerScreen(uri)
        }

        composable(
            route = "${Destinations.ImageViewer}?uri={uri}",
            arguments = listOf(navArgument("uri") { type = NavType.StringType })
        ) { backStack ->
            val uri = Uri.parse(backStack.arguments?.getString("uri"))
            ImageViewerScreen(uri)
        }

        composable(Destinations.Settings) {
            SettingsScreen()
        }
    }
}
