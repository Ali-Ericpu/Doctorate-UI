package com.doctorate.ui.entity

import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.character_profession_caster
import doctorateui.composeapp.generated.resources.character_profession_medic
import doctorateui.composeapp.generated.resources.character_profession_pioneer
import doctorateui.composeapp.generated.resources.character_profession_sniper
import doctorateui.composeapp.generated.resources.character_profession_special
import doctorateui.composeapp.generated.resources.character_profession_support
import doctorateui.composeapp.generated.resources.character_profession_tank
import doctorateui.composeapp.generated.resources.character_profession_warrior
import org.jetbrains.compose.resources.DrawableResource

/**
 * ClassName: Char
 * Package: com.doctorate.ui.entity
 * Description:
 * @author Raincc
 * @Create 2024/8/13 14:46
 * @Version 1.0
 */
data class Char(
    val instId: Int,
    val charId: String,
    var name: String?,
    var rarity: Int?,
    var profession: String?,
    var favorPoint: Int,
    var potentialRank: Int,
    var mainSkillLvl: Int,
    var skin: String,
    var level: Int,
    var exp: Int,
    var evolvePhase: Int,
    var defaultSkillIndex: Int,
    var gainTime: Long,
    var skills: MutableList<Skill>,
    var voiceLan: String,
    var currentEquip: String?,
    var equip: MutableMap<String, Equip>,
    var starMark: Int,
    var currentTmpl: String?,
    var tmpl: MutableMap<String, TmplChar>?
) : Comparable<Char> {
    override fun compareTo(other: Char): Int =
        when {
            this.starMark != other.starMark -> starMark.compareTo(other.starMark)
            this.evolvePhase != other.evolvePhase -> other.evolvePhase.compareTo(evolvePhase)
            this.rarity != other.rarity -> other.rarity!!.compareTo(rarity!!)
            this.level != other.level -> other.level.compareTo(level)
            else -> 0
        }
}

data class Skill(
    val skillId: String,
    var unlock: Int,
    var state: Int,
    var specializeLevel: Int,
    var completeUpgradeTime: Int,
)

data class Equip(
    var hide: Int,
    var level: Int,
    var locked: Int
)

data class TmplChar(
    var skinId: String,
    var defaultSkillIndex: Int,
    var skills: MutableList<Skill>,
    var currentEquip: String?,
    var equip: MutableMap<String, Equip>
)

enum class Profession(val icon: DrawableResource) {
    SNIPER(Res.drawable.character_profession_sniper),
    WARRIOR(Res.drawable.character_profession_warrior),
    TANK(Res.drawable.character_profession_tank),
    PIONEER(Res.drawable.character_profession_pioneer),
    CASTER(Res.drawable.character_profession_caster),
    MEDIC(Res.drawable.character_profession_medic),
    SUPPORT(Res.drawable.character_profession_support),
    SPECIAL(Res.drawable.character_profession_special),
}