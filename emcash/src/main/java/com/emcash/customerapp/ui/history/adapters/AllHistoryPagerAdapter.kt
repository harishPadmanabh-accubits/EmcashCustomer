package com.emcash.customerapp.ui.history.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.transactions.TransactionHistoryGroupResponse.Data.TransactionGroup
import com.emcash.customerapp.ui.history.adapters.AllHistoryPagerAdapter.ViewHolder
import kotlinx.android.synthetic.main.item_transaction_item.view.*

object TransactionsDiffUtil : DiffUtil.ItemCallback<TransactionGroup>() {
    override fun areItemsTheSame(oldItem: TransactionGroup, newItem: TransactionGroup): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: TransactionGroup, newItem: TransactionGroup): Boolean {
        return false
    }

}

class AllHistoryPagerAdapter : PagingDataAdapter<TransactionGroup, ViewHolder>(TransactionsDiffUtil) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transactions = getItem(position)
        holder.itemView.apply {
            transactions?.let {
                tv_transaction_date.text = it.date
                rv_transaction_details.adapter = AllTransactionsDetailsAdapter(it.transactions)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction_item, parent, false)
        )
}

class InboundHistoryPagerAdapter : PagingDataAdapter<TransactionGroup, InboundHistoryPagerAdapter.ViewHolder>(TransactionsDiffUtil) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transactions = getItem(position)
        holder.itemView.apply {
            transactions?.let {
                tv_transaction_date.text = it.date
                rv_transaction_details.adapter = AllTransactionsDetailsAdapter(it.transactions)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction_item, parent, false)
        )
}

class OutboundHistoryPagerAdapter : PagingDataAdapter<TransactionGroup, OutboundHistoryPagerAdapter.ViewHolder>(TransactionsDiffUtil) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transactions = getItem(position)
        holder.itemView.apply {
            transactions?.let {
                tv_transaction_date.text = it.date
                rv_transaction_details.adapter = AllTransactionsDetailsAdapter(it.transactions)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction_item, parent, false)
        )
}
