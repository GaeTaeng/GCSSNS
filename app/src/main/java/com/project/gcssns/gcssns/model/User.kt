package com.project.gcssns.gcssns.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by i412 on 2018-10-02.
 */

@Parcelize
data class User(var uid: String? = null, var profileImageUrl: String? = null, var userEmail : String? = null, var userName : String? = null, var userStudentId : String? = null, var userMajor : String? = null) : Parcelable