package com.example.rickandmorty.presentation.rickMorty.dateFilter

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.data.model.Episode
import com.example.rickandmorty.domain.usecase.EpisodeUseCase
import com.example.rickandmorty.network.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DateFilterViewModel @Inject constructor(
    private val episodeUseCase: EpisodeUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _episodes = MutableLiveData<List<Episode>>()
    val episodes: LiveData<List<Episode>> get() = _episodes

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> get() = _error

    private val _isDateError = MutableLiveData<Boolean>()
    val isDateError: LiveData<Boolean> get() = _isDateError

    private val _isReset = MutableLiveData<Boolean>()
    val isReset: LiveData<Boolean> get() = _isReset

    private var allEpisodes: List<Episode> = listOf()

    fun getEpisodes() = viewModelScope.launch {
        if (networkUtils.isNetworkAvailable()) {
            allEpisodes = episodeUseCase.getEpisodes()
            _episodes.postValue(allEpisodes)
        } else {
            _error.postValue(Constants.NO_CONNECTION)
        }
    }

    fun validateDates(
        firstDate: Date?,
        secondDate: Date?,
        textFirstDate: String,
        textSecondDate: String,
        res: Resources
    ) {
        if ((textFirstDate == res.getString(R.string.first_date) ||
                    textSecondDate == res.getString(R.string.second_date)) ||
            firstDate?.after(secondDate) == true
        ) {
            _isDateError.postValue(true)
        } else {
            _isDateError.postValue(false)
        }
    }

    fun resetState(containerVisible: Boolean) {
        _isReset.postValue(!containerVisible)
    }

    fun filterEpisodesByDate(startDate: Date, endDate: Date) {
        if (networkUtils.isNetworkAvailable()) {
            val filteredEpisodes = allEpisodes.filter { episode ->
                val episodeDate = episode.airDate?.let { date ->
                    SimpleDateFormat(Constants.vmDate, Locale.US).parse(date)
                }
                episodeDate != null && (episodeDate in startDate..endDate)
            }
            _episodes.postValue(filteredEpisodes)
        } else {
            _error.postValue(Constants.NO_CONNECTION)
        }
    }
}