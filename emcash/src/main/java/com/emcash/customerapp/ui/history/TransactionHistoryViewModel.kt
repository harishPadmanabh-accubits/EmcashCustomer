package com.emcash.customerapp.ui.history

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.*
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.data.repos.TransactionHistoryRepository
import com.emcash.customerapp.enums.TransactionHistoryScreens
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.extensions.toFormattedDate
import com.emcash.customerapp.model.dummyTransactionHistoryData
import com.emcash.customerapp.model.transactions.*
import com.emcash.customerapp.ui.history.adapters.HistoryPagingSource
import com.emcash.customerapp.utils.DEFAULT_PAGE_CONFIG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class TransactionHistoryViewModel(val app: Application) : AndroidViewModel(app) {

    val api = EmCashApiManager(app).api
    val filter = MutableLiveData<HistoryFilter>().default(HistoryFilter())
    var scope: CoroutineScope? = null
    var currentScreen = TransactionHistoryScreens.ALL
    var isFilterByDate = false

    fun sendStatus(s_status: String) {
        isFilterByDate = false
        when (currentScreen) {
            TransactionHistoryScreens.ALL -> filter.value =
                HistoryFilter(status = s_status, mode = "0")
            TransactionHistoryScreens.INBOUND -> filter.value =
                HistoryFilter(status = s_status, mode = "1")
            TransactionHistoryScreens.OUTBOUND -> filter.value =
                HistoryFilter(status = s_status, mode = "2")
        }

    }

    fun sendDate(s_date: ArrayList<String>) {
        isFilterByDate =true
        when (currentScreen) {
            TransactionHistoryScreens.ALL -> filter.value = HistoryFilter(
                startDate = s_date.first(),
                endDate = s_date.last(),
                mode = "0"
            )

            TransactionHistoryScreens.INBOUND -> filter.value = HistoryFilter(
                startDate = s_date.first(),
                endDate = s_date.last(),
                mode = "1"
            )

            TransactionHistoryScreens.OUTBOUND -> filter.value = HistoryFilter(
                startDate = s_date.first(),
                endDate = s_date.last(),
                mode = "2"
            )
        }

        Timber.e("date start ${s_date[0]} end ${s_date.last()}")
    }


    val pagedTransactions: LiveData<PagingData<TransactionHistoryGroupResponse.Data.TransactionGroup>> =
        Transformations.switchMap(filter) {
            Pager(PagingConfig(DEFAULT_PAGE_CONFIG)) {
                val filterValue = filter.value
                filterValue.let {

                }
                HistoryPagingSource(
                    api,
                    filter.value?.mode ?: "0",
                    filter.value?.startDate ?: "",
                    filter.value?.endDate ?: "",
                    filter.value?.status ?: "",
                    filter.value?.type ?: ""
                )
            }.liveData.cachedIn(scope ?: viewModelScope)
        }

}
