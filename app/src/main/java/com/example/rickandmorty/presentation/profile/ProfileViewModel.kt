package com.example.rickandmorty.presentation.profile

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.R

class ProfileViewModel : ViewModel() {
    private val _emailHelperText = MutableLiveData<String?>()
    val emailHelperText: LiveData<String?>
        get() = _emailHelperText

    private val _passHelperText = MutableLiveData<String?>()
    val passHelperText: LiveData<String?>
        get() = _passHelperText

    private val _confirmationPassHelperText = MutableLiveData<String?>()
    val confirmationPassHelperText: LiveData<String?>
        get() = _confirmationPassHelperText

    private var originalPassword: String? = null

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
            originalPassword = password
            isValid = true
        }
        return isValid
    }

    fun validateConfirmationPassword(password: String) : Boolean {
        var isValid = false
        if (password != originalPassword) {
            _confirmationPassHelperText.value = R.string.password_mismatch.toString()
            isValid = false
        } else {
            _confirmationPassHelperText.value = null
            isValid = true
        }
        return isValid
    }

}