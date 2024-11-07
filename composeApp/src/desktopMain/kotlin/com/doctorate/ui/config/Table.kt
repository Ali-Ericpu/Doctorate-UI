package com.doctorate.ui.config

import com.doctorate.ui.util.JsonUtil
import com.doctorate.ui.util.get
import com.doctorate.ui.util.getValue
import java.io.File

/**
 * ClassName: Table
 * Package: com.doctorate.ui.config
 * Description:
 * @author Raincc
 * @Create 2024/8/16 21:53
 * @Version 1.0
 */
object Table {
    val CHARACTER_TABLE = JsonUtil.fromJson<Map<String, Map<String, Any>>>(File("data/excel/character_table.json"))
    val SKIN_TABLE = JsonUtil.transToMap(File("data/excel/skin_table.json"))
    val FAVOR_TABLE = JsonUtil.transToMap(File("data/excel/favor_table.json"))

    fun getCharacterData(charId: String): Map<String, Any> {
        return CHARACTER_TABLE[charId] ?: throw RuntimeException("$charId is not exists")
    }

    fun getSkinPortraitId(skinId: String): String? {
        return SKIN_TABLE.getValue<String>("charSkins[\"${skinId}\"].portraitId")
    }

    fun getMaxCharEvoLevel(charId: String): Int {
        return getCharacterData(charId).get<List<Map<String, Any>>>("phases")!!.size - 1
    }

    fun getMaxCharLevel(charId: String, evoPhase: Int): Int {
        val phases = getCharacterData(charId).get<List<Map<String, Any>>>("phases")!!
        val phase = phases.getOrNull(evoPhase) ?: phases.first()
        return phase["maxLevel"] as Int
    }

    fun getRealFavPoint(percent: Int): Int {
        val favorFrames = FAVOR_TABLE.get<List<Map<String, Any>>>("favorFrames")!!
        return favorFrames[percent]["level"] as Int
    }

    fun getFavPointPercent(favPoint: Int): Int {
        val favorFrames = FAVOR_TABLE.get<List<Map<String, Any>>>("favorFrames")!!
        favorFrames.forEach {
            if (it["level"] as Int >= favPoint) {
                return it.getValue<Int>("data.percent")!!
            }
        }
        return 0
    }
}