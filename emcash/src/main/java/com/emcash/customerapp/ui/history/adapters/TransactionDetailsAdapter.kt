package com.emcash.customerapp.ui.history.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.model.DummyTransactionDetalsModel
import com.emcash.customerapp.model.TRANSACTED_INBOUND
import com.emcash.customerapp.model.TRANSACTED_OUTBOUND
import com.emcash.customerapp.ui.history.adapters.TransactionDetailsAdapter.*
import kotlinx.android.synthetic.main.item_transaction_details.view.*

class TransactionDetailsAdapter(val transactionList: List<DummyTransactionDetalsModel>):RecyclerView.Adapter<ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_details,parent,false)
    )

    override fun getItemCount(): Int = transactionList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = transactionList[position]
        holder.itemView.apply {
            if(currentItem.type== TRANSACTED_INBOUND){
                iv_inbound_icon.show()
                iv_outbound_icon.hide()
                tv_type_indicator.text = context.getString(R.string.payment_recieved)
                tv_time.text = currentItem.time
                tv_value_changed.text = currentItem.valueLoaded.plus(" EmCash")
                tv_balance.text = currentItem.Balance
            }else if(currentItem.type == TRANSACTED_OUTBOUND){
                iv_inbound_icon.hide()
                iv_outbound_icon.show()
                tv_type_indicator.text = context.getString(R.string.paymen_made)
                tv_time.text = currentItem.time
                tv_value_changed.text = currentItem.valueLoaded.plus(" EmCash")
                tv_balance.text = currentItem.Balance
            }
        }
    }


}