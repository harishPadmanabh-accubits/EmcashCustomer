package com.emcash.customerapp.ui.history.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.DummyTransactionModel
import com.emcash.customerapp.model.TRANSACTED_INBOUND
import com.emcash.customerapp.model.TRANSACTED_OUTBOUND
import com.emcash.customerapp.ui.history.adapters.TransactionFilter.*
import com.emcash.customerapp.ui.history.adapters.TransactionHistoryAdapter.*
import kotlinx.android.synthetic.main.item_wallet_activity.view.*

class TransactionHistoryAdapter(val transactionHistory: List<DummyTransactionModel>) :
    RecyclerView.Adapter<ViewHolder>() {
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var transactionType = ALL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_wallet_activity, parent, false)

    )

    override fun getItemCount(): Int = transactionHistory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = transactionHistory[position]
        holder.itemView.apply {
            when (transactionType) {
                ALL -> {
                    tv_activity_date.text = currentItem.date
                    rv_activity_details.apply {
                        adapter =
                            TransactionDetailsAdapter(
                                currentItem.transactionList
                            )
                    }
                }
                INBOUND -> {
                    tv_activity_date.text = currentItem.date
                    rv_activity_details.apply {
                        adapter =
                            TransactionDetailsAdapter(
                                currentItem.transactionList.filter {
                                    it.type == TRANSACTED_INBOUND
                                }
                            )
                    }
                }
                OUTBOUND -> {
                    tv_activity_date.text = currentItem.date
                    rv_activity_details.apply {
                        adapter =
                            TransactionDetailsAdapter(
                                currentItem.transactionList.filter {
                                    it.type == TRANSACTED_OUTBOUND
                                }
                            )
                    }
                }
            }


        }
    }
}

enum class TransactionFilter {
    ALL, INBOUND, OUTBOUND
}