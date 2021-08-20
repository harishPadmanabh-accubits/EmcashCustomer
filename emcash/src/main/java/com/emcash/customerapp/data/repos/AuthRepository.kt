package com.emcash.customerapp.data.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountRequest
import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountResponse
import com.emcash.customerapp.model.auth.userExists.UserExistCheckRequest
import com.emcash.customerapp.model.auth.userExists.UserExistCheckResponse
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.model.tnc.TncResponse

class AuthRepository(val context: Context) {
    private val syncManager = SyncManager(context)
    private val api = EmCashApiManager(context).api

    fun performSwitchAccount(switchAccountRequest: SwitchAccountRequest,onApiCallBack:(status:Boolean,response:SwitchAccountResponse.Data?,error:String?)->Unit):LiveData<ApiMapper<SwitchAccountResponse>>{
        val switchData = MutableLiveData<ApiMapper<SwitchAccountResponse>>()
        switchData.value = ApiMapper(ApiCallStatus.LOADING,null,null)
        api.switchAccount(switchAccountRequest).awaitResponse(onSuccess = {response->
            val data = response?.data
            syncManager.switchAccountData = data        //cache response
            syncManager.sessionId = data?.sessionId    //store session id
            switchData.value = ApiMapper(ApiCallStatus.SUCCESS,response,null)
            onApiCallBack(true,response?.data,null)
        },onFailure = {error->
            switchData.value = ApiMapper(ApiCallStatus.ERROR,null,error)
            onApiCallBack(false,null,error)
        })
        return switchData
    }

    fun performUserExistCheck(userExistCheckRequest: UserExistCheckRequest,onApiCallBack:(status:Boolean,response:Boolean?,error:String?)->Unit):LiveData<ApiMapper<UserExistCheckResponse.Data>>{
        val statusData = MutableLiveData<ApiMapper<UserExistCheckResponse.Data>>()
        statusData.value = ApiMapper(ApiCallStatus.LOADING,null,null)
        api.checkIfUserExist(userExistCheckRequest).awaitResponse(onSuccess = {response->
            val status = response?.data?.isExists ?:false
            syncManager.doesUserExist = status
            statusData.value = ApiMapper(ApiCallStatus.SUCCESS,response?.data,null)
            onApiCallBack(true,status,null)
        },onFailure = {error->
            statusData.value = ApiMapper(ApiCallStatus.ERROR,null,error)
            onApiCallBack(true,null,null)
        })
        return statusData
    }

    val _profileData = MutableLiveData<ApiMapper<ProfileDetailsResponse.Data>>()
    fun getProfileDetails():MutableLiveData<ApiMapper<ProfileDetailsResponse.Data>>{
        _profileData.value = ApiMapper(ApiCallStatus.LOADING,null,null)
        api.getProfileDetails().awaitResponse(onSuccess = {response->
            val profileDetails = response?.data
            profileDetails?.let{
                syncManager.profileDetails = it
                _profileData.value = ApiMapper(ApiCallStatus.SUCCESS,profileDetails,null)
            }
        },onFailure = {error->
            _profileData.value = ApiMapper(ApiCallStatus.ERROR,null,error)
        })
        return _profileData
    }


    var _tncData = MutableLiveData<ApiMapper<String>>()
    fun getTnc(){
        _tncData.value = ApiMapper(ApiCallStatus.LOADING,null,null)
        api.getTnc().awaitResponse(onSuccess = {response->
            val tnc = response?.data
            _tncData.value = ApiMapper(ApiCallStatus.SUCCESS,tnc,null)
        },onFailure = {error->
            _tncData.value = ApiMapper(ApiCallStatus.ERROR,null,error)
        })
    }
}