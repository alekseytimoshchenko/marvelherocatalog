package com.krokosha.marvelhero.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krokosha.marvelhero.model.CharacterResult
import com.krokosha.marvelhero.model.CharactersApiResponse
import com.krokosha.marvelhero.model.api.NetworkResult
import com.krokosha.marvelhero.repos.MarvelApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryApiViewModel @Inject constructor(
    private val repo: MarvelApiRepo
): ViewModel() {
    val characters: StateFlow<NetworkResult<CharactersApiResponse>> = repo.characters
    val queryText = MutableStateFlow("")
    private val queryInput = Channel<String>(Channel.CONFLATED)
    val characterDetails: MutableState<CharacterResult?> = repo.characterDetails

    init { retrieveCharacters() }

    @OptIn(FlowPreview::class)
    private fun retrieveCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            queryInput.receiveAsFlow()
                .filter { validateQuery(it) }
                .debounce(1000)
                .collect { repo.query(it) }
        }
    }

    private fun validateQuery(query: String): Boolean = query.length >= 2

    fun onQueryUpdate(input: String) {
        queryText.value = input
        queryInput.trySend(input)
    }

    fun retrieveCharacterBy(id: Int) = repo.getCharacterBy(id = id)
}