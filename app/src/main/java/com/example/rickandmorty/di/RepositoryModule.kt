package com.example.rickandmorty.di

import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.repository.CharacterRepositoryImpl
import com.example.rickandmorty.data.repository.EpisodeRepository
import com.example.rickandmorty.data.repository.EpisodeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsEpisodeRepository(repositoryImpl: EpisodeRepositoryImpl): EpisodeRepository

    @Binds
    abstract fun bindsCharacterRepository(repositoryImpl: CharacterRepositoryImpl): CharacterRepository
}