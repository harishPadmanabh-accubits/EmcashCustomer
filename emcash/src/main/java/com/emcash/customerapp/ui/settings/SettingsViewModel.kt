package com.emcash.customerapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.extensions.default

class SettingsViewModel(val app:Application):AndroidViewModel(app) {
    val syncManager = SyncManager(app)
    val profile = syncManager.profileDetails
    val logoutBottomSheetVisibility = MutableLiveData<Boolean>()

}