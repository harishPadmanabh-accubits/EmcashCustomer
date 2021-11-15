package com.emcash.customerapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.emcash.customerapp.extensions.FromJson
import com.emcash.customerapp.extensions.fromJson
import com.emcash.customerapp.extensions.toJson
import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountResponse
import com.emcash.customerapp.model.payments.PaymentRequest
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import com.emcash.customerapp.ui.terms.TncStatus
import com.emcash.customerapp.utils.*
import timber.log.Timber
import java.lang.Exception


class SyncManager(val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val prefName = PREF_NAME

    init {
        sharedPreferences = EncryptedSharedPreferences.create(
            prefName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        editor = sharedPreferences.edit()
    }

    var sessionId: String?
        get() = try {
            sharedPreferences.getString(KEY_SESSION_ID, "")
        } catch (e: Exception) {
            null
        }
        set(value) = editor.putString(KEY_SESSION_ID, value).apply()

    var switchAccountData: SwitchAccountResponse.Data?
        get() = try {
            val data = sharedPreferences.getString(KEY_SWITCH_ACCOUNT, "")
            if (data.isNullOrEmpty())
                null
            else
                data.FromJson(SwitchAccountResponse.Data::class.java)
        } catch (e: Exception) {
            null
        }
        set(value) = editor.putString(KEY_SWITCH_ACCOUNT, value?.toJson()).apply()

    var doesUserExist: Boolean
        get() = try {
            sharedPreferences.getBoolean(KEY_USER_EXIST, false)
        } catch (e: Exception) {
            false
        }
        set(value) = editor.putBoolean(KEY_USER_EXIST, value).apply()

    var shouldShowOnboardingScreens: Boolean
        get() = try {
            sharedPreferences.getBoolean(KEY_SHOW_ONBOARDING, true)
        } catch (e: Exception) {
            false
        }
        set(value) = editor.putBoolean(KEY_SHOW_ONBOARDING, value).apply()

    var tncStatus: TncStatus
        get() = try {
            val status = sharedPreferences.getString(KEY_TERMS_ACCEPTED, "")
            status?.fromJson(TncStatus::class.java) ?: TncStatus.NOT_SHOWN
        } catch (e: Exception) {
            TncStatus.NOT_SHOWN
        }
        set(value) = editor.putString(KEY_TERMS_ACCEPTED, value.toString()).apply()

    var profileDetails: ProfileDetailsResponse.Data?
        get() = try {
            val profileData = sharedPreferences.getString(KEY_PROFILE_DATA_CACHE, "")
            if (profileData != null && profileData.isNotEmpty())
                profileData.fromJson(ProfileDetailsResponse.Data::class.java)
            else
                null
        } catch (e: Exception) {
            null
        }
        set(value) = editor.putString(KEY_PROFILE_DATA_CACHE, value?.toJson()).apply()

    var initiatedRefId: String?
        get() = try {
            sharedPreferences.getString(KEY_REF_ID, "")
        } catch (e: Exception) {
            null
        }
        set(value) = editor.putString(KEY_REF_ID, value).apply()

    var fcmToken: String
        get() = try {
            var token = ""
            sharedPreferences.getString(KEY_FCM_TOKEN, "")?.let {
                token = it
            }
            token
        } catch (e: Exception) {
            ""
        }
        set(value) = editor.putString(KEY_FCM_TOKEN, value).apply()

    var isFromEmCashNotification: Boolean
        get() = try {
            sharedPreferences.getBoolean(KEY_IS_FROM_EMCASH_NOTIFICATION, false)
        } catch (e: Exception) {
            false
        }
        set(value) = editor.putBoolean(KEY_IS_FROM_EMCASH_NOTIFICATION, value).apply()


    fun logout() {
        editor.clear().apply()
    }

    var uuid: String?
        get() = try {
            sharedPreferences.getString(KEY_UUID, "")
        } catch (e: Exception) {
            null
        }
        set(value) = editor.putString(KEY_UUID, value).apply()

    var isLogoutPending: Boolean
        get() = try {
            sharedPreferences.getBoolean(KEY_UUID, false)
        } catch (e: Exception) {
            false
        }
        set(value) = editor.putBoolean(KEY_PENDING_LOGOUT, value).apply()

    var recentTransactionsCache: RecentTransactionResponse.Data?
        get() = try {
            val recentTransactions = sharedPreferences.getString(KEY_RECENT_TRANSACTION_CACHE, "")
            if (recentTransactions != null && recentTransactions.isNotEmpty())
                recentTransactions.fromJson(RecentTransactionResponse.Data::class.java)
            else
                null
        } catch (e: Exception) {
            null
        }
    set(value) = editor.putString(KEY_RECENT_TRANSACTION_CACHE, value?.toJson()).apply()


}