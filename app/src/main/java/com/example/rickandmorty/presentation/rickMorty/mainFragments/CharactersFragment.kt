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
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import com.example.rickandmorty.presentation.rickMorty.adapters.CharacterAdapter
import com.example.rickandmorty.presentation.main.MainViewModel
import com.example.rickandmorty.presentation.rickMorty.characterDetail.CharacterDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding
    private lateinit var characterAdapter: CharacterAdapter
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
        mainViewModel.characters.observe(viewLifecycleOwner) { characters: List<Character?> ->
            characterAdapter.updateCharacters(characters)
        }

        mainViewModel.filterStatus.observe(viewLifecycleOwner) {
            mainViewModel.getCharactersByStatus()
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
        characterAdapter = CharacterAdapter(
            emptyList(),
            object : CharacterAdapter.OnCharacterClick {
                override fun onClick(character: Character) {
                    val intent = Intent(context, CharacterDetailActivity::class.java)
                    intent.putExtra(Constants.id, character.id)
                    startActivity(intent)
                }
            }
        )
        binding.containerCharacters.layoutManager = LinearLayoutManager(requireContext())
        binding.containerCharacters.adapter = characterAdapter
    }

    private fun initViews(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }
}