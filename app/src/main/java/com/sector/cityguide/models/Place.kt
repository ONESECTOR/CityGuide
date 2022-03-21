package com.sector.cityguide.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val image: String = "",
    val general: String = "",
    val history: String = "",
    val youCanSee: String = ""
) : Parcelable