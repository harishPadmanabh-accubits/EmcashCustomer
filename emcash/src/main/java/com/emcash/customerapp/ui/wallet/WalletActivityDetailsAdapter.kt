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

import com.emcash.customerapp.model.wallet.WalletActivityGroupResponse.Data.WalletActivityGroup.WalletActivity
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_MODE_DEBIT
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_TYPE_REQUEST
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_TYPE_TOPUP
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_TYPE_TRANSFER
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_TYPE_WITHDRAW
import kotlinx.android.synthetic.main.item_inner_activity_details.view.*

class WalletActivityDetailsAdapter(val data : List<WalletActivity>):RecyclerView.Adapter<WalletActivityDetailsAdapter.ViewHolder>() {
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
            tv_time.text = toFormattedTime(currentItem.updatedAt)
            val type=currentItem.transactionInfo.type
            if(type== PAYMENT_TYPE_TRANSFER){
                if (currentItem.mode == PAYMENT_MODE_DEBIT) {
                    tv_type_indicator.text=currentItem.remitter.name
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_inbound)
                    iv_type_indicator_load_emcash.show()
                    tv_info_emcash.hide()


                } else {
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_outbound)
                    iv_type_indicator_load_emcash.show()
                    tv_type_indicator.text=currentItem.beneficiary.name
                    tv_info_emcash.hide()

                }
            }
            else if(type==PAYMENT_TYPE_TOPUP){
                iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_icon_load_emcash)
                iv_type_indicator_load_emcash.show()
                tv_type_indicator.text="Loaded"
                tv_value_emcash.text = currentItem.transactionInfo.amount.toString()
                tv_value_changed.text = "+${currentItem.transactionInfo.amount}"
            }else if(type==PAYMENT_TYPE_WITHDRAW){
                iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_icon_convert)
                iv_type_indicator_load_emcash.show()
                tv_type_indicator.text="Converted"
                tv_value_emcash.text = currentItem.transactionInfo.amount.toString()
                tv_value_changed.text = "AED ${currentItem.transactionInfo.amount}"
                iv_coin_image.hide()
            }else if(type ==PAYMENT_TYPE_REQUEST ){
                if (currentItem.mode == PAYMENT_MODE_DEBIT) {
                    tv_type_indicator.text=currentItem.remitter.name
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_inbound)
                    iv_type_indicator_load_emcash.show()
                    tv_info_emcash.hide()


                } else {
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_outbound)
                    tv_type_indicator.text=currentItem.beneficiary.name
                    iv_type_indicator_load_emcash.show()
                    tv_info_emcash.hide()


                }
            }
            if (currentItem.mode == PAYMENT_MODE_DEBIT) {
                tv_value_changed.text="+"+currentItem.transactionInfo.amount.toString()

            } else {
                tv_value_changed.text="-"+currentItem.transactionInfo.amount.toString()

            }

            tv_balance.apply {
                tv_balance.text = currentItem.balance.toString()
                if (currentItem.mode == PAYMENT_MODE_DEBIT) {
                    setTextColor(ContextCompat.getColor(context,R.color.green))
                } else {
                    setTextColor(ContextCompat.getColor(context,R.color.red))
                }
            }

        }
    }

}