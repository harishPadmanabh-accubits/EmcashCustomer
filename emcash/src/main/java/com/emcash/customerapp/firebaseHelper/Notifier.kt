package com.emcash.customerapp.firebaseHelper

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.emcash.customerapp.R
import com.emcash.customerapp.model.RemoteData
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.utils.IS_FROM_DEEPLINK
import com.emcash.customerapp.utils.KEY_DEEPLINK
import com.emcash.customerapp.utils.KEY_TYPE
import timber.log.Timber

object Notifier {
    fun handleEmCashNotification(
        remoteData: RemoteData,
        context: Context,
        launchClassName:String
    ) {
        Timber.e("fcm $remoteData")
        if (remoteData.title != null && remoteData.message != null) {
            showNotification(remoteData,context,launchClassName)
        } else {
            Timber.e("Arrived null on notify")
        }

    }

    private fun showNotification(
        remoteData: RemoteData,
        context: Context,
        launchClassName:String
    ) {
        val channelId = "com.emcash.customer.notifications"
        val channeldesc = "Emcash customer Notification Channel"
        val builder: Notification.Builder

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, Class.forName(launchClassName)).apply {
            putExtra(KEY_DEEPLINK, remoteData.deeplink?.toString())
            putExtra(KEY_TYPE, type?.toString())
            putExtra(IS_FROM_DEEPLINK, true)
        }

        val stackBuilder = TaskStackBuilder.create(context)
            .also {
            it.addNextIntent(notificationIntent)
        }

        val resultPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channeldesc,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelId)
                .setContentTitle(remoteData.title)
                .setContentText(remoteData.message)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.icon_notify)
                .setBadgeIconType(R.drawable.icon_notify)
        } else {

            builder = Notification.Builder(context)
                .setContentTitle(remoteData.title)
                .setContentText(remoteData.message)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.icon_notify)

        }
        notificationManager.notify(10001, builder.build())

    }


}