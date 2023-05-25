package com.example.rickandmorty.presentation.rickMorty.episodeDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.constants.Constants.Companion.NO_CONNECTION
import com.example.rickandmorty.data.model.Episode
import com.example.rickandmorty.domain.usecase.CharacterUseCase
import com.example.rickandmorty.domain.usecase.EpisodeUseCase
import com.example.rickandmorty.network.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailViewModel @Inject constructor(
    private val episodeUseCase: EpisodeUseCase,
    private val networkUtils: NetworkUtils,
    private val characterUseCase: CharacterUseCase
) : ViewModel() {

    private val _episode = MutableLiveData<Episode?>()
    val episode: LiveData<Episode?> get() = _episode

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> get() = _error

    val isLoading = MutableLiveData<Boolean>()

    fun getEpisode(id: Int) = viewModelScope.launch {
        isLoading.postValue(true)
        if (networkUtils.isNetworkAvailable()) {
            val episode = episodeUseCase.getEpisode(id)
            _episode.postValue(episode)
            isLoading.postValue(false)
        } else {
            _error.postValue(NO_CONNECTION)
            isLoading.postValue(false)
        }
    }

    fun getCharacterNames(characterUrls: List<String>) = liveData {
        if (networkUtils.isNetworkAvailable()) {
            val ids = characterUrls.map { it.split("/").last().toInt() }
            val characters = characterUseCase.getAllCharacters(ids)
            emit(characters.map { it.name })
        } else {
            emit(emptyList())
        }
    }
}