package com.project.gcssns.gcssns.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeFeed(var id : String? = null, var user : User? = null, var text : String? = null, var picutreImageUrls : ArrayList<String>? = null, var timestamp : Long? = null) : Parcelable