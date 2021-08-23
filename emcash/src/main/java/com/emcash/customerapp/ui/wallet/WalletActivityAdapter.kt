package com.emcash.customerapp.ui.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.DummyAcivityModel
import kotlinx.android.synthetic.main.item_wallet_activity.view.*

class WalletActivityAdapter(val data:List<DummyAcivityModel>):RecyclerView.Adapter<WalletActivityAdapter.ViewHolder>() {
   open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_wallet_activity,parent,false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.itemView.apply {
            tv_activity_date.text =currentItem.date
            rv_activity_details.apply {
                //adapter = WalletActivityDetailsAdapter(currentItem.activities)
            }

        }
    }


}