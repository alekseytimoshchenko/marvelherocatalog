package com.krokosha.marvelhero.repos

import com.krokosha.marvelhero.model.CharactersApiResponse
import com.krokosha.marvelhero.model.api.MarvelApi
import com.krokosha.marvelhero.model.api.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class MarvelApiRepo(private val api: MarvelApi) {
    private val _characters = MutableStateFlow<NetworkResult<CharactersApiResponse>>(NetworkResult.Initial())
    val characters: StateFlow<NetworkResult<CharactersApiResponse>> = _characters

    fun query(query: String) {
        _characters.value = NetworkResult.Loading()

        api.getCharacters(name = query)
            .enqueue(object : Callback<CharactersApiResponse> {
                override fun onResponse(call: Call<CharactersApiResponse>, response: Response<CharactersApiResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _characters.value = NetworkResult.Success(it)
                        }
                    } else {
                        _characters.value = NetworkResult.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<CharactersApiResponse>, t: Throwable) {
                    t.localizedMessage?.let {
                        _characters.value = NetworkResult.Error(it)
                    }
                }
            })
    }
}