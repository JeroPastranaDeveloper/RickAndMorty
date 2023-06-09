package com.example.rickandmorty.useCaseImplementations

import com.example.rickandmorty.data.model.Episode
import com.example.rickandmorty.data.repository.EpisodeRepository
import com.example.rickandmorty.domain.usecase.EpisodeUseCase
import javax.inject.Inject

class EpisodeUseCaseImpl @Inject constructor(private val episodeRepository: EpisodeRepository) :
    EpisodeUseCase {
    override suspend fun getEpisodes(): List<Episode> {
        return episodeRepository.getEpisodes()
    }

    override suspend fun getEpisode(id: Int): Episode? {
        return episodeRepository.getEpisode(id)
    }
}