package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.source.CharactersDataSource
import com.example.rickandmorty.data.constants.Status
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val dataSource: CharactersDataSource) : CharacterRepository {
    override suspend fun getCharacter(id: Int): Character? {
        return dataSource.getCharacter(id)
    }

    override suspend fun getCharactersByStatus(status: Status): List<Character> {
        return dataSource.getCharactersByStatus(status)
    }

    override suspend fun getAllCharacters(ids: List<Int>): List<Character> {
        return dataSource.getAllCharacters(ids)
    }
}