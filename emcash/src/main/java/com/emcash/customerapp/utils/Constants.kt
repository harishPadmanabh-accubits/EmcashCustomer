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
const val KEY_PROFILE_DATA_CACHE = "profile_data"
const val KEY_TOPUP_AMOUNT = "topup"
const val KEY_TOPUP_DESC = "topup_desc"
const val KEY_SELECTED_CONTACT = "selected_contact"
const val KEY_REF_ID = "ref_id"
const val KEY_BEN_ID = "ben_id"
const val KEY_TRANSACTION_TYPE = "trans_type"
const val kEY_PENDING_REQUEST = "pending_request"
const val KEY_IS_FROM_QR = "is_from_qr"
const val KEY_QR_DATA = "data_from_qr"
const val KEY_USER_ID_FROM_DEEPLINK = "deep_user"
const val KEY_FCM_TOKEN="fcm"

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
const val SCREEN_CONTACTS = 208

const val KEY_DEEPLINK = "deeplink"
const val KEY_TYPE = " notify_type"
const val IS_FROM_DEEPLINK = "is_from_deeplink"

/*
    Contacts Page Items
 */
const val ITEM_RECENT_CONTACTS = 1000
const val ITEM_ALL_CONTACTS = 1001


const val BASE_URL: String = "https://emcash-api-dev.devtomaster.com/"
const val IMAGE_BASE_URL: String = "https://stemcashmerchantdocstest.blob.core.windows.net"

const val TYPE_TRANSFER = "Transfer"
const val TYPE_REQUEST = "Request"

const val DEFAULT_PAGE_CONFIG = 3

