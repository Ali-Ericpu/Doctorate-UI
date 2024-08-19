package com.doctorate.ui.page.character

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.doctorate.ui.config.Table
import com.doctorate.ui.entity.Char
import com.doctorate.ui.entity.SaveCharBody
import com.doctorate.ui.network.datasource.CharacterDataSource
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
    private var _isEditConfig = MutableStateFlow(false)
    val isEditConfig = _isEditConfig.asStateFlow()
    private var _isSelect = MutableStateFlow(false)
    val isSelect = _isSelect.asStateFlow()
    private var _profession = MutableStateFlow("ALL")
    val profession = _profession.asStateFlow()
    private var _loadAnimate = MutableStateFlow(false)
    val loadAnimate = _loadAnimate.asStateFlow()


    init {
        println("Load character ViewModel")
    }

    fun syncCharList(): List<Char> = characterList.also { it.sort() }

    suspend fun initCharData(adminKey: String, uid: String) = withContext(Dispatchers.IO) {
        _loadAnimate.emit(true)
        val result = CharacterDataSource.syncCharacter(adminKey, uid)
        if (result.status != 0 || result.data == null) throw RuntimeException(result.msg)
        characterData.clear()
        result.data.forEach { instId, char ->
            val charData = Table.getCharacterData(char.charId)
            char.name = charData["name"] as String
            char.profession = charData["profession"] as String
            char.rarity = (charData["rarity"] as String).substringAfter("_").toInt()
            characterData[instId] = char
        }
        selectProfession(_profession.value)
        delay(500)
        closeAnimate()
    }

    fun selectProfession(profession: String) {
        CoroutineScope(Dispatchers.Default).launch {
            println("select :$profession")
            _profession.emit(profession)
            characterList.clear()
            if (profession == "ALL") {
                characterList.addAll(characterData.values)
            } else {
                characterData.values.filter { it.profession!! == profession }
                    .forEach { characterList.add(it) }
            }
        }
    }

    fun changeSelectState(state: Boolean) =
        CoroutineScope(Dispatchers.Default).launch {
            _isSelect.emit(state)
        }


    suspend fun changeCharData(char: Char, adminKey: String, uid: String) = withContext(Dispatchers.IO) {
        val result = CharacterDataSource.saveCharacter(adminKey, SaveCharBody(uid, char.instId, char))
        if (result.status != 0) throw RuntimeException(result.msg)
        characterData[char.instId.toString()] = char
        selectProfession(_profession.value)
    }

    suspend fun closeAnimate() = withContext(Dispatchers.IO) {
        _splash.emit(false)
        _loadAnimate.emit(false)
    }

    fun changeEditState() = CoroutineScope(Dispatchers.Default).launch {
        _isEditConfig.emit(!isEditConfig.value)
    }

}