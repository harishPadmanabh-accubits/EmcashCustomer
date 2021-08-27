package com.emcash.customerapp.ui.history.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.transactions.TransactionHistoryGroupUIModel
import kotlinx.android.synthetic.main.item_transaction_item.view.*

class AllTransactionAdapter(val transactions : ArrayList<TransactionHistoryGroupUIModel>) : RecyclerView.Adapter<AllTransactionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllTransactionAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_item, parent, false)
        return AllTransactionAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transactions = transactions[position]
        holder.itemView.apply {
            tv_transaction_date.text =transactions.date
            rv_transaction_details.adapter =AllTransactionsDetailsAdapter(transactions.activities)
        }

    }

    override fun getItemCount(): Int {
        return transactions.size
    }


}