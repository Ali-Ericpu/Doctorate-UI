package com.doctorate.ui.page.character

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.doctorate.ui.config.Table
import com.doctorate.ui.entity.Char
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    private var _isSelect = MutableStateFlow(false)
    val isSelect = _isSelect.asStateFlow()
    private var _profession = MutableStateFlow("ALL")
    val profession = _profession.asStateFlow()


    init {
        println("Load character ViewModel")
    }

    fun syncCharList(): List<Char> = characterList.also { it.sort() }

    fun initCharData(charsData: Map<String, Char>) {
        characterData.clear()
        characterList.clear()
        charsData.forEach { instId, char ->
            val charData = Table.getCharacterData(char.charId)
            char.name = charData["name"] as String
            char.profession = charData["profession"] as String
            char.rarity = (charData["rarity"] as String).substringAfter("_").toInt()
            characterData[instId] = char
        }
        selectProfession(_profession.value)
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

    fun changeCharData(char: Char) {
        characterData[char.instId.toString()] = char
        selectProfession(_profession.value)
    }

}