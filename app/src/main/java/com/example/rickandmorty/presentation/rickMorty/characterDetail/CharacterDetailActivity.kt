package com.example.rickandmorty.presentation.rickMorty.characterDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.data.constants.initStatusBar
import com.example.rickandmorty.databinding.ActivityCharacterDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterDetailBinding
    private lateinit var viewModel: CharacterDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this)[CharacterDetailViewModel::class.java]
        initStatusBar()
        initObservers()
    }

    private fun initObservers() {
        val characterId = intent.getIntExtra(Constants.id, -1)

        viewModel.error.observe(this) { errorCode ->
            val errorMessage = when (errorCode) {
                Constants.NO_CONNECTION -> getString(R.string.no_connection)
                else -> getString(R.string.error_general)
            }
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        if (characterId != -1) {
            viewModel.getCharacter(characterId)
            viewModel.character.observe(this) { character ->
                with(binding) {
                    Glide.with(root.context)
                        .load(character?.image)
                        .placeholder(ContextCompat.getDrawable(root.context, R.drawable.no_image))
                        .into(imageCharacter)
                    textId.text = resources.getString(R.string.id, character?.id.toString())
                    textName.text = resources.getString(R.string.name, character?.name)
                    textStatus.text = resources.getString(R.string.status, character?.status)
                    textSpecie.text = resources.getString(R.string.specie, character?.species)
                    textType.text = resources.getString(R.string.type, character?.type)
                    textGender.text = resources.getString(R.string.gender, character?.gender)
                    textOrigin.text = resources.getString(R.string.origin, character?.origin?.name)
                    textLocation.text = resources.getString(R.string.location, character?.location?.name)
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