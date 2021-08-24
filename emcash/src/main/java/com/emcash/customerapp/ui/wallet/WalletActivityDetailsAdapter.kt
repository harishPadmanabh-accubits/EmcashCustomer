package com.emcash.customerapp.ui.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.extensions.toFormattedTime
import com.emcash.customerapp.model.ACTIVITY_TYPE_CONVERTED
import com.emcash.customerapp.model.ACTIVITY_TYPE_LOADED
import com.emcash.customerapp.model.DummyActivityDetails
import com.emcash.customerapp.model.wallet.WalletActivityResponse
import com.emcash.customerapp.model.wallet.WalletActivityResponse.Data
import com.emcash.customerapp.model.wallet.WalletActivityResponse.Data.*
import kotlinx.android.synthetic.main.item_inner_activity_details.view.*

class WalletActivityDetailsAdapter(val data : List<WalletActivityItem>):RecyclerView.Adapter<WalletActivityDetailsAdapter.ViewHolder>() {
    open class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_inner_activity_details,parent,false)
        )
    }

    override fun getItemCount(): Int =data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
//        holder.itemView.apply {
//            if(currentItem.type == ACTIVITY_TYPE_LOADED){
//                tv_type_indicator.text = "Loaded"
//                tv_value_emcash.text = currentItem.valueLoaded
//                tv_time.text = currentItem.time
//                tv_balance.apply {
//                    text = currentItem.Balance
//                    setTextColor(ContextCompat.getColor(context,R.color.green))
//                }
//                tv_value_changed.text = currentItem.changedValue
//                iv_type_indicator_convert_emcash.hide()
//                iv_type_indicator_load_emcash.show()
//            }else if(currentItem.type == ACTIVITY_TYPE_CONVERTED){
//                tv_type_indicator.text = "Converted"
//                tv_value_emcash.text = currentItem.valueLoaded
//                tv_time.text = currentItem.time
//                tv_balance.apply {
//                    text = currentItem.Balance
//                    setTextColor(ContextCompat.getColor(context,R.color.red))
//                }
//                tv_value_changed.text = currentItem.changedValue
//                iv_coin_image.hide()
//                iv_type_indicator_convert_emcash.show()
//                iv_type_indicator_load_emcash.hide()
//
//            }
//
//        }



        holder.itemView.apply {
            tv_time.text = toFormattedTime(currentItem.updatedAt)
            val type=currentItem.transactionInfo.type
            if(type==1){
                if (currentItem.mode == 1) {
                    tv_type_indicator.text=currentItem.transactionInfo.remitter.name
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_inbound)

                } else {
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_outbound)
                    tv_type_indicator.text=currentItem.transactionInfo.beneficiary.name
                }
            }
            else if(type==2){
                iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_icon_load_emcash)
                iv_type_indicator_load_emcash.show()
                tv_type_indicator.text="Loaded"
                tv_value_emcash.text = currentItem.transactionInfo.amount.toString()
                tv_value_changed.text = "+${currentItem.transactionInfo.amount}"
            }else if(type==3){
                iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_icon_convert)
                iv_type_indicator_load_emcash.show()
                tv_type_indicator.text="Converted"
                tv_value_emcash.text = currentItem.transactionInfo.amount.toString()
                tv_value_changed.text = "AED ${currentItem.transactionInfo.amount}"
                iv_coin_image.hide()
            }else if(type ==4 ){
                if (currentItem.mode == 1) {
                    tv_type_indicator.text=currentItem.transactionInfo.remitter.name
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_inbound)

                } else {
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_outbound)
                    tv_type_indicator.text=currentItem.transactionInfo.beneficiary.name

                }
            }
//            if (currentItem.mode == 1) {
//                tv_value_changed.text="+"+currentItem.transactionInfo.amount.toString()+" EmCash"
//
//            } else {
//                tv_value_changed.text="-"+currentItem.transactionInfo.amount.toString()+" EmCash"
//
//            }
            tv_balance.apply {
                tv_balance.text = currentItem.balance.toString()
                if (currentItem.mode == 1) {
                    setTextColor(ContextCompat.getColor(context,R.color.green))

                } else {

                    setTextColor(ContextCompat.getColor(context,R.color.red))

                }
            }

        }





    }


    companion object{
        const val TYPE_TRANSFER=1
        const val TYPE_TOPUP=2
        const val TYPE_WITHDRAW = 3
        const val TYPE_REQUEST=4
    }
}