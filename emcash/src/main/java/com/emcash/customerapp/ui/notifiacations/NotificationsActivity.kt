package com.emcash.customerapp.ui.notifiacations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.ui.notifiacations.adapter.NotificationAdapter
import com.emcash.customerapp.ui.notifiacations.adapter.NotificationDetailsAdapter
import com.emcash.customerapp.utils.KEY_BEN_ID
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.LoaderDialog
import com.emcash.customerapp.utils.SCREEN_HOME_RECENT_CONTACTS
import kotlinx.android.synthetic.main.activity_notifiactions.*

class NotificationsActivity : AppCompatActivity(),NotificationDetailsAdapter.NotificationItemClickListener {

    private val viewModel : NotificationsViewModel by viewModels()
    private val loader by lazy{LoaderDialog(this)}
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
            getNotifications(1,50).observe(this@NotificationsActivity, Observer {
                when(it.status){
                    ApiCallStatus.LOADING->loader.showLoader()
                    ApiCallStatus.SUCCESS->{
                        val notifications= it.data?.notifications
                        notifications?.let {
                            rv_notification.adapter = NotificationAdapter(
                                groupNotification(it),this@NotificationsActivity
                            )
                        loader.hideLoader()
                        }
                    }
                    ApiCallStatus.ERROR->{
                        loader.hideLoader()
                        showShortToast(it.errorMessage)
                    }
                }
            })

        }
    }

    override fun onNotificationClick(benId: String) {
        val beneficiaryId= if(benId.isNullOrEmpty()) 0 else benId.toInt()
        openActivity(NewPaymentActivity::class.java) {
            this.putInt(LAUNCH_SOURCE, SCREEN_HOME_RECENT_CONTACTS)
            this.putInt(KEY_BEN_ID,beneficiaryId)
        }
    }
}