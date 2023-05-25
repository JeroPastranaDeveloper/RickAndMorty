package com.example.rickandmorty.presentation.rickMorty.mainFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.data.constants.Constants.Companion.NO_CONNECTION
import com.example.rickandmorty.data.model.Episode
import com.example.rickandmorty.databinding.FragmentEpisodesBinding
import com.example.rickandmorty.presentation.rickMorty.adapters.EpisodeAdapter
import com.example.rickandmorty.presentation.main.MainViewModel
import com.example.rickandmorty.presentation.rickMorty.episodeDetail.EpisodeDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodesFragment : Fragment() {

    private lateinit var binding: FragmentEpisodesBinding
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews(inflater, container)
        initAdapter()
        initObservers()
        return binding.root
    }

    private fun initObservers() {
        mainViewModel.episodes.observe(viewLifecycleOwner) { episodes ->
            episodeAdapter.updateEpisodes(episodes)
        }

        mainViewModel.error.observe(viewLifecycleOwner) { errorCode: Int ->
            val errorMessage = when (errorCode) {
                NO_CONNECTION -> getString(R.string.no_connection)
                else -> getString(R.string.error_general)
            }
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun initAdapter() {
        episodeAdapter = EpisodeAdapter(
            emptyList(),
            object : EpisodeAdapter.OnEpisodeClick {
                override fun onClick(episode: Episode) {
                    val intent = Intent(context, EpisodeDetailActivity::class.java)
                    intent.putExtra(Constants.id, episode.id)
                    startActivity(intent)
                }
            }
        )
        binding.containerEpisodes.layoutManager = LinearLayoutManager(requireContext())
        binding.containerEpisodes.adapter = episodeAdapter
    }

    private fun initViews(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.getEpisodes()
    }

    fun filterEpisodesByName(query: String) {
        mainViewModel.filterEpisodesByName(query)
    }
}