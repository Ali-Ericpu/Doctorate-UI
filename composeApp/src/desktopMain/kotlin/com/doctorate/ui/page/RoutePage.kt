package com.doctorate.ui.page

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
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
import com.doctorate.ui.page.emulator.Emulator

enum class Page(val route: String, val desc: String) {
    Emulator("emulator", "模拟器"), Frida("frida", "脚本"), Setting("setting", "设置");

    companion object {
        fun getRoute(route: String?): Page {
            if (route == null) return Emulator
            return Page.entries.find { it.route == route } ?: Emulator
        }
    }
}

@Composable
@Preview
fun RoutePage() {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    var selection = Page.getRoute(backStackEntry.value?.destination?.route)
    MaterialTheme {
        Surface {
            Row(modifier = Modifier.fillMaxSize()) {
                TabColumn(
                    navController = navController,
                    currentPage = selection,
                    modifier = Modifier.fillMaxHeight().weight(1f)
                )
                Box(modifier = Modifier.fillMaxHeight().weight(4f).padding(20.dp)) {
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
                            App()
                        }
                        composable(route = Page.Frida.name) {
                            Text(text = "frida")
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
fun TabColumn(
    navController: NavHostController,
    currentPage: Page,
    modifier: Modifier
) {
    Column(
        modifier = modifier.background(Color(245, 245, 245))
    ) {
        Page.entries.map {
            ActionTab(page = it, selected = it == currentPage, navController = navController)
        }
    }
}

@Composable
fun ActionTab(page: Page, selected: Boolean, navController: NavHostController) {
    NavigationRailItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .height(50.dp)
            .background(if (selected) Color(42, 199, 219) else Color(245, 245, 245)),
        icon = { Text(text = page.desc, color = Color.Black) },
        selected = selected,
        onClick = { navController.navigateSingleTopTo(page.name) },
    )
}