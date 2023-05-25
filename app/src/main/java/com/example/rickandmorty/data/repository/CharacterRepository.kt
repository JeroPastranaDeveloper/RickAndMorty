package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.constants.Status

interface CharacterRepository {
    suspend fun getCharacter(id: Int): Character?
    suspend fun getCharactersByStatus(status: Status): List<Character>
    suspend fun getAllCharacters(ids: List<Int>): List<Character>
}