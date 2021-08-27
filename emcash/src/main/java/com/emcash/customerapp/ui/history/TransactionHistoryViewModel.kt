package com.emcash.customerapp.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.repos.TransactionHistoryRepository
import com.emcash.customerapp.extensions.toFormattedDate
import com.emcash.customerapp.model.dummyTransactionHistoryData
import com.emcash.customerapp.model.transactions.Transaction
import com.emcash.customerapp.model.transactions.TransactionHistoryGroupUIModel
import com.emcash.customerapp.model.transactions.TransactionHistoryResponse
import timber.log.Timber

class TransactionHistoryViewModel(val app: Application):AndroidViewModel(app){

    val type = MutableLiveData<String>()
    val status = MutableLiveData<String>()
    val date = MutableLiveData<ArrayList<String>>()
    val endDate = MutableLiveData<String>()

    fun sendType(s_type: String) {
        type.value = s_type
    }

    fun sendStatus(s_status: String) {
        status.value = s_status
    }

    fun sendDate(s_date: ArrayList<String>) {
        date.value = s_date
    }

    var alltransactionHistoryResponse =
        MutableLiveData<ApiMapper<TransactionHistoryResponse.Data>>()
    var inBoundtransactionHistoryResponse =
        MutableLiveData<ApiMapper<TransactionHistoryResponse.Data>>()
    var outBoundtransactionHistoryResponse =
        MutableLiveData<ApiMapper<TransactionHistoryResponse.Data>>()

    val repository = TransactionHistoryRepository(app)

    fun getAllTransactions(
        mode: String,
        type: String,
        status: String,
        startDate: String,
        endDate: String
    ) {
        alltransactionHistoryResponse.value = ApiMapper(ApiCallStatus.LOADING, null, null)

        repository.getAllTransactionHistory(
            mode,
            type,
            status,
            startDate,
            endDate
        ) { status, message, result ->
            when (status) {
                true -> {
                    alltransactionHistoryResponse.value =
                        ApiMapper(ApiCallStatus.SUCCESS, result, null)
                }
                false -> {
                    alltransactionHistoryResponse.value =
                        ApiMapper(ApiCallStatus.ERROR, null, message)

                }
            }
        }
    }


    fun getInBoundTransactions(
        mode: String,
        type: String,
        status: String,
        startDate: String,
        endDate: String
    ) {
        inBoundtransactionHistoryResponse.value = ApiMapper(ApiCallStatus.LOADING, null, null)

        repository.getInboundTransaction(
            mode,
            type,
            status,
            startDate,
            endDate
        ) { status, message, result ->
            when (status) {
                true -> {
                    inBoundtransactionHistoryResponse.value =
                        ApiMapper(ApiCallStatus.SUCCESS, result, null)
                }
                false -> {
                    inBoundtransactionHistoryResponse.value =
                        ApiMapper(ApiCallStatus.ERROR, null, message)

                }
            }
        }
    }

    fun getOutBoundTransactions(
        mode: String,
        type: String,
        status: String,
        startDate: String,
        endDate: String
    ) {
        outBoundtransactionHistoryResponse.value = ApiMapper(ApiCallStatus.LOADING, null, null)

        repository.getOutboundTransaction(
            mode,
            type,
            status,
            startDate,
            endDate
        ) { status, message, result ->
            when (status) {
                true -> {
                    outBoundtransactionHistoryResponse.value =
                        ApiMapper(ApiCallStatus.SUCCESS, result, null)
                }
                false -> {
                    outBoundtransactionHistoryResponse.value =
                        ApiMapper(ApiCallStatus.ERROR, null, message)

                }
            }
        }
    }

    fun groupTransactionActivitiesByDate(rows: ArrayList<Transaction>?): ArrayList<TransactionHistoryGroupUIModel> {

        val finalActivityList =
            ArrayList<TransactionHistoryGroupUIModel>()  //final processed list
        val accessedDates = ArrayList<String>() //to check if a date is already accesssed

        rows?.let { allTransactions ->   //check if rows are null
            if (!allTransactions.isNullOrEmpty()) {
                val dates = allTransactions.map {
                    it.updatedAt                                                                 //gets a list of updatedAt from all data using map
                }
                dates.forEach { unformattedDate ->
                    Timber.e("Unformatted date $unformattedDate")
                    val formattedDate = toFormattedDate(unformattedDate)
                    Timber.e("formatted date $formattedDate")

                    if (!accessedDates.contains(formattedDate)) {              //check if date already accessed otherwise duplications will occur
                        val groupedActivities = allTransactions.filter { row ->
                            toFormattedDate(row.updatedAt) == formattedDate
                        }                                                     // get all trnasaction under each item in dates list
                        finalActivityList.add(
                            TransactionHistoryGroupUIModel(
                                formattedDate,
                                groupedActivities
                            )
                        )  //add to custom model
                        accessedDates.add(formattedDate)               //add date to accessDate Array
                    }


                }
            }


        }
        return finalActivityList //pass this to adapter
    }

}
