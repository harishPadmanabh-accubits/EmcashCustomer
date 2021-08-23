package com.emcash.customerapp.ui.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.wallet.TransactionItemUiModel
import com.emcash.customerapp.ui.wallet.WalletPagedAdapter.*
import kotlinx.android.synthetic.main.item_wallet_activity.view.*


object WalletDiffUtils : DiffUtil.ItemCallback<TransactionItemUiModel>() {
    override fun areItemsTheSame(
        oldItem: TransactionItemUiModel,
        newItem: TransactionItemUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: TransactionItemUiModel,
        newItem: TransactionItemUiModel
    ): Boolean {
        return oldItem.date == newItem.date &&
                oldItem.transactionList.containsAll(newItem.transactionList)
    }

}

class WalletPagedAdapter : PagingDataAdapter<TransactionItemUiModel, ViewHolder>(WalletDiffUtils) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.itemView.apply {
                tv_activity_date.text =it.date
                rv_activity_details.apply {
                    adapter = WalletActivityDetailsAdapter(it.transactionList)
                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_wallet_activity, parent, false)
        )

}