package com.emcash.customerapp.model

import com.emcash.customerapp.utils.LevelProfileImageView

data class DummyUserData(
    var name:String,
    var image : Int?,
    var level:LevelProfileImageView.UserProfileLevel = LevelProfileImageView.UserProfileLevel.NONE

)

