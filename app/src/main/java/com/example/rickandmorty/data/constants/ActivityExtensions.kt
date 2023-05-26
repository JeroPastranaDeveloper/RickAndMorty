package com.example.rickandmorty.data.constants

import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rickandmorty.R

fun AppCompatActivity.initStatusBar() {
    val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    window.statusBarColor = ContextCompat.getColor(this, R.color.white_black)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.windowInsetsController?.setSystemBarsAppearance(
            if (isDarkTheme) 0 else WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}