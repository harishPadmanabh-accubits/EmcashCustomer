package com.emcash.customerapp.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.notifications.NotificationUiModel
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.ui.notifications.adapter.NotificationAdapter
import com.emcash.customerapp.ui.notifications.adapter.NotificationDetailsAdapter
import com.emcash.customerapp.ui.notifications.adapter.NotificationItemClickListener
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.activity_notifiactions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsActivity : AppCompatActivity(), NotificationItemClickListener {

    private val viewModel: NotificationsViewModel by viewModels()
    private val loader by lazy { LoaderDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifiactions)
        iv_back.setOnClickListener {
            onBackPressed()
        }

        observe()
    }

    private fun observe() {
        viewModel.apply {
            getNotifications(1, 100).observe(this@NotificationsActivity, Observer {
                when (it.status) {
                    ApiCallStatus.LOADING -> loader.showLoader()
                    ApiCallStatus.SUCCESS -> {
                        val notifications = it.data?.notifications
                        notifications?.let {
                            var groupedNotifications: ArrayList<NotificationUiModel>
                            lifecycleScope.launch(Dispatchers.IO) {
                                groupedNotifications =
                                    groupNotification(ArrayList(it.filter { item ->
                                        item.isActive
                                    }))
                                withContext(Dispatchers.Main) {
                                    if (groupedNotifications.isNotEmpty())
                                        rv_notification.adapter = NotificationAdapter(
                                            groupedNotifications, this@NotificationsActivity
                                        )
                                    else
                                        tv_empty_notification.show()
                                    loader.hideLoader()
                                }
                            }
                        }
                    }
                    ApiCallStatus.ERROR -> {
                        loader.hideLoader()
                        showShortToast(it.errorMessage)
                    }
                }
            })

        }
    }

    override fun onNotificationClick(benId: String, notificationId: String) {
        viewModel.onNotificationClick(notificationId)
        val beneficiaryId = if (benId.isNullOrEmpty()) 0 else benId.toInt()
        openActivity(NewPaymentActivity::class.java) {
            this.putInt(LAUNCH_SOURCE, SCREEN_NOTIFICATIONS)
            this.putInt(LAUNCH_DESTINATION, SCREEN_CHAT)
            this.putInt(KEY_BEN_ID, beneficiaryId)
        }
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        openActivity(HomeActivity::class.java)
        finish()
    }
}