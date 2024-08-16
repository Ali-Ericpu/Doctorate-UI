package com.doctorate.ui.page.character

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import com.doctorate.ui.config.Table
import com.doctorate.ui.entity.Char
import kotlin.math.max

/**
 * ClassName: CharacterDetail
 * Package: com.doctorate.ui.page.character
 * Description:
 * @author Raincc
 * @Create 2024/8/14 13:04
 * @Version 1.0
 */
@Composable
fun CharacterDetail(
    char: Char,
    onValueSave: (Char?) -> Unit,
) {
    var level by remember { mutableStateOf(char.level) }
    var maxLevel by remember { mutableStateOf(Table.getMaxCharLevel(char.charId, char.evolvePhase)) }
    var evolvePhase by remember { mutableStateOf(char.evolvePhase) }
    var favorPercent by remember { mutableStateOf(Table.getFavPointPercent(char.favorPoint)) }
    var potentialRank by remember { mutableStateOf(char.potentialRank) }
    DialogWindow(
        onCloseRequest = { onValueSave(null) },
        resizable = true,
        title = "Char Detail",
        state = DialogState(
            size = DpSize(600.dp, 400.dp),
            position = WindowPosition(Alignment.Center)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn {
                item {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(modifier = Modifier.weight(1f)) {
                            IntRangeSlider(
                                value = evolvePhase,
                                maxValue = Table.getMaxCharEvoLevel(char.charId),
                                description = "EvolvePhase",
                                onValueChange = {
                                    char.evolvePhase = it
                                    maxLevel = Table.getMaxCharLevel(char.charId, char.evolvePhase)
                                    if (char.level > maxLevel) char.level = maxLevel
                                },
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            IntRangeSlider(
                                value = potentialRank,
                                maxValue = 5,
                                description = "Potential",
                                onValueChange = { char.potentialRank = it },
                            )
                        }
                    }
                }
                item {
                    IntRangeSlider(
                        value = level,
                        start = 1,
                        maxValue = maxLevel,
                        description = "Level",
                        onValueChange = { char.level = it },
                    )
                }
                item {
                    IntRangeSlider(
                        value = favorPercent,
                        maxValue = 200,
                        description = "FavorPoint",
                        onValueChange = { char.favorPoint = Table.getRealFavPoint(it) }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                TextButton(
                    onClick = { onValueSave(null) },
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colors.primary)
                ) {
                    Text(text = "Cancel", color = Color.Black)
                }
                TextButton(
                    onClick = {
                        println(char)
                        onValueSave(char)
                    },
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colors.primary)
                ) {
                    Text(text = "Save", color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun IntRangeSlider(
    value: Int,
    start: Int = 0,
    maxValue: Int,
    description: String,
    onValueChange: (Int) -> Unit
) {
    var value by remember { mutableStateOf(value.toFloat()) }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .height(80.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(
                text = description,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = value.toInt().toString(),
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        }
        Slider(
            value = value,
            onValueChange = { value = it.also { onValueChange(it.toInt()) } },
            valueRange = start.toFloat()..maxValue.toFloat(),
            steps = max(maxValue - start - 1, 0),
            colors = SliderDefaults.colors(
                inactiveTickColor = Color.White.copy(alpha = 0f),
                activeTickColor = Color.White.copy(alpha = 0f),
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}