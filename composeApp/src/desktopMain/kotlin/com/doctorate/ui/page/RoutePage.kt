package com.doctorate.ui.page

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.doctorate.ui.App
import com.doctorate.ui.config.LocalAppConfig
import com.doctorate.ui.page.character.Character
import com.doctorate.ui.page.emulator.Emulator
import com.doctorate.ui.page.setting.Setting

enum class Page(val route: String, val desc: String) {
    Emulator("emulator", "EMULATOR"), Nothing("nothing", "NOTHING"), Setting("setting", "SETTING");

    companion object {
        fun getRoute(route: String?): Page {
            if (route == null) return Emulator
            return Page.entries.find { it.name == route } ?: Emulator
        }
    }
}

@Composable
@Preview
fun RoutePage() {
    val config = LocalAppConfig.current.config
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    var currentPage = Page.getRoute(backStackEntry.value?.destination?.route)
    Surface {
        Row(modifier = Modifier.fillMaxSize()) {
            TabNavigationRail(
                currentPage = currentPage,
                onTabSelect = { navController.navigateSingleTopTo(it.name) },
                modifier = Modifier.fillMaxHeight().weight(1f)
            )
            Box(modifier = Modifier.fillMaxHeight().weight(4f).padding(12.dp)) {
                NavHost(
                    navController = navController,
                    startDestination = Page.Emulator.name,
                    enterTransition = {
                        fadeIn(
                            initialAlpha = 0.1f,
                            animationSpec = tween(800)
                        )
                    },
                    exitTransition = {
                        fadeOut(
                            targetAlpha = 0f,
                            animationSpec = tween(800)
                        )
                    },
                    popEnterTransition = {
                        fadeIn(
                            initialAlpha = 0.1f,
                            animationSpec = tween(800)
                        )
                    },
                    popExitTransition = {
                        fadeOut(
                            targetAlpha = 0f,
                            animationSpec = tween(800)
                        )
                    }
                ) {
                    composable(route = Page.Emulator.name) {
                        Emulator()
                    }
                    composable(route = Page.Setting.name) {
                        Setting()
                    }
                    composable(route = Page.Nothing.name) {
                        if (config.extraMode) {
                            Character()
                        } else {
                            App()
                        }
                    }
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().route ?: Page.Emulator.name
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

@Composable
fun TabNavigationRail(
    currentPage: Page,
    onTabSelect: (Page) -> Unit,
    modifier: Modifier
) {
    NavigationRail(
        modifier = modifier
    ) {
        Page.entries.map {
            NavigationRailItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .height(50.dp)
                    .background(if (it == currentPage) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground),
                icon = { Text(text = it.name, color = Color.Black) },
                selected = it == currentPage,
                onClick = { onTabSelect(it) },
                selectedContentColor = Color.Cyan,
                unselectedContentColor = Color.LightGray
            )
        }
    }
}