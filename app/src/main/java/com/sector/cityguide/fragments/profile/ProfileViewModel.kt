package com.sector.cityguide.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sector.cityguide.R
import com.sector.cityguide.models.ProfileMenu

class ProfileViewModel: ViewModel() {
    private var list = MutableLiveData<MutableList<ProfileMenu>>()

    init {
        list = MutableLiveData()
        list.value = setElements()
    }

    fun getElements(): LiveData<MutableList<ProfileMenu>> {
        return list
    }

    private fun setElements(): MutableList<ProfileMenu> {
        val arrayList: MutableList<ProfileMenu> = ArrayList()

        arrayList.add(
            ProfileMenu(
                title = "Избранное",
                description = "Просмотр того, что вам понравилось",
                icon = R.drawable.ic_outline_favorite_light
            )
        )
        arrayList.add(
            ProfileMenu(
                title = "Профиль",
                description = "Измените информацию о себе",
                icon = R.drawable.ic_outline_profile_light
            )
        )
        arrayList.add(
            ProfileMenu(
                title = "Настройки",
                description = "Общее в приложении",
                icon = R.drawable.ic_outline_settings_light
            )
        )

        return arrayList
    }
}