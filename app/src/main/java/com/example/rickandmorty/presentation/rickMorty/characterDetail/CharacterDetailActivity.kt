package com.example.rickandmorty.presentation.rickMorty.characterDetail

import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Constants
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
        initViews()
        initObservers()
    }

    private fun initViews() {
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if(isDarkTheme) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.white_black)
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                window.statusBarColor = ContextCompat.getColor(this, R.color.white_black)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.white_black)
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                window.statusBarColor = ContextCompat.getColor(this, R.color.white_black)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
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
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        if (characterId != -1) {
            viewModel.getCharacter(characterId)
            viewModel.character.observe(this) { character ->

                Glide.with(binding.root.context)
                    .load(character?.image)
                    .placeholder(ContextCompat.getDrawable(binding.root.context, R.drawable.no_image))
                    .into(binding.imageCharacter)
                binding.textId.text = resources.getString(R.string.id, character?.id.toString())
                binding.textName.text = resources.getString(R.string.name, character?.name)
                binding.textStatus.text = resources.getString(R.string.status, character?.status)
                binding.textSpecie.text = resources.getString(R.string.specie, character?.species)
                binding.textType.text = resources.getString(R.string.type, character?.type)
                binding.textGender.text = resources.getString(R.string.gender, character?.gender)
                binding.textOrigin.text = resources.getString(R.string.origin, character?.origin?.name)
                binding.textLocation.text = resources.getString(R.string.location, character?.location?.name)
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