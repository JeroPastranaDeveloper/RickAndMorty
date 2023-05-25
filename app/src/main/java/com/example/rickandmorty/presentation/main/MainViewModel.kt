package com.example.rickandmorty.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.constants.Constants.Companion.NO_CONNECTION
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.Episode
import com.example.rickandmorty.data.constants.Status
import com.example.rickandmorty.domain.usecase.CharacterUseCase
import com.example.rickandmorty.domain.usecase.EpisodeUseCase
import com.example.rickandmorty.network.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val episodeUseCase: EpisodeUseCase,
    private val characterUseCase: CharacterUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _allEpisodes = MutableLiveData<List<Episode>>()
    private val _episodes = MutableLiveData<List<Episode>>()
    val episodes: LiveData<List<Episode>> get() = _episodes

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    private val _filterStatus = MutableLiveData(Status.ALL)
    val filterStatus: LiveData<Status> get() = _filterStatus

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> get() = _error

    val isLoading = MutableLiveData<Boolean>()

    fun getEpisodes() = viewModelScope.launch {
        isLoading.postValue(true)
        if (networkUtils.isNetworkAvailable()) {
            val episodes = episodeUseCase.getEpisodes()
            _allEpisodes.postValue(episodes)
            _episodes.postValue(episodes)
            isLoading.postValue(false)
        } else {
            _error.postValue(NO_CONNECTION)
            isLoading.postValue(false)
        }
    }

    fun filterEpisodesByName(query: String) = viewModelScope.launch {
        val filteredEpisodes = _allEpisodes.value?.filter { episode ->
            episode.name?.contains(query, ignoreCase = true) ?: false
        } ?: emptyList()
        _episodes.postValue(filteredEpisodes)
    }

    fun getCharactersByStatus() = viewModelScope.launch {
        isLoading.postValue(true)
        val status = _filterStatus.value ?: Status.ALL
        if (networkUtils.isNetworkAvailable()) {
            val characters = characterUseCase.getCharactersByStatus(status)
            _characters.postValue(characters)
            isLoading.postValue(false)
        } else {
            _error.postValue(NO_CONNECTION)
            isLoading.postValue(false)
        }
    }

    fun setFilterStatus(status: Status) {
        _filterStatus.value = status
    }
}