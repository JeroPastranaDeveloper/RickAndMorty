package com.example.rickandmorty.data.source

import com.example.rickandmorty.data.model.Episode
import javax.inject.Inject

class EpisodesDataSource @Inject constructor() {
    suspend fun getEpisodes(): List<Episode> {
        val response = RetrofitInstance.api.getEpisodes()
        return response.body()?.results?.filterNotNull() ?: emptyList()
    }

    suspend fun getEpisode(id: Int): Episode? {
        val response = RetrofitInstance.api.getEpisode(id)
        return if (response.isSuccessful) response.body() else null
    }
}