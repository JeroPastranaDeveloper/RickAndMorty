package com.example.rickandmorty.presentation.rickMorty.dateFilter

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.data.constants.Constants
import com.example.rickandmorty.data.constants.initStatusBar
import com.example.rickandmorty.data.model.Episode
import com.example.rickandmorty.databinding.ActivityDateFilterBinding
import com.example.rickandmorty.presentation.rickMorty.adapters.DateFilterAdapter
import com.example.rickandmorty.presentation.rickMorty.episodeDetail.EpisodeDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DateFilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDateFilterBinding
    private lateinit var adapter: DateFilterAdapter
    private lateinit var viewModel: DateFilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[DateFilterViewModel::class.java]

        initViews()
        initAdapter()
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.episodes.observe(this) { episodes ->
            adapter.updateEpisodes(episodes)
        }

        viewModel.error.observe(this) { errorCode: Int ->
            val errorMessage = when (errorCode) {
                Constants.NO_CONNECTION -> getString(R.string.no_connection)
                else -> getString(R.string.error_general)
            }
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.isDateError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.error_date),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val firstDateString = binding.datePickerFirstDate.text.toString()
                val secondDateString = binding.datePickerSecondDate.text.toString()
                val firstDate = parseToDate(firstDateString)
                val secondDate = parseToDate(secondDateString)

                if (firstDate != null && secondDate != null) {
                    viewModel.filterEpisodesByDate(firstDate, secondDate)
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.error_date),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                viewModel.resetState(binding.containerDate.isVisible)
            }
        }

        viewModel.isReset.observe(this) { isReset ->
            with(binding) {
                if (isReset) {
                    containerDate.visibility = View.VISIBLE
                    datePickerFirstDate.visibility = View.GONE
                    datePickerSecondDate.visibility = View.GONE
                    textTitle.visibility = View.GONE
                    buttonAccept.text = resources.getString(R.string.reset)
                } else {
                    containerDate.visibility = View.GONE
                    datePickerFirstDate.visibility = View.VISIBLE
                    datePickerSecondDate.visibility = View.VISIBLE
                    textTitle.visibility = View.VISIBLE
                    buttonAccept.text = resources.getString(R.string.done)
                }
            }
        }
    }

    private fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.getEpisodes()
        binding.containerDate.visibility = View.GONE

        initStatusBar()
    }

    private fun initAdapter() {
        adapter = DateFilterAdapter(
            emptyList(),
            object : DateFilterAdapter.OnEpisodeClick {
                override fun onClick(episode: Episode) {
                    val intent = Intent(this@DateFilterActivity, EpisodeDetailActivity::class.java)
                    intent.putExtra(Constants.id, episode.id)
                    startActivity(intent)
                }
            }
        )
        binding.containerDate.layoutManager = LinearLayoutManager(this)
        binding.containerDate.adapter = adapter
    }

    private fun initListeners() {
        binding.datePickerFirstDate.setOnClickListener {
            pickDate(binding.datePickerFirstDate)
        }

        binding.datePickerSecondDate.setOnClickListener {
            pickDate(binding.datePickerSecondDate)
        }

        binding.buttonAccept.setOnClickListener {
            val firstDate = parseToDate(binding.datePickerFirstDate.text.toString())
            val secondDate = parseToDate(binding.datePickerSecondDate.text.toString())
            val textFirstDate = binding.datePickerFirstDate.text.toString()
            val textSecondDate = binding.datePickerSecondDate.text.toString()

            viewModel.validateDates(firstDate, secondDate, textFirstDate, textSecondDate, resources)
        }
    }

    private fun parseToDate(dateString: String): Date? {
        val format = SimpleDateFormat(Constants.textDate, Locale.getDefault())
        return try {
            format.parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }

    private fun pickDate(datePicker: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            datePicker.text = selectedDate
        }, year, month, day).show()
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