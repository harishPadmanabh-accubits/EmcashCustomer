package com.emcash.customerapp.ui.wallet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.model.wallet.topup.WalletTopupRequest
import com.emcash.customerapp.utils.DEFAULT_PAGE_CONFIG

class WalletViewModel(val app:Application):AndroidViewModel(app) {
    val api = EmCashApiManager(app).api

    val homeRepository = HomeRepository(app)

    val walletActivities = Pager(PagingConfig(DEFAULT_PAGE_CONFIG)){
        WalletActivityPagingSource(api)
    }.flow.cachedIn(viewModelScope)

    fun  syncProfileFromServer():LiveData<ApiMapper<ProfileDetailsResponse.Data>>{
        val _profile = MutableLiveData<ApiMapper<ProfileDetailsResponse.Data>>()
        _profile.value = ApiMapper(ApiCallStatus.LOADING,null,null)
        homeRepository.getProfile { status, response, error ->
            when(status){
                true-> _profile.value= ApiMapper(ApiCallStatus.SUCCESS,response,null)
                false->_profile.value= ApiMapper(ApiCallStatus.ERROR,null,error)
            }
        }
        return _profile
    }


}