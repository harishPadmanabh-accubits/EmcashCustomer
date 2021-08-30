package com.emcash.customerapp.ui.notifiacations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.repos.NotificationsRepository
import com.emcash.customerapp.extensions.toFormattedDate
import com.emcash.customerapp.model.notifications.Notification
import com.emcash.customerapp.model.notifications.NotificationResponse
import com.emcash.customerapp.model.notifications.NotificationUiModel
import timber.log.Timber

class NotificationsViewModel(app:Application):AndroidViewModel(app) {
    val repository = NotificationsRepository(app)
    var _notifications = MutableLiveData<ApiMapper<NotificationResponse.Data>>()

    fun  getNotifications(page:Int,limit:Int):LiveData<ApiMapper<NotificationResponse.Data>>{
        _notifications.value = ApiMapper(ApiCallStatus.LOADING, null, null)
        repository.getNotifications(page,limit){
            status, message, result ->
            when(status){
                true->_notifications.value=ApiMapper(ApiCallStatus.SUCCESS, result, null)
                false->_notifications.value= ApiMapper(ApiCallStatus.ERROR,null, message)
            }
        }
        return _notifications

    }


    fun groupNotification(rows: ArrayList<Notification>?): ArrayList<NotificationUiModel> {

        val NotificationActivityList = ArrayList<NotificationUiModel>()  //final processed list
        val accessedDates = ArrayList<String>() //to check if a date is alreaaady accesssed

        rows?.let { allTransactions ->   //check if rows are null
            if (!allTransactions.isNullOrEmpty()) {
                val dates = allTransactions.map {
                    it.updatedAt                                                                 //gets a list of updatedAt from all data using map
                }
                dates.forEach { unformattedDate ->
                    Timber.e("Unformatted date $unformattedDate")
                    val formattedDate = toFormattedDate(unformattedDate)
                    Timber.e("formatted date $formattedDate")

                    if(!accessedDates.contains(formattedDate)){              //check if date already accessed otherwise duplications will occur
                        val groupedActivities = allTransactions.filter { row ->
                            toFormattedDate(row.updatedAt) == formattedDate
                        }                                                     // get all trnasaction under each item in dates list
                        NotificationActivityList.add(
                            NotificationUiModel(
                                formattedDate,
                                groupedActivities
                            )
                        )  //add to custom model
                        accessedDates.add(formattedDate)               //add date to accessDate Array
                    }


                }
            }


        }
        return NotificationActivityList //pass this to adapter
    }


}