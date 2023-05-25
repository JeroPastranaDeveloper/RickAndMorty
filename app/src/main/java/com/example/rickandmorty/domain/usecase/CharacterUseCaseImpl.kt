package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.constants.Status
import javax.inject.Inject

class CharacterUseCaseImpl @Inject constructor(private val characterRepository: CharacterRepository) :
    CharacterUseCase {
    override suspend fun getCharacter(id: Int): Character? {
        return characterRepository.getCharacter(id)
    }

    override suspend fun getCharactersByStatus(status: Status): List<Character> {
        return characterRepository.getCharactersByStatus(status)
    }

    override suspend fun getAllCharacters(ids: List<Int>): List<Character> {
        return characterRepository.getAllCharacters(ids)
    }
}
