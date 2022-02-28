package com.sector.cityguide.fragments.home.viewmodel

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

    fun getGreetingMessage() {
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        _greeting.value = when(timeOfDay) {
            in 0..11 -> R.string.good_morning
            in 12..15 -> R.string.good_afternoon
            in 16..20 -> R.string.good_evening
            in 21..23 -> R.string.good_night
            else -> R.string.hello
        }
    }
}