package com.emcash.customerapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.emcash.customerapp.data.SyncManager

class SettingsViewModel(val app:Application):AndroidViewModel(app) {
    val syncManager = SyncManager(app)
    val profile = syncManager.profileDetails

}