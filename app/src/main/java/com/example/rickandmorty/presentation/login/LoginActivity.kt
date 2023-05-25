package com.example.rickandmorty.presentation.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.databinding.ActivityLoginBinding
import com.example.rickandmorty.presentation.rickMorty.onBoarding.OnBoardingActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setTheme(R.style.Base_Theme_Login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.lifecycleOwner = this
        binding.loginViewModel = viewModel
        initView()
        initPreferences()
        initListeners()
        initObservers()
    }

    private fun initView() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white_black)
    }

    private fun initPreferences() {
        sharedPreferences = getSharedPreferences(Constants.preferences, Context.MODE_PRIVATE)

        val email = sharedPreferences.getString(resources.getString(R.string.email), null)
        val password = sharedPreferences.getString(resources.getString(R.string.pass), null)

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }
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

        binding.buttonLogin.setOnClickListener {
            if (viewModel.validateEmail(binding.textEditEmail.text.toString()) && viewModel.validatePassword(
                    binding.textEditPass.text.toString()
                )
            ) {
                saveCredentials(binding.textEditEmail.text.toString(), binding.textEditPass.text.toString())
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, resources.getString(R.string.wrong_data), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Preferencias van al domain y lo guarda en el datasource
    private fun saveCredentials(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(resources.getString(R.string.email), email)
        editor.putString(resources.getString(R.string.pass), password)
        editor.putBoolean(Constants.logged, true)
        editor.apply()
    }
}