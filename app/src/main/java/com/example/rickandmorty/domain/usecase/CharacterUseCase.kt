package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.constants.Status

interface CharacterUseCase {
    suspend fun getCharacter(id: Int): Character?
    suspend fun getCharactersByStatus(status: Status): List<Character>
    suspend fun getAllCharacters(ids: List<Int>): List<Character>
}