package com.emcash.customerapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.emcash.customerapp.extensions.FromJson
import com.emcash.customerapp.extensions.fromJson
import com.emcash.customerapp.extensions.toJson
import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountResponse
import com.emcash.customerapp.ui.terms.TncStatus
import com.emcash.customerapp.utils.*
import java.lang.Exception


class SyncManager(val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val prefName = "emcash_preferences"
    init {
        sharedPreferences = EncryptedSharedPreferences.create(
            prefName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        editor = sharedPreferences.edit()
    }

    var sessionId:String?
    get() =try {
         sharedPreferences.getString(KEY_SESSION_ID,"")
    }catch (e:Exception){
        null
    }
    set(value) = editor.putString(KEY_SESSION_ID,value).apply()

    var switchAccountData:SwitchAccountResponse.Data?
    get() = try {
        val data = sharedPreferences.getString(KEY_SWITCH_ACCOUNT,"")
        if(data.isNullOrEmpty())
            null
        else
            data.FromJson(SwitchAccountResponse.Data::class.java)
    }catch (e:Exception){
        null
    }
    set(value) = editor.putString(KEY_SWITCH_ACCOUNT,value?.toJson()).apply()

    var doesUserExist : Boolean
    get() = try {
        sharedPreferences.getBoolean(KEY_USER_EXIST,false)
    }catch (e:Exception){
        false
    }
    set(value) = editor.putBoolean(KEY_USER_EXIST,value).apply()

    var shouldShowOnboardingScreens:Boolean
        get() = try {
            sharedPreferences.getBoolean(KEY_SHOW_ONBOARDING,true)
        }catch (e:Exception){
            false
        }
        set(value) = editor.putBoolean(KEY_SHOW_ONBOARDING,value).apply()

    var tncStatus:TncStatus
    get() =try {
        val status =  sharedPreferences.getString(KEY_TERMS_ACCEPTED,"")
        status?.fromJson(TncStatus::class.java) ?: TncStatus.NOT_SHOWN
    }catch (e:Exception){
        TncStatus.NOT_SHOWN
    }
    set(value) = editor.putString(KEY_TERMS_ACCEPTED,value.toString()).apply()




}