package com.emcash.customerapp.ui.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.model.ACTIVITY_TYPE_CONVERTED
import com.emcash.customerapp.model.ACTIVITY_TYPE_LOADED
import com.emcash.customerapp.model.DummyActivityDetails
import kotlinx.android.synthetic.main.item_inner_activity_details.view.*

class WalletActivityDetailsAdapter(val data : List<DummyActivityDetails>):RecyclerView.Adapter<WalletActivityDetailsAdapter.ViewHolder>() {
    open class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_inner_activity_details,parent,false)
        )
    }

    override fun getItemCount(): Int =data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.itemView.apply {
            if(currentItem.type == ACTIVITY_TYPE_LOADED){
                tv_type_indicator.text = "Loaded"
                tv_value_emcash.text = currentItem.valueLoaded
                tv_time.text = currentItem.time
                tv_balance.apply {
                    text = currentItem.Balance
                    setTextColor(ContextCompat.getColor(context,R.color.green))
                }
                tv_value_changed.text = currentItem.changedValue
                iv_type_indicator_convert_emcash.hide()
                iv_type_indicator_load_emcash.show()
            }else if(currentItem.type == ACTIVITY_TYPE_CONVERTED){
                tv_type_indicator.text = "Converted"
                tv_value_emcash.text = currentItem.valueLoaded
                tv_time.text = currentItem.time
                tv_balance.apply {
                    text = currentItem.Balance
                    setTextColor(ContextCompat.getColor(context,R.color.red))
                }
                tv_value_changed.text = currentItem.changedValue
                iv_coin_image.hide()
                iv_type_indicator_convert_emcash.show()
                iv_type_indicator_load_emcash.hide()

            }

        }
    }
}