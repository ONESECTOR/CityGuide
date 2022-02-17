package com.sector.cityguide.fragments.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sector.cityguide.R
import com.sector.cityguide.models.ProfileMenu

class ProfileViewModel: ViewModel() {
    private val list = MutableLiveData<MutableList<ProfileMenu>>()

    init {
        list.value = setMenu()
    }

    fun getMenuElements(): LiveData<MutableList<ProfileMenu>> {
        return list
    }

    private fun setMenu(): MutableList<ProfileMenu> {
        return arrayListOf(
            ProfileMenu(
                title = "Избранное",
                description = "Просмотр того, что вам понравилось",
                icon = R.drawable.ic_outline_favorite_light
            ),
            ProfileMenu(
                title = "Профиль",
                description = "Измените информацию о себе",
                icon = R.drawable.ic_outline_profile_light
            )
        )
    }
}