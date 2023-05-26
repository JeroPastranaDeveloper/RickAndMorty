package com.example.rickandmorty.presentation.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.data.constants.initStatusBar
import com.example.rickandmorty.databinding.ActivityProfileBinding
import com.example.rickandmorty.presentation.login.LoginActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var loginPreferences: SharedPreferences
    private lateinit var onBoardingPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding.lifecycleOwner = this
        binding.profileViewModel = viewModel
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initStatusBar()
        initPreferences()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.passHelperText.observe(this) { helperText ->
            binding.containerPass.error =
                if (helperText.isNullOrEmpty()) null else getString(R.string.invalid_pass)
        }

        viewModel.emailHelperText.observe(this) { helperText ->
            binding.containerEmail.error =
                if (helperText.isNullOrEmpty()) null else getString(R.string.invalid_email)
        }

        viewModel.confirmationPassHelperText.observe(this) { helperText ->
            binding.containerConfirmationPass.error =
                if (helperText.isNullOrEmpty()) null else getString(R.string.password_mismatch)
        }
    }

    private fun initListeners() {
        binding.textEditEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                viewModel.validateEmail(binding.textEditEmail.text.toString())
            }
        }

        binding.textEditPass.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                viewModel.validatePassword(binding.textEditPass.text.toString())
            }
        }

        binding.textEditConfirmationPass.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                viewModel.validateConfirmationPassword(binding.textEditConfirmationPass.text.toString())
            }
        }

        binding.buttonSave.setOnClickListener {
            if (viewModel.validateEmail(binding.textEditEmail.text.toString()) && viewModel.validatePassword(binding.textEditPass.text.toString()) && viewModel.validateConfirmationPassword(binding.textEditConfirmationPass.text.toString())) {
                saveCredentials(
                    binding.textEditEmail.text.toString(),
                    binding.textEditPass.text.toString()
                )
                Toast.makeText(this, resources.getString(R.string.done), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, resources.getString(R.string.wrong_data), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun clearLoginPreferences() {
        val loginEditor = loginPreferences.edit()
        loginEditor.clear()
        loginEditor.apply()
    }

    private fun clearOnBoardingPreferences() {
        val onBoardingEditor = onBoardingPreferences.edit()
        onBoardingEditor.clear()
        onBoardingEditor.apply()
    }

    private fun saveCredentials(email: String, password: String) {
        val editor = loginPreferences.edit()
        editor.putString(resources.getString(R.string.email), email)
        editor.putString(resources.getString(R.string.pass), password)
        editor.apply()
    }

    private fun initPreferences() {
        loginPreferences = getSharedPreferences(Constants.preferences, Context.MODE_PRIVATE)
        onBoardingPreferences = getSharedPreferences(Constants.onBoarding, Context.MODE_PRIVATE)

        val email = loginPreferences.getString(resources.getString(R.string.email), "")
        val password = loginPreferences.getString(resources.getString(R.string.pass), "")

        binding.textEditEmail.setText(email)
        binding.textEditPass.setText(password)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_logout -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.confirmation))
                    .setPositiveButton(getString(R.string.logout)) { _, _ ->
                        clearLoginPreferences()
                        clearOnBoardingPreferences()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finishAffinity()
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}