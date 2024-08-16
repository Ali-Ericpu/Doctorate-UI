package com.doctorate.ui.page.character

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.doctorate.ui.config.LocalAppConfig
import com.doctorate.ui.config.Table
import com.doctorate.ui.entity.Char
import com.doctorate.ui.entity.Profession
import com.doctorate.ui.network.datasource.CharacterDataSource
import com.doctorate.ui.view.LocalAppToaster
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImagePainter
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.character_charbg_1
import doctorateui.composeapp.generated.resources.character_charbg_2
import doctorateui.composeapp.generated.resources.character_charbg_3
import doctorateui.composeapp.generated.resources.character_charbg_4
import doctorateui.composeapp.generated.resources.character_charbg_5
import doctorateui.composeapp.generated.resources.character_charbg_6
import doctorateui.composeapp.generated.resources.character_elite_0
import doctorateui.composeapp.generated.resources.character_elite_1
import doctorateui.composeapp.generated.resources.character_elite_2
import doctorateui.composeapp.generated.resources.character_elite_bg
import doctorateui.composeapp.generated.resources.character_empty_skill
import doctorateui.composeapp.generated.resources.character_level_bg
import doctorateui.composeapp.generated.resources.character_lowerhub1
import doctorateui.composeapp.generated.resources.character_lowerhub2
import doctorateui.composeapp.generated.resources.character_lowerhub3
import doctorateui.composeapp.generated.resources.character_lowerhub4
import doctorateui.composeapp.generated.resources.character_lowerhub5
import doctorateui.composeapp.generated.resources.character_lowerhub6
import doctorateui.composeapp.generated.resources.character_potential_1
import doctorateui.composeapp.generated.resources.character_potential_2
import doctorateui.composeapp.generated.resources.character_potential_3
import doctorateui.composeapp.generated.resources.character_potential_4
import doctorateui.composeapp.generated.resources.character_potential_5
import doctorateui.composeapp.generated.resources.character_rartylight_1
import doctorateui.composeapp.generated.resources.character_rartylight_2
import doctorateui.composeapp.generated.resources.character_rartylight_3
import doctorateui.composeapp.generated.resources.character_rartylight_4
import doctorateui.composeapp.generated.resources.character_rartylight_5
import doctorateui.composeapp.generated.resources.character_rartylight_6
import doctorateui.composeapp.generated.resources.character_star_1
import doctorateui.composeapp.generated.resources.character_star_2
import doctorateui.composeapp.generated.resources.character_star_3
import doctorateui.composeapp.generated.resources.character_star_4
import doctorateui.composeapp.generated.resources.character_star_5
import doctorateui.composeapp.generated.resources.character_star_6
import doctorateui.composeapp.generated.resources.character_star_mark
import doctorateui.composeapp.generated.resources.character_star_mark_edit
import doctorateui.composeapp.generated.resources.character_upperhub_1
import doctorateui.composeapp.generated.resources.character_upperhub_2
import doctorateui.composeapp.generated.resources.character_upperhub_3
import doctorateui.composeapp.generated.resources.character_upperhub_4
import doctorateui.composeapp.generated.resources.character_upperhub_5
import doctorateui.composeapp.generated.resources.character_upperhub_6
import doctorateui.composeapp.generated.resources.character_upperhub_shadow
import doctorateui.composeapp.generated.resources.equip_bg
import doctorateui.composeapp.generated.resources.icon_profession_caster
import doctorateui.composeapp.generated.resources.icon_profession_medic
import doctorateui.composeapp.generated.resources.icon_profession_pioneer
import doctorateui.composeapp.generated.resources.icon_profession_sniper
import doctorateui.composeapp.generated.resources.icon_profession_special
import doctorateui.composeapp.generated.resources.icon_profession_support
import doctorateui.composeapp.generated.resources.icon_profession_tank
import doctorateui.composeapp.generated.resources.icon_profession_warrior
import doctorateui.composeapp.generated.resources.profession_select
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath
import org.jetbrains.compose.resources.painterResource
import java.io.File

/**
 * ClassName: Character
 * Package: com.doctorate.ui.page.character
 * Description:
 * @author Raincc
 * @Create 2024/8/13 14:37
 * @Version 1.0
 */

@Composable
fun Character(
    viewModel: CharacterViewModel = viewModel { CharacterViewModel() }
) {
    val config = LocalAppConfig.current.config
    val toaster = LocalAppToaster.current
    val currentProfession by viewModel.profession.collectAsState()
    val selectProfession by viewModel.isSelect.collectAsState()
    val charList = viewModel.syncCharList()

    val professions = Profession.entries.toList()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(GridCells.FixedSize(138.dp)) {
            items(charList) {
                CharacterCard(it) { viewModel.changeCharData(it) }
            }
        }
        Box(modifier = Modifier.width(66.dp).align(alignment = Alignment.TopEnd).padding(top = 24.dp)) {
            if (selectProfession) {
                LazyColumn(userScrollEnabled = false, modifier = Modifier.fillMaxHeight()) {
                    item {
                        IconButton(
                            onClick = {
                                if (currentProfession == "ALL") {
                                    viewModel.changeSelectState(false)
                                } else {
                                    viewModel.selectProfession("ALL")
                                }
                            },
                            modifier = Modifier.height(66.dp)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.7f))
                                .align(alignment = Alignment.Center)
                        ) {
                            Text(
                                text = if (currentProfession == "ALL") "BACK" else "ALL",
                                textAlign = TextAlign.Center,
                                style = TextStyle(color = MaterialTheme.colors.primary)
                            )
                        }
                    }
                    items(professions) {
                        Box {
                            Image(
                                painter = painterResource(it.icon),
                                contentDescription = it.name,
                                modifier = Modifier.alpha(0.7f)
                                    .clickable(onClick = { viewModel.selectProfession(it.name) })
                            )
                            if (currentProfession == it.name) {
                                Image(
                                    painter = painterResource(Res.drawable.profession_select),
                                    contentDescription = "select",
                                    alignment = Alignment.CenterEnd,
                                    modifier = Modifier.fillMaxWidth().height(66.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                Button(onClick = { viewModel.changeSelectState(true) }) {
                    Text(text = "All")
                }
            }
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data = CharacterDataSource.syncCharacter(config.adminKey, config.uid)
                    viewModel.initCharData(data)
                } catch (e: Exception) {
                    toaster.toastFailure(e.message.toString())
                }
            }
        }) {
            Text(text = "get")
        }
    }
}

@Composable
fun CharacterCard(
    char: Char,
    onCharChange: (char: Char) -> Unit
) {
    var showDetail by remember { mutableStateOf(false) }

    val evolvePhasePainter = when (char.evolvePhase) {
        0 -> Res.drawable.character_elite_0
        1 -> Res.drawable.character_elite_1
        2 -> Res.drawable.character_elite_2
        else -> Res.drawable.character_elite_0
    }
    var rarityPainter = Res.drawable.character_star_1
    var charBgPainter = Res.drawable.character_charbg_1
    var upperHubPainter = Res.drawable.character_upperhub_1
    var lowerHubPainter = Res.drawable.character_lowerhub1
    var rarityLightPainter = Res.drawable.character_rartylight_1
    var professionPainter = when (char.profession!!) {
        "PIONEER" -> Res.drawable.icon_profession_pioneer
        "WARRIOR" -> Res.drawable.icon_profession_warrior
        "SNIPER" -> Res.drawable.icon_profession_sniper
        "SPECIAL" -> Res.drawable.icon_profession_special
        "TANK" -> Res.drawable.icon_profession_tank
        "CASTER" -> Res.drawable.icon_profession_caster
        "MEDIC" -> Res.drawable.icon_profession_medic
        "SUPPORT" -> Res.drawable.icon_profession_support
        else -> throw IllegalArgumentException("unknown character profession :${char.profession}")
    }
    when (char.rarity!!) {
        2 -> {
            rarityPainter = Res.drawable.character_star_2
            charBgPainter = Res.drawable.character_charbg_2
            upperHubPainter = Res.drawable.character_upperhub_2
            lowerHubPainter = Res.drawable.character_lowerhub2
            rarityLightPainter = Res.drawable.character_rartylight_2
        }

        3 -> {
            rarityPainter = Res.drawable.character_star_3
            charBgPainter = Res.drawable.character_charbg_3
            upperHubPainter = Res.drawable.character_upperhub_3
            lowerHubPainter = Res.drawable.character_lowerhub3
            rarityLightPainter = Res.drawable.character_rartylight_3
        }

        4 -> {
            rarityPainter = Res.drawable.character_star_4
            charBgPainter = Res.drawable.character_charbg_4
            upperHubPainter = Res.drawable.character_upperhub_4
            lowerHubPainter = Res.drawable.character_lowerhub4
            rarityLightPainter = Res.drawable.character_rartylight_4
        }

        5 -> {
            rarityPainter = Res.drawable.character_star_5
            charBgPainter = Res.drawable.character_charbg_5
            upperHubPainter = Res.drawable.character_upperhub_5
            lowerHubPainter = Res.drawable.character_lowerhub5
            rarityLightPainter = Res.drawable.character_rartylight_5
        }

        6 -> {
            rarityPainter = Res.drawable.character_star_6
            charBgPainter = Res.drawable.character_charbg_6
            upperHubPainter = Res.drawable.character_upperhub_6
            lowerHubPainter = Res.drawable.character_lowerhub6
            rarityLightPainter = Res.drawable.character_rartylight_6
        }
    }
    Box(modifier = Modifier.height(294.dp).width(138.dp).clickable { showDetail = true }) {
        Box(
            modifier = Modifier.padding(4.dp).height(260.dp).width(130.dp).align(Alignment.TopCenter),
        ) {

            //char bg
            Image(
                painter = painterResource(charBgPainter),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Image(
                painter = painterResource(Res.drawable.character_upperhub_shadow),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            //char skin

            val portrait = File("data/portrait/${Table.getSkinPortraitId(char.skin)}.png").absolutePath
            Image(
                painter = rememberImagePainter(ImageRequest(data = portrait.toPath())),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            //rarity light
            Image(
                painter = painterResource(rarityLightPainter),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
            )
            //top start hub
            Image(
                painter = painterResource(upperHubPainter),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopStart)
            )
            //profession icon
            Image(
                painterResource(professionPainter), null,
                modifier = Modifier.align(Alignment.TopStart).padding(start = 6.dp, top = 6.dp).size(28.dp)
            )
            //char rarity star
            Image(
                painterResource(rarityPainter), null,
                modifier = Modifier.align(Alignment.TopStart).padding(top = 6.dp, start = 34.dp)
            )
        }
        //star mark
        Image(
            painterResource(if (char.starMark == 1) Res.drawable.character_star_mark else Res.drawable.character_star_mark_edit),
            null,
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 3.dp, bottom = 4.dp)
        )
        //evolvePhase icon bg
        Image(
            painterResource(Res.drawable.character_elite_bg), null,
            modifier = Modifier.padding(top = 124.dp).size(60.dp).align(Alignment.CenterStart).alpha(0.7f)
        )
        //lower hub
        Image(
            painterResource(lowerHubPainter), null,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        //char evolvePhase
        Image(
            painterResource(evolvePhasePainter), null,
            modifier = Modifier.padding(start = 1.dp, top = 90.dp).size(60.dp).align(Alignment.CenterStart)
        )
        //char potentialRank
        if (char.potentialRank > 0) {
            val potentialPainter = when (char.potentialRank) {
                1 -> Res.drawable.character_potential_1
                2 -> Res.drawable.character_potential_2
                3 -> Res.drawable.character_potential_3
                4 -> Res.drawable.character_potential_4
                else -> Res.drawable.character_potential_5
            }
            Image(
                painterResource(Res.drawable.equip_bg), null,
                modifier = Modifier.padding(top = 132.dp, end = 10.dp).size(20.dp).align(Alignment.CenterEnd)
            )
            Image(
                painterResource(potentialPainter), null,
                modifier = Modifier.padding(top = 132.dp, end = 8.dp).size(24.dp).align(Alignment.CenterEnd)
            )
        }
        //char skill
        val skill = char.skills.getOrNull(char.defaultSkillIndex)
        val skillPainter = if (skill == null) {
            if (char.currentTmpl == null) {
                painterResource(Res.drawable.character_empty_skill)
            } else {
                val tmplChar = char.tmpl!![char.currentTmpl]
                val tmplSkill = tmplChar?.skills?.getOrNull(tmplChar.defaultSkillIndex)
                if (tmplSkill == null) {
                    painterResource(Res.drawable.character_empty_skill)
                } else {
                    val skillIcon = File("data/skill/skill_icon_${tmplSkill.skillId}.png").absolutePath
                    rememberImagePainter(ImageRequest(data = skillIcon.toPath()))
                }
            }
        } else {
            val skillIcon = File("data/skill/skill_icon_${skill.skillId}.png").absolutePath
            rememberImagePainter(ImageRequest(data = skillIcon.toPath()))
        }
        Image(
            painter = skillPainter,
            contentDescription = null,
            modifier = Modifier.padding(bottom = 30.dp, end = 8.dp).size(36.dp).align(Alignment.BottomEnd)
        )
        //char level
        Image(
            painterResource(Res.drawable.character_level_bg), null,
            modifier = Modifier.padding(bottom = 24.dp).size(60.dp).align(Alignment.BottomStart)
        )
        Text(
            text = char.level.toString(),
            color = Color.White,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 30.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 6.dp, bottom = 32.dp).width(50.dp).align(Alignment.BottomStart)
        )
        Text(
            text = "LV",
            color = Color.White,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 8.sp
            ),
            modifier = Modifier.padding(start = 26.dp, bottom = 62.dp).align(Alignment.BottomStart)
        )
        //char name
        Text(
            char.name!!,
            color = Color.White,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 4.dp, end = 8.dp)
        )
    }
    AnimatedVisibility(showDetail) {
        CharacterDetail(char.copy()) {
            showDetail = false
            it?.let { onCharChange(it) }
            /*TODO()*/
        }
    }
}

