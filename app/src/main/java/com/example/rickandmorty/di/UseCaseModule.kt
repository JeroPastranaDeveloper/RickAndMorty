package com.example.rickandmorty.di

import com.example.rickandmorty.domain.usecase.CharacterUseCase
import com.example.rickandmorty.domain.usecase.CharacterUseCaseImpl
import com.example.rickandmorty.domain.usecase.EpisodeUseCase
import com.example.rickandmorty.domain.usecase.EpisodeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindsEpisodeUseCase(useCaseImpl: EpisodeUseCaseImpl) : EpisodeUseCase

    @Binds
    abstract fun bindsCharacterUseCase(useCaseImpl: CharacterUseCaseImpl) : CharacterUseCase
}