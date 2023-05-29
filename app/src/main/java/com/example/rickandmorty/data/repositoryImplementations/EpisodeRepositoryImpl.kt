package com.example.rickandmorty.data.repositoryImplementations

import com.example.rickandmorty.data.model.Episode
import com.example.rickandmorty.data.repository.EpisodeRepository
import com.example.rickandmorty.data.source.EpisodesDataSource
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val dataSource: EpisodesDataSource
) : EpisodeRepository {
    override suspend fun getEpisodes(): List<Episode> {
        return dataSource.getEpisodes()
    }

    override suspend fun getEpisode(id: Int): Episode? {
        return dataSource.getEpisode(id)
    }
}