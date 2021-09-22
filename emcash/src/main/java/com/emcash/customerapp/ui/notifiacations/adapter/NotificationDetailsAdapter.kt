package com.emcash.customerapp.ui.notifiacations.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.toFormattedTime
import com.emcash.customerapp.model.notifications.Notification
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
            tv_time.text = toFormattedTime(data[position].createdAt)
            var type = data[position].type
            ll_notifications.setOnClickListener {
                if (type != 5 || type != 6) {
//                    var bundle = bundleOf(
//                        KEY_USERID to data[position].contactUserId
//                    )
//
//                    findNavController().navigate(
//                        R.id.action_notificationsFragment_to_paymentChatHistoryFragment,
//                        bundle
//                    )


                }

            }
            if (type == 1) {//pending
                iv_point.setColorFilter(ContextCompat.getColor(context, R.color.orange));
            } else if (type == 2) {//success
                iv_point.setColorFilter(ContextCompat.getColor(context, R.color.green));
            } else if (type == 3) {//rejected
                iv_point.setColorFilter(ContextCompat.getColor(context, R.color.red));
            } else if (type == 6) {//registration completed
                iv_point.setColorFilter(ContextCompat.getColor(context, R.color.app_sky_blue));
            } else if (type == 5) {//rejected from merchant side
                iv_point.setColorFilter(ContextCompat.getColor(context, R.color.red));
            }
//            val colors = arrayOf(
//                Color.parseColor("#70DDFF"),
//                Color.parseColor("#EDB054"),
//                Color.parseColor("#BB579F"),
//                Color.parseColor("#16C89D"),
//                Color.parseColor("#8D92FF")
//            )
//            val randomColor = colors.random()
//            iv_point.setColorFilter(randomColor)


            setOnClickListener {
                listener.onNotificationClick(data[position].contactUserId)
            }
        }
    }

    interface NotificationItemClickListener{
        fun onNotificationClick(benId:String)
    }
}