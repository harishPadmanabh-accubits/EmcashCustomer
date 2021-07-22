package com.emcash.customerapp.model

import com.emcash.customerapp.R
import com.emcash.customerapp.utils.LevelProfileImageView

data class DummyUserData(
    var name:String,
    var image : Int?,
    var level:LevelProfileImageView.UserProfileLevel = LevelProfileImageView.UserProfileLevel.NONE

)
val users = listOf<DummyUserData>(
    DummyUserData(
        "Mary ",
        R.drawable.sample_dp,
        LevelProfileImageView.UserProfileLevel.GREEN
    ),
    DummyUserData(
        "Alan",
        R.drawable.sample_img,
        LevelProfileImageView.UserProfileLevel.RED
    ),
    DummyUserData(
        "Milan",
        R.drawable.sample_dp,
        LevelProfileImageView.UserProfileLevel.NONE
    ),
    DummyUserData(
        "Anise",
        R.drawable.sample_img,
        LevelProfileImageView.UserProfileLevel.RED
    ),
    DummyUserData(
        "Fijui",
        R.drawable.sample_dp,
        LevelProfileImageView.UserProfileLevel.NONE
    ),
    DummyUserData(
        "Lewis",
        R.drawable.sample_img,
        LevelProfileImageView.UserProfileLevel.YELLOW
    ),
    DummyUserData(
        "Kamal",
        null,
        LevelProfileImageView.UserProfileLevel.GREEN
    ), DummyUserData(
        "Michel",
        R.drawable.sample_img,
        LevelProfileImageView.UserProfileLevel.YELLOW
    )
)


