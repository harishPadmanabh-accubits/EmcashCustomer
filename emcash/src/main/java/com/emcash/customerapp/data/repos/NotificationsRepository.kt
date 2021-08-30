package com.emcash.customerapp.data.repos

import android.content.Context
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.model.notifications.NotificationResponse

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

}