package com.emcash.customerapp.ui.notifications.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.toFormattedTime
import com.emcash.customerapp.extensions.toLocalTime
import com.emcash.customerapp.model.notifications.Notification
import com.emcash.customerapp.utils.EmCashUtils
import com.emcash.customerapp.utils.EmCashUtils.NOTIFICATION_STATUS_COMPLETE_REGISTRATION
import com.emcash.customerapp.utils.EmCashUtils.NOTIFICATION_STATUS_PENDING
import com.emcash.customerapp.utils.EmCashUtils.NOTIFICATION_STATUS_REJECTED
import com.emcash.customerapp.utils.EmCashUtils.NOTIFICATION_STATUS_REJECTED_FROM_MERCHANT
import com.emcash.customerapp.utils.EmCashUtils.NOTIFICATION_STATUS_SUCCESS
import kotlinx.android.synthetic.main.item_notification_details.view.*

class NotificationDetailsAdapter(
    val data: List<Notification>,
   val listener: NotificationItemClickListener
) :
    RecyclerView.Adapter<NotificationDetailsAdapter.ViewHolder>() {
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationDetailsAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification_details, parent, false)
        return NotificationDetailsAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NotificationDetailsAdapter.ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_notification.text = data[position].message
            tv_time.text = data[position].updatedAt.toLocalTime()
            when (data[position].type) {
                NOTIFICATION_STATUS_PENDING -> {//pending
                    iv_point.setColorFilter(ContextCompat.getColor(context, R.color.orange))
                }
                NOTIFICATION_STATUS_SUCCESS -> {//success
                    iv_point.setColorFilter(ContextCompat.getColor(context, R.color.green))
                }
                NOTIFICATION_STATUS_REJECTED -> {//rejected
                    iv_point.setColorFilter(ContextCompat.getColor(context, R.color.red))
                }
                NOTIFICATION_STATUS_COMPLETE_REGISTRATION -> {//registration completed
                    iv_point.setColorFilter(ContextCompat.getColor(context, R.color.app_sky_blue));
                }
                NOTIFICATION_STATUS_REJECTED_FROM_MERCHANT -> {//rejected from merchant side
                    iv_point.setColorFilter(ContextCompat.getColor(context, R.color.red));
                }
            }

            setOnClickListener {
                listener.onNotificationClick(data[position].contactUserId,data[position].id)
            }
        }
    }

}

interface NotificationItemClickListener{
    fun onNotificationClick(benId:String,notificationId:String)
}