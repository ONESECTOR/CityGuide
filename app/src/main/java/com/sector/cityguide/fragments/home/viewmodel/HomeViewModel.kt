package com.sector.cityguide.fragments.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sector.cityguide.R
import java.util.*

class HomeViewModel: ViewModel() {
    private val _greeting = MutableLiveData<Int>()
    fun greetingMessage(): LiveData<Int> {
        return _greeting
    }

    init {
        getGreetingMessage()
    }

    private fun getGreetingMessage() {
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)
        Log.d("Time", timeOfDay.toString())

        _greeting.value = when(timeOfDay) {
            in 6..11 -> R.string.good_morning
            in 12..17 -> R.string.good_afternoon
            in 18..21 -> R.string.good_evening
            in 22..23 -> R.string.good_night
            in 0..5 -> R.string.good_night
            else -> R.string.hello
        }
    }
}