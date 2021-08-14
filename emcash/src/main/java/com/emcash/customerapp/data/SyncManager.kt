package com.emcash.customerapp.data

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


class SyncManager(val context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val prefName = "emcash_preferences"
    init {
        pref = EncryptedSharedPreferences.create(
            prefName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        editor = pref.edit()
    }
}