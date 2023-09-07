package com.krishna.foodwave.User.model

import android.net.Uri

data class User(
    var name:String?="",
    var phoneNumber:String?="",
    var Email:String?= "",
    var password:String?= "",
    var profilePhoto: String = "",
)