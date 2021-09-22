package com.emcash.customerapp.ui.notifiacations.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.notifications.NotificationUiModel
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(val data:  MutableList<NotificationUiModel>,
val listener:NotificationDetailsAdapter.NotificationItemClickListener
): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_notification_date.text= data[position].date
            rv_notification_details.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                adapter = NotificationDetailsAdapter(data[position].activities,listener)
            }
        }
    }


}