package com.project.gcssns.gcssns.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeFeedComment(var id: String? = null, var HomeFeedId : String? = null, var text : String? = null, var user : User? = null, var timestamp : Long? = null) : Parcelable