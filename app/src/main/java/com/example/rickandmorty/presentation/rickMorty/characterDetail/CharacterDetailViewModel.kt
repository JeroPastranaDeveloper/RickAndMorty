package com.example.rickandmorty.presentation.rickMorty.characterDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.domain.usecase.CharacterUseCase
import com.example.rickandmorty.network.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _character = MutableLiveData<Character?>()
    val character: LiveData<Character?> get() = _character

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> get() = _error

    val isLoading = MutableLiveData<Boolean>()

    fun getCharacter(id: Int) = viewModelScope.launch {
        isLoading.postValue(true)
        if (networkUtils.isNetworkAvailable()) {
            val character = characterUseCase.getCharacter(id)
            _character.postValue(character)
            isLoading.postValue(false)
        } else {
            _error.postValue(Constants.NO_CONNECTION)
            isLoading.postValue(false)
        }
    }
}