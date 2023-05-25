package com.example.rickandmorty.presentation.boardingFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.databinding.FragmentProfileBinding
import com.example.rickandmorty.presentation.main.MainActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.buttonStart.setOnClickListener {
            val preferences = requireActivity().getSharedPreferences(Constants.onBoarding, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putBoolean(Constants.firstTime, false)
            editor.apply()

            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }
    }
}