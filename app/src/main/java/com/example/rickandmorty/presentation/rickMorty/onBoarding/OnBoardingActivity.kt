package com.example.rickandmorty.presentation.rickMorty.onBoarding

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.databinding.ActivityOnBoardingBinding
import com.example.rickandmorty.presentation.adapters.TabsPageAdapter
import com.example.rickandmorty.presentation.main.MainActivity
import com.google.android.material.tabs.TabLayout

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_Login)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)

        initViews()
        checkFirstTime()
        setContentView(binding.root)
        initPager()
        initTabLayout()
    }

    private fun initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.background_text)
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.background_text)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun checkFirstTime() {
        if (!isFirstTime()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
    }

    private fun initTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun initPager() {
        binding.viewPager.currentItem = 0
        val adapter = TabsPageAdapter(this, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter
        binding.indicator.setViewPager(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun isFirstTime(): Boolean {
        val preferences = getSharedPreferences(Constants.onBoarding, Context.MODE_PRIVATE)
        return preferences.getBoolean(Constants.firstTime, true)
    }
}
