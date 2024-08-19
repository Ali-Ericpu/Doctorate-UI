package com.doctorate.ui.page.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.doctorate.ui.entity.Skill
import com.doctorate.ui.page.setting.EditSwitch
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImagePainter
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.cancel
import doctorateui.composeapp.generated.resources.character_locked_skill
import doctorateui.composeapp.generated.resources.character_skill_selected
import doctorateui.composeapp.generated.resources.character_special_skill_0
import doctorateui.composeapp.generated.resources.character_special_skill_1
import doctorateui.composeapp.generated.resources.character_special_skill_2
import doctorateui.composeapp.generated.resources.character_special_skill_3
import doctorateui.composeapp.generated.resources.elite_phase
import doctorateui.composeapp.generated.resources.fav_pt
import doctorateui.composeapp.generated.resources.level
import doctorateui.composeapp.generated.resources.potential
import doctorateui.composeapp.generated.resources.save
import doctorateui.composeapp.generated.resources.skill_level
import doctorateui.composeapp.generated.resources.star_mark
import okio.Path.Companion.toPath
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt

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
    var level by remember { mutableStateOf(char.level.toFloat()) }
    var maxLevel by remember { mutableStateOf(Table.getMaxCharLevel(char.charId, char.evolvePhase)) }
    var evolvePhase by remember { mutableStateOf(char.evolvePhase.toFloat()) }
    var skillLvl by remember { mutableStateOf(char.mainSkillLvl.toFloat()) }
    var maxSkillLvl by remember { mutableStateOf(7) }
    var defaultSkill by remember { mutableStateOf(char.defaultSkillIndex) }
    var favorPercent by remember { mutableStateOf(Table.getFavPointPercent(char.favorPoint).toFloat()) }
    var potentialRank by remember { mutableStateOf(char.potentialRank.toFloat()) }
    var starMark by remember { mutableStateOf(char.starMark == 1) }
    var skills = mutableStateListOf<Skill>()
    char.skills.forEach { skills.add(it.copy()) }
    DialogWindow(
        onCloseRequest = { onValueSave(null) }, resizable = true, title = char.name!!, state = DialogState(
            size = DpSize(600.dp, 800.dp), position = WindowPosition(Alignment.Center)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
        ) {
            Column {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(modifier = Modifier.weight(1f)) {
                        IntRangeSlider(
                            value = evolvePhase,
                            maxValue = Table.getMaxCharEvoLevel(char.charId),
                            description = stringResource(Res.string.elite_phase),
                            onValueChange = {
                                maxLevel = Table.getMaxCharLevel(char.charId, it.toInt())
                                if (level > maxLevel) level = maxLevel.toFloat()
                                skills.forEachIndexed { index, skill ->
                                    if (index <= evolvePhase.roundToInt()) {
                                        skill.unlock = 1
                                        defaultSkill = index
                                    } else {
                                        skill.unlock = 0
                                    }
                                }
                                maxSkillLvl = if (it > 0) 7 else 4
                                if (skillLvl > maxSkillLvl) skillLvl = maxSkillLvl.toFloat()
                                evolvePhase = it
                            },
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        IntRangeSlider(
                            value = potentialRank,
                            maxValue = 5,
                            description = stringResource(Res.string.potential),
                            onValueChange = { potentialRank = it },
                        )
                    }
                }
                IntRangeSlider(
                    value = level,
                    start = 1,
                    maxValue = maxLevel,
                    description = stringResource(Res.string.level),
                    onValueChange = { level = it },
                )
                IntRangeSlider(
                    value = favorPercent,
                    maxValue = 200,
                    description = stringResource(Res.string.fav_pt),
                    onValueChange = { favorPercent = it },
                )
                if (skills.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                    ) {
                        Box {
                            IntRangeSlider(
                                value = skillLvl,
                                start = 1,
                                maxValue = maxSkillLvl,
                                description = stringResource(Res.string.skill_level),
                                onValueChange = { skillLvl = it },
                            )
                        }
                        LazyRow(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(8.dp).height(108.dp)
                        ) {
                            items(skills.size) { index ->
                                SkillDetail(evolvePhase.toInt(),
                                    skillLvl.roundToInt(),
                                    skills[index],
                                    defaultSkill == index,
                                    onSelectedChange = { if (skills[index].unlock == 1) defaultSkill = index },
                                    onSpecialLevelChange = { skills[index].specializeLevel = it })
                            }
                        }
                    }
                }
                EditSwitch(
                    content = stringResource(Res.string.star_mark),
                    state = starMark,
                    onCheckedChange = { starMark = it },
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth().padding(8.dp).height(60.dp)
            ) {
                TextButton(
                    onClick = { onValueSave(null) },
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colors.primary)
                ) {
                    Text(text = stringResource(Res.string.cancel), color = Color.Black)
                }
                TextButton(
                    onClick = {
                        char.evolvePhase = evolvePhase.roundToInt()
                        char.level = level.roundToInt()
                        char.potentialRank = potentialRank.roundToInt()
                        char.favorPoint = Table.getRealFavPoint(favorPercent.roundToInt())
                        char.mainSkillLvl = skillLvl.roundToInt()
                        char.defaultSkillIndex = defaultSkill
                        char.skills = skills
                        char.starMark = if (starMark) 1 else 0
                        char.exp = 0
                        if (char.evolvePhase < 2) {
                            char.equip.values.forEach {
                                it.hide = 1
                                it.locked = 1
                                it.level = 0
                            }
                            if (char.mainSkillLvl < 7) {
                                skills.forEach { it.specializeLevel = 0 }
                            }
                        }
                        onValueSave(char)
                    }, modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colors.primary)
                ) {
                    Text(text = stringResource(Res.string.save), color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun IntRangeSlider(
    value: Float, start: Int = 0, maxValue: Int, description: String, onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray).height(80.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(
                text = description, fontSize = 24.sp, color = Color.Black, modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = value.roundToInt().toString(),
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        }
        Slider(
            value = value,
            onValueChange = { onValueChange(it) },
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

@Composable
fun SkillDetail(
    evoPhase: Int,
    skillLevel: Int,
    skill: Skill,
    select: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    onSpecialLevelChange: (Int) -> Unit,
) {
    var specializeLevel by remember { mutableStateOf(skill.specializeLevel) }
    Row(modifier = Modifier.padding(8.dp)) {
        Box(modifier = Modifier.clickable { onSelectedChange(true) }) {
            val skillPainter = if (skill.unlock == 0) {
                painterResource(Res.drawable.character_locked_skill)
            } else {
                val skillIcon = File("data/skill/skill_icon_${skill.skillId}.png").absolutePath
                rememberImagePainter(ImageRequest(data = skillIcon.toPath()))
            }
            Image(
                painter = skillPainter, contentDescription = null, modifier = Modifier.fillMaxSize().border(
                    width = 4.dp,
                    color = MaterialTheme.colors.primary.copy(
                        alpha = if (select) 1f else 0f,
                    ),
                )
            )
            if (evoPhase >= 2 && skillLevel == 7) {
                val specialLevelPainter = when (specializeLevel) {
                    1 -> Res.drawable.character_special_skill_1
                    2 -> Res.drawable.character_special_skill_2
                    3 -> Res.drawable.character_special_skill_3
                    else -> Res.drawable.character_special_skill_0
                }
                Image(
                    painter = painterResource(specialLevelPainter),
                    contentDescription = null,
                    modifier = Modifier.align(alignment = Alignment.TopStart)
                )
            }
            if (select) {
                Image(
                    painter = painterResource(Res.drawable.character_skill_selected),
                    contentDescription = null,
                    modifier = Modifier.align(alignment = Alignment.TopEnd)
                )
            }
        }
        if (evoPhase >= 2 && skillLevel == 7) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.padding(8.dp)
            ) {
                IconButton(
                    onClick = { if (specializeLevel < 3) onSpecialLevelChange(++specializeLevel) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "add"
                    )
                }
                IconButton(
                    onClick = { if (specializeLevel > 0) onSpecialLevelChange(--specializeLevel) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "min"
                    )
                }
            }
        }
    }
}