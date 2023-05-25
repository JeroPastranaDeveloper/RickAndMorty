package com.example.rickandmorty.presentation.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Status
import com.example.rickandmorty.databinding.ActivityMainBinding
import com.example.rickandmorty.presentation.profile.ProfileActivity
import com.example.rickandmorty.presentation.rickMorty.dateFilter.DateFilterActivity
import com.example.rickandmorty.presentation.rickMorty.mainFragments.CharactersFragment
import com.example.rickandmorty.presentation.rickMorty.mainFragments.EpisodesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initView()
        initListeners()
    }

    private fun initView() {
        replaceFragment(EpisodesFragment())

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

    private fun initListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_characters -> {
                    replaceFragment(CharactersFragment())
                    true
                }

                R.id.nav_episodes -> {
                    replaceFragment(EpisodesFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container_fragment, fragment)
        transaction.commit()
        currentFragment = fragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }

            R.id.menu_filter -> {
                if (currentFragment is CharactersFragment) {
                    showFilterDialog()
                } else if (currentFragment is EpisodesFragment) {
                    showSearchDialog()
                }
                true
            }

            R.id.menu_show_all -> {
                if (currentFragment is EpisodesFragment) {
                    replaceFragment(EpisodesFragment())
                } else if (currentFragment is CharactersFragment) {
                    mainViewModel.setFilterStatus(Status.ALL)
                }
                true
            }

            R.id.menu_date_filter -> {
                startActivity(Intent(this, DateFilterActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSearchDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.search_episode))

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
            val query = input.text.toString()
            (currentFragment as EpisodesFragment).filterEpisodesByName(query)
            dialog.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun showFilterDialog() {
        val options = arrayOf(
            resources.getString(R.string.filter_live),
            resources.getString(R.string.filter_dead),
            resources.getString(R.string.filter_unknown)
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.filter_by))
        builder.setItems(options) { _, which ->
            val selectedStatus = when (which) {
                0 -> Status.ALIVE
                1 -> Status.DEAD
                2 -> Status.UNKNOWN
                else -> Status.ALL
            }
            mainViewModel.setFilterStatus(selectedStatus)
        }
        val dialog = builder.create()
        dialog.show()
    }
}