package com.example.rickandmorty.domain.usecase

import com.example.rickandmorty.data.model.Episode

interface EpisodeUseCase {
    suspend fun getEpisodes(): List<Episode>
    suspend fun getEpisode(id: Int): Episode?
}