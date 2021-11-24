package com.emcash.customerapp.ui.viewAllTransactions.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import kotlinx.android.synthetic.main.item_recent_payment.view.*

class ViewAllTransactionsAdapterV2(
    private val listener: AllTransactedUserItemClickListener
) :
    PagingDataAdapter<RecentTransactionItem, ViewAllTransactionsAdapterV2.ViewHolder>(
        ViewAllTransactionDiffUtils()
    ) {
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recent_payment, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.apply {
            currentItem?.let {item->
                val firstName = item.name.split(" ")
                level_image.setProfileName(firstName[0])
                level_image.setLevel(item.level)
                if (!item.profileImage.isNullOrEmpty()) {
                    level_image.setProfileImage(item.profileImage)
                } else {
                    level_image.setFirstLetter(item.name)
                }
                setOnClickListener {
                    listener.onTransactedUserClicked(item)
                }
            }
        }

    }
}

class ViewAllTransactionDiffUtils : DiffUtil.ItemCallback<RecentTransactionItem>() {
    override fun areItemsTheSame(
        oldItem: RecentTransactionItem,
        newItem: RecentTransactionItem
    ): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(
        oldItem: RecentTransactionItem,
        newItem: RecentTransactionItem
    ): Boolean {
        return oldItem == newItem
    }

}

interface AllTransactedUserItemClickListener{
    fun onTransactedUserClicked(user:RecentTransactionItem)
}
