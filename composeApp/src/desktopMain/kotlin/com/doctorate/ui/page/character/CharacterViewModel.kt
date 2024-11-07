package com.doctorate.ui.page.character

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.doctorate.ui.config.Table
import com.doctorate.ui.entity.Char
import com.doctorate.ui.entity.Item
import com.doctorate.ui.network.datasource.CharacterDataSource
import com.doctorate.ui.req.GainItemReq
import com.doctorate.ui.req.SaveCharReq
import com.doctorate.ui.req.UnlockAllCharReq
import com.doctorate.ui.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ClassName: CharacterViewModel
 * Package: com.doctorate.ui.page.character
 * Description:
 * @author Raincc
 * @Create 2024/8/15 21:30
 * @Version 1.0
 */
class CharacterViewModel : ViewModel() {

    val characterData = mutableMapOf<String, Char>()
    val characterList = mutableStateListOf<Char>()
    private var _splash = MutableStateFlow(true)
    val splash = _splash.asStateFlow()
    private var _isSetting = MutableStateFlow(false)
    val isSetting = _isSetting.asStateFlow()
    private var _isSelect = MutableStateFlow(false)
    val isSelect = _isSelect.asStateFlow()
    private var _profession = MutableStateFlow("ALL")
    val profession = _profession.asStateFlow()
    private var _loadAnimate = MutableStateFlow(false)
    val loadAnimate = _loadAnimate.asStateFlow()
    private var _gainChar = MutableStateFlow(false)
    val gainChar = _gainChar.asStateFlow()
    private var _extension = MutableStateFlow(false)
    val extension = _extension.asStateFlow()


    init {
        log().info("Load Character ViewModel")
    }

    fun syncCharList(): List<Char> = characterList.also { it.sort() }

    suspend fun initCharData(adminKey: String, uid: String) = withContext(Dispatchers.IO) {
        if (!loadAnimate.value) {
            _loadAnimate.emit(true)
            val result = CharacterDataSource.syncCharacter(adminKey, uid)
            if (result.status != 0 || result.data == null) throw RuntimeException(result.msg)
            characterData.clear()
            result.data.forEach { instId, char ->
                runCatching { Table.getCharacterData(char.charId) }.onSuccess {
                    char.name = it["name"] as String
                    char.profession = it["profession"] as String
                    char.rank = (it["rarity"] as String).substringAfter("_").toInt()
                    characterData[instId] = char
                }.onFailure { log().info(it.message!!) }
            }
            selectProfession(_profession.value)
            delay(500)
            closeAnimate()
        }
    }

    fun selectProfession(profession: String) {
        CoroutineScope(Dispatchers.Default).launch {
            log().info("Select profession: {}", profession)
            _profession.emit(profession)
            characterList.clear()
            if (profession == "ALL") {
                characterList.addAll(characterData.values)
            } else {
                characterList.addAll(characterData.values.filter { it.profession!! == profession })
            }
        }
    }

    fun changeSelectState(state: Boolean) = CoroutineScope(Dispatchers.Default).launch {
        _isSelect.emit(state)
    }

    suspend fun changeCharData(char: Char, adminKey: String, uid: String) = withContext(Dispatchers.IO) {
        val result = CharacterDataSource.saveCharacter(adminKey, uid, SaveCharReq(char.instId, char))
        if (result.status != 0) throw RuntimeException(result.msg)
        characterData[char.instId.toString()] = char
        selectProfession(_profession.value)
    }

    suspend fun closeAnimate() = withContext(Dispatchers.IO) {
        _splash.emit(false)
        _loadAnimate.emit(false)
    }

    fun changeSettingState() = CoroutineScope(Dispatchers.Default).launch {
        _isSetting.emit(!isSetting.value)
    }

    fun changeGainCharState() = CoroutineScope(Dispatchers.Default).launch {
        _gainChar.emit(!gainChar.value)
    }

    suspend fun gainItem(adminKey: String, uid: String, item: Item) = withContext(Dispatchers.IO) {
        CharacterDataSource.gainItem(adminKey, uid, GainItemReq(listOf(item)))
        if (item.type == "CHAR") {
            initCharData(adminKey, uid)
        }
    }

    fun changeExtensionState() = CoroutineScope(Dispatchers.Default).launch {
        _extension.emit(!extension.value)
    }

    suspend fun unlockAllFlags(adminKey: String, uid: String) = withContext(Dispatchers.IO) {
        CharacterDataSource.unlockAllFlags(adminKey, uid)
    }

    suspend fun unlockAllStages(adminKey: String, uid: String) = withContext(Dispatchers.IO) {
        CharacterDataSource.unlockAllStages(adminKey, uid)
    }

    suspend fun unlockAllChar(adminKey: String, uid: String, unlockAllCharReq: UnlockAllCharReq) =
        withContext(Dispatchers.IO) { CharacterDataSource.unlockAllChar(adminKey, uid, unlockAllCharReq) }

}