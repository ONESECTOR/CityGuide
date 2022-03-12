package com.sector.cityguide.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Profile(
    val name: String = "",
    val phone: String = "",
    val uid: String = ""
): Parcelable