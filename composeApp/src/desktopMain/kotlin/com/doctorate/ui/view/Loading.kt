package com.doctorate.ui.view

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
fun Loading() {
    val rotate by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

//    Icon(painter = painterResource("icon_loading.png"), null, modifier = Modifier.size(20.dp).rotate(rotate))
}