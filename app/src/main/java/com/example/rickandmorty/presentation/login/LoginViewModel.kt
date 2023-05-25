package com.example.rickandmorty.presentation.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.R

class LoginViewModel : ViewModel() {
    private val _emailHelperText = MutableLiveData<String?>()
    val emailHelperText: LiveData<String?>
        get() = _emailHelperText

    private val _passHelperText = MutableLiveData<String?>()
    val passHelperText: LiveData<String?>
        get() = _passHelperText

    fun validateEmail(email: String) : Boolean {
        var isValid = false
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()) {
            _emailHelperText.value = R.string.invalid_email.toString()
            isValid = false
        } else {
            _emailHelperText.value = null
            isValid = true
        }
        return isValid
    }

    fun validatePassword(password: String) : Boolean {
        var isValid = false
        if (password.length < 8 || !password.matches(".*[0-9].*".toRegex()) || password.isEmpty()) {
            _passHelperText.value = R.string.invalid_pass.toString()
            isValid = false
        } else {
            _passHelperText.value = null
            isValid = true
        }
        return isValid
    }
}