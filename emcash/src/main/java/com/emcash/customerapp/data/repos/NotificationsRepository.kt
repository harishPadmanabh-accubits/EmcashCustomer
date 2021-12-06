package com.emcash.customerapp.data.repos

import android.content.Context
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.model.notifications.NotificationResponse
import timber.log.Timber
import java.lang.Exception

class NotificationsRepository(private val context: Context) {
    private val syncManager = SyncManager(context)
    private val api = EmCashApiManager(context).api

    fun getNotifications(
        page: Int, limit: Int,
        onApiCallback: (status: Boolean, message: String?, result: NotificationResponse.Data?) -> Unit
    ) {
        api.getNotifications(
            page, limit
        ).awaitResponse(onSuccess = {
            onApiCallback(true,null,it?.data)
        },onFailure = {
            onApiCallback(false, it, null)
        })
    }

    suspend fun onNotificationClick(id:String){
        try {
            api.onNotificationItemClick(id)
        }catch (e:Exception){
            e.printStackTrace()
            Timber.e("onNotificationClickExc $e")
        }
    }

}