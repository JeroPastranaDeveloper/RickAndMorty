package com.example.rickandmorty.data.source

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.constants.Status
import javax.inject.Inject

class CharactersDataSource @Inject constructor() {
    private suspend fun getCharacters(): List<Character> {
        val response = RetrofitInstance.api.getCharacters()
        return response.body()?.results?.filterNotNull() ?: emptyList()
    }

    suspend fun getCharacter(id: Int): Character? {
        val response = RetrofitInstance.api.getCharacter(id)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getCharactersByStatus(status: Status): List<Character> {
        val characters = getCharacters()
        return when (status) {
            Status.ALIVE -> characters.filter { it.status == Status.ALIVE.statusString }
            Status.DEAD -> characters.filter { it.status == Status.DEAD.statusString }
            Status.UNKNOWN -> characters.filter { it.status == Status.UNKNOWN.statusString }
            Status.ALL -> characters
        }
    }

    suspend fun getAllCharacters(ids: List<Int>): List<Character> {
        val response = RetrofitInstance.api.getAllCharacters(ids.joinToString(","))
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
}