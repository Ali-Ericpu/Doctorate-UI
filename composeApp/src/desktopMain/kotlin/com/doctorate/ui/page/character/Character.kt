package com.doctorate.ui.page.character

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.lifecycle.viewmodel.compose.viewModel
import com.doctorate.ui.config.AppConfig
import com.doctorate.ui.config.LocalAppConfig
import com.doctorate.ui.config.Table
import com.doctorate.ui.entity.Char
import com.doctorate.ui.entity.Profession
import com.doctorate.ui.network.datasource.CharacterDataSource
import com.doctorate.ui.view.LocalAppToaster
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImagePainter
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.admin_key
import doctorateui.composeapp.generated.resources.cancel
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
import doctorateui.composeapp.generated.resources.icon
import doctorateui.composeapp.generated.resources.icon_profession_caster
import doctorateui.composeapp.generated.resources.icon_profession_medic
import doctorateui.composeapp.generated.resources.icon_profession_pioneer
import doctorateui.composeapp.generated.resources.icon_profession_sniper
import doctorateui.composeapp.generated.resources.icon_profession_special
import doctorateui.composeapp.generated.resources.icon_profession_support
import doctorateui.composeapp.generated.resources.icon_profession_tank
import doctorateui.composeapp.generated.resources.icon_profession_warrior
import doctorateui.composeapp.generated.resources.lv
import doctorateui.composeapp.generated.resources.port
import doctorateui.composeapp.generated.resources.profession_select
import doctorateui.composeapp.generated.resources.save
import doctorateui.composeapp.generated.resources.server_uri
import doctorateui.composeapp.generated.resources.setting
import doctorateui.composeapp.generated.resources.uid
import kotlinx.coroutines.launch
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.roundToInt

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
    val onConfigChange = LocalAppConfig.current.onConfigChange
    val toaster = LocalAppToaster.current
    val currentProfession by viewModel.profession.collectAsState()
    val selectProfession by viewModel.isSelect.collectAsState()
    val showLoadAnimate by viewModel.loadAnimate.collectAsState()
    val splash by viewModel.splash.collectAsState()
    val isEditConfig by viewModel.isEditConfig.collectAsState()
    val charList = viewModel.syncCharList()
    val coroutineScope = rememberCoroutineScope()
    val professions = Profession.entries.toList()
    val professionOffsetX by animateFloatAsState(if (selectProfession) 0f else 1.2f)
    val menuOffsetX by animateFloatAsState(if (!selectProfession) 0f else 1.5f)
    Box(modifier = Modifier.fillMaxSize()) {
        CompositionLocalProvider(
            LocalImageLoader provides remember { generateImageLoader() },
        ) {
            LazyVerticalGrid(GridCells.FixedSize(138.dp)) {
                items(charList) {
                    CharacterCard(it) {
                        coroutineScope.launch {
                            try {
                                viewModel.changeCharData(it, config.adminKey, config.uid)
                            } catch (e: Exception) {
                                toaster.toastFailure(e.message.toString())
                            }
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier.width(66.dp).align(alignment = Alignment.TopEnd).padding(top = 24.dp)) {
            LazyColumn(
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .offsetPercent(offsetPercentX = professionOffsetX)
            ) {
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
            if (!splash) {
                Column(modifier = Modifier.offsetPercent(offsetPercentX = menuOffsetX)) {
                    CircleIconButton(
                        icon = Icons.Default.Menu,
                        onclick = { viewModel.changeSelectState(true) }
                    )
                    CircleIconButton(
                        icon = Icons.Default.Settings,
                        onclick = { viewModel.changeEditState() }
                    )
                    Icons.Default.Menu
                    CircleIconButton(
                        icon = Icons.Default.Refresh,
                        onclick = {
                            coroutineScope.launch {
                                try {
                                    viewModel.initCharData(config.adminKey, config.uid)
                                } catch (e: Exception) {
                                    viewModel.closeAnimate()
                                    toaster.toastFailure(e.message.toString())
                                }
                            }
                        }
                    )
                }
            }
        }
        if (splash) {
            CircleIconButton(
                icon = Icons.Default.Build,
                onclick = {
                    coroutineScope.launch {
                        try {
                            viewModel.initCharData(config.adminKey, config.uid)
                        } catch (e: Exception) {
                            viewModel.closeAnimate()
                            toaster.toastFailure(e.message.toString())
                        }
                    }
                },
                size = 60,
                modifier = Modifier.align(alignment = Alignment.Center)
            )
        }
        AnimatedVisibility(
            visible = showLoadAnimate,
            enter = fadeIn(
                initialAlpha = 0.1f,
                animationSpec = tween(100)
            ),
            exit = fadeOut(
                targetAlpha = 0f,
                animationSpec = tween(800)
            )
        ) {
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
                val rotate by rememberInfiniteTransition().animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
                Icon(
                    painter = painterResource(Res.drawable.icon),
                    null,
                    modifier = Modifier.size(100.dp).rotate(rotate).align(alignment = Alignment.Center)
                )
            }
        }
    }
    if (isEditConfig) {
        ExtraSettingDialog(
            config = config,
            onValueSave = {
                viewModel.changeEditState()
                it?.let {
                    onConfigChange(it)
                    CharacterDataSource.reset()
                }
            }
        )
    }
}

@Composable
fun CharacterCard(char: Char, onCharChange: (char: Char) -> Unit) {
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
            val portraitId = URLEncoder.encode(Table.getSkinPortraitId(char.skin), StandardCharsets.UTF_8)
            val link = "https://torappu.prts.wiki/assets/char_portrait/$portraitId.png"
            Image(
                painter = rememberImagePainter(ImageRequest(data = link)),
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
            text = stringResource(Res.string.lv),
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
    if (showDetail) {
        CharacterDetail(char.copy()) {
            showDetail = false
            it?.let { onCharChange(it) }
        }
    }
}

fun Modifier.offsetPercent(offsetPercentX: Float = 0f, offsetPercentY: Float = 0f): Modifier =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            val offsetX = (offsetPercentX * placeable.width).roundToInt()
            val offsetY = (offsetPercentY * placeable.height).roundToInt()
            placeable.place(offsetX, offsetY)
        }
    }

@Composable
fun CircleIconButton(
    icon: ImageVector,
    onclick: () -> Unit,
    size: Int = 48,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = { onclick() }, modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .padding(8.dp)
                .size(size.dp)
                .background(color = MaterialTheme.colors.primary, shape = CircleShape)
                .padding(12.dp)
        )
    }
}

@Composable
fun ExtraSettingDialog(config: AppConfig, onValueSave: (AppConfig?) -> Unit) {
    DialogWindow(
        onCloseRequest = { onValueSave(null) },
        resizable = false,
        title = stringResource(Res.string.setting),
        state = DialogState(
            size = DpSize(400.dp, 320.dp),
            position = WindowPosition(Alignment.Center)
        )
    ) {
        var serverUri by remember { mutableStateOf(config.serverUri) }
        var serverPort by remember { mutableStateOf(config.serverPort) }
        var uid by remember { mutableStateOf(config.uid) }
        var adminKey by remember { mutableStateOf(config.adminKey) }
        val textColors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.DarkGray,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.DarkGray,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            Row {
                OutlinedTextField(
                    value = serverUri,
                    onValueChange = { serverUri = it },
                    label = { Text(stringResource(Res.string.server_uri)) },
                    singleLine = true,
                    colors = textColors,
                    modifier = Modifier.padding(8.dp).weight(7f),
                )
                OutlinedTextField(
                    value = serverPort,
                    onValueChange = { serverPort = it },
                    label = { Text(stringResource(Res.string.port)) },
                    isError = serverPort.isNotBlank() && (serverPort.toIntOrNull() == null || serverPort.toUInt() > 65536u),
                    singleLine = true,
                    colors = textColors,
                    modifier = Modifier.padding(8.dp).weight(3f),
                )
            }
            Row {
                OutlinedTextField(
                    value = uid,
                    onValueChange = { uid = it },
                    label = { Text(stringResource(Res.string.uid)) },
                    isError = uid.isNotBlank() && uid.toDoubleOrNull() == null,
                    singleLine = true,
                    colors = textColors,
                    modifier = Modifier.padding(8.dp).weight(1f),
                )
                OutlinedTextField(
                    value = adminKey,
                    onValueChange = { adminKey = it },
                    label = { Text(stringResource(Res.string.admin_key)) },
                    singleLine = true,
                    colors = textColors,
                    modifier = Modifier.padding(8.dp).weight(1f),
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                TextButton(
                    onClick = { onValueSave(null) },
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colors.primary)
                ) {
                    Text(text = stringResource(Res.string.cancel), color = Color.Black)
                }
                TextButton(
                    onClick = {
                        onValueSave(
                            config.copy(
                                serverUri = serverUri,
                                serverPort = serverPort,
                                uid = uid,
                                adminKey = adminKey,
                            )
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colors.primary)
                ) {
                    Text(text = stringResource(Res.string.save), color = Color.Black)
                }
            }
        }
    }
}

fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            // cache 32MB bitmap
            bitmapMemoryCacheConfig {
                maxSize(32 * 1024 * 1024) // 32MB
            }
            // cache 50 image
            imageMemoryCacheConfig {
                maxSize(50)
            }
            // cache 50 painter
            painterMemoryCacheConfig {
                maxSize(50)
            }
            diskCacheConfig {
                directory(File("data/").toOkioPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

