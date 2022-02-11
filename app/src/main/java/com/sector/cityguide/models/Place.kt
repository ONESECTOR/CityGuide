package com.sector.cityguide.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val name: String = "",
    val description: String = "",
    val location: String = "",
    val image: String = ""
): Parcelable