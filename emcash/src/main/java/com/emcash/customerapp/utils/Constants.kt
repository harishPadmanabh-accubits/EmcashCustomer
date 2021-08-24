package com.emcash.customerapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.MasterKeys

const val RC_CAMERA_PERM = 123
const val RC_LOCATION_PERM = 124
const val RC_SMS_PERM = 125
const val RC_CONTACTS_PERM = 126

/**
 * Intent Keys
 */
const val LAUNCH_SOURCE = "launch_source"
const val LAUNCH_DESTINATION = "launch_destination"
const val KEY_SESSION_ID = "session_id"
const val KEY_SWITCH_ACCOUNT = "switch_account"
const val KEY_USER_EXIST = "user_exist"
const val KEY_SHOW_ONBOARDING = "show_onboarding"
const val KEY_TERMS_ACCEPTED = "tnc_status"
const val KEY_PROFILE_DATA_CACHE =  "profile_data"
const val KEY_TOPUP_AMOUNT = "topup"
const val KEY_TOPUP_DESC = "topup_desc"
const val KEY_SELECTED_CONTACT = "selected_contact"
const val KEY_REF_IF = "ref_id"


/**
 * Screen Keys
 */
const val SCREEN_TRANSFER = 200
const val SCREEN_HOME_RECENT_CONTACTS = 201
const val SCREEN_HOME = 202
const val SCREEN_WALLET = 203
const val INTRO_SCREEN = 204
const val SCREEN_SETTINGS = 205
const val SCREEN_RECEIPT = 206
const val SCREEN_CHAT = 207

/*
    Contacts Page Items
 */
const val ITEM_RECENT_CONTACTS = 1000
const val ITEM_ALL_CONTACTS = 1001


const val BASE_URL: String = "https://emcash-api-dev.devtomaster.com/"

