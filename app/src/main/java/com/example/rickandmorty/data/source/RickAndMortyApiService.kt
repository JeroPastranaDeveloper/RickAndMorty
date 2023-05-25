package com.example.rickandmorty.data.source

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.CharacterResponse
import com.example.rickandmorty.data.model.Episode
import com.example.rickandmorty.data.model.EpisodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyApiService {
    @GET("episode")
    suspend fun getEpisodes(): Response<EpisodeResponse>

    @GET("character")
    suspend fun getCharacters(): Response<CharacterResponse>

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Response<Episode>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<Character>

    @GET("character/{ids}")
    suspend fun getAllCharacters(@Path("ids") ids: String): Response<List<Character>>
}