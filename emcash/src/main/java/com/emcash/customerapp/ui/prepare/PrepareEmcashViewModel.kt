package com.emcash.customerapp.ui.prepare

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.repos.AuthRepository
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.model.profile.ProfileDetailsResponse

class PrepareEmcashViewModel(val app : Application):AndroidViewModel(app) {

    var _bottomSheetVisiblity = MutableLiveData<Boolean>()
    val bottomSheetVisibility : LiveData<Boolean> get() = _bottomSheetVisiblity

    val authRepository = AuthRepository(app)
    val syncManager = SyncManager(app)

    var userName = ""
    var dpUrl=""

    val profileData:LiveData<ApiMapper<ProfileDetailsResponse.Data>> get()=authRepository.getProfileDetails()
    var profileDetails:ProfileDetailsResponse.Data?=null


}