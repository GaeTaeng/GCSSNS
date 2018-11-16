package com.project.gcssns.gcssns.model

data class HomeFeed(var user : User? = null, var text : String? = null, var picutreImageUrls : ArrayList<String>? = null, var timestamp : Long? = null)