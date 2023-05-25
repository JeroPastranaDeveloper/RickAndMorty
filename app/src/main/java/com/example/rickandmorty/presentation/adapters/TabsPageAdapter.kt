package com.example.rickandmorty.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rickandmorty.presentation.boardingFragments.ProfileFragment
import com.example.rickandmorty.presentation.boardingFragments.ShowEpisodesInfoFragment
import com.example.rickandmorty.presentation.boardingFragments.ShowCharactersInfoFragment
import com.example.rickandmorty.presentation.rickMorty.onBoarding.OnBoardingActivity

class TabsPageAdapter (activity: OnBoardingActivity, private val tabCount: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ShowCharactersInfoFragment()
            1 -> ShowEpisodesInfoFragment()
            2 -> ProfileFragment()
            else -> ShowCharactersInfoFragment()
        }
    }
}