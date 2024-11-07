package com.doctorate.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

private val primaryLight = Color(0xFF3399FF)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFF87CEEB)
private val onPrimaryContainerLight = Color(0xFF003143)
private val secondaryLight = Color(0xFF2A6A47)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFF74B58C)
private val onSecondaryContainerLight = Color(0xFF002211)
private val tertiaryLight = Color(0xFF8043A3)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFDFA2FF)
private val onTertiaryContainerLight = Color(0xFF49046C)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFF5FAFF)
private val onBackgroundLight = Color(0xFFC0C0C0)
private val surfaceLight = Color(0xFFFCF9F8)
private val onSurfaceLight = Color(0xFF1B1B1C)

private val primaryDark = Color(0xFFFF6666)
private val onPrimaryDark = Color(0xFF003547)
private val primaryContainerDark = Color(0xFF00B6EB)
private val secondaryDark = Color(0xFF93D5AA)
private val onSecondaryDark = Color(0xFF00391F)
private val secondaryContainerDark = Color(0xFF609F78)
private val errorDark = Color(0xFFBA1A1A)
private val onErrorDark = Color(0xFFFFFFFF)
private val backgroundDark = Color(0xFF0E1417)
private val onBackgroundDark = Color(0xFFDEE3E8)
private val surfaceDark = Color(0xFF131314)
private val onSurfaceDark = Color(0xFFE4E2E2)

val lightTheme = Colors(
    primary = primaryLight,
    primaryVariant = onPrimaryLight,
    secondary = secondaryContainerLight,
    secondaryVariant = secondaryLight,
    background = backgroundLight,
    surface = surfaceLight,
    error = errorLight,
    onPrimary = onPrimaryDark,
    onSecondary = onSecondaryLight,
    onBackground = onBackgroundLight,
    onSurface = onSurfaceLight,
    onError = onErrorLight,
    isLight = true
)

val darkTheme = Colors(
    primary = primaryDark,
    primaryVariant = primaryContainerDark,
    secondary = secondaryDark,
    secondaryVariant = secondaryContainerDark,
    background = backgroundDark,
    surface = surfaceDark,
    error = errorDark,
    onPrimary = onPrimaryDark,
    onSecondary = onSecondaryDark,
    onBackground = onBackgroundDark,
    onSurface = onSurfaceDark,
    onError = onErrorDark,
    isLight = false
)






