package com.example.rickandmorty.presentation.rickMorty.episodeDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.rickandmorty.databinding.ActivityEpisodeDetailBinding
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.data.constants.Constants.Companion.NO_CONNECTION
import com.example.rickandmorty.data.constants.initStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEpisodeDetailBinding
    private lateinit var viewModel: EpisodeDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this)[EpisodeDetailViewModel::class.java]
        initStatusBar()
        initObservers()
    }

    private fun initObservers() {
        val episodeId = intent.getIntExtra(Constants.id, -1)

        viewModel.error.observe(this) { errorCode ->
            val errorMessage = when (errorCode) {
                NO_CONNECTION -> getString(R.string.no_connection)
                else -> getString(R.string.error_general)
            }
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        if (episodeId != -1) {
            viewModel.getEpisode(episodeId)
            viewModel.episode.observe(this) { episode ->
                with(binding) {
                    textId.text = resources.getString(R.string.id, episode?.id.toString())
                    textName.text = resources.getString(R.string.name, episode?.name)
                    textSeason.text = root.context.getString(R.string.season, episode?.episode?.substring(1, 3))
                    textEpisode.text = root.context.getString(R.string.episode, episode?.episode?.substring(4, 6))
                    textDate.text = resources.getString(R.string.date, episode?.airDate)

                    val characterUrls = episode?.characters ?: emptyList()
                    viewModel.getCharacterNames(characterUrls.filterNotNull()).observe(this@EpisodeDetailActivity) { characterNames ->
                        textCharacters.text = resources.getString(R.string.characters, characterNames.joinToString(", "))
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}