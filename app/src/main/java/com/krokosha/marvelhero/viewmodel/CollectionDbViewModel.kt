package com.krokosha.marvelhero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krokosha.marvelhero.model.CharacterResult
import com.krokosha.marvelhero.model.db.DbCharacter
import com.krokosha.marvelhero.model.db.ICollectionDbRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDbViewModel @Inject constructor(private val repo: ICollectionDbRepo): ViewModel() {
    val currentCharacter = MutableStateFlow<DbCharacter?>(null)
    val collection = MutableStateFlow<List<DbCharacter>>(listOf())

    init { getCollection() }

    private fun getCollection() {
        viewModelScope.launch {
            repo.getCharacters().collect {
                collection.value = it
            }
        }
    }

    fun setCurrentCharacterBy(id: Int?) {
        id?.let {
            viewModelScope.launch {
                repo.getCharacterBy(id = it).collect {
                    currentCharacter.value = it
                }
            }
        }
    }

    fun add(character: CharacterResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.add(character = DbCharacter.from(character = character))
        }
    }

    fun delete(character: DbCharacter) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(character = character)
        }
    }
}
