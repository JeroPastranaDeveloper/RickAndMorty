package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.model.Episode

interface EpisodeRepository {
    suspend fun getEpisodes(): List<Episode>
    suspend fun getEpisode(id: Int): Episode?
}
