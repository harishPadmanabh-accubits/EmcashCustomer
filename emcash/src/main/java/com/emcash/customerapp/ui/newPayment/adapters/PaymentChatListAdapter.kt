package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.getCurrentDate
import com.emcash.customerapp.model.payments.TransactionHistoryUI
import com.emcash.customerapp.ui.newPayment.PaymentHistoryItemClickListener
import kotlinx.android.synthetic.main.row_payment_chat.view.*

object PaymentDiffUtil : DiffUtil.ItemCallback<TransactionHistoryUI>() {
    override fun areItemsTheSame(
        oldItem: TransactionHistoryUI,
        newItem: TransactionHistoryUI
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: TransactionHistoryUI,
        newItem: TransactionHistoryUI
    ): Boolean {
        return oldItem.date == newItem.date &&
                oldItem.transactions == newItem.transactions
    }

}

class PaymentChatListAdapter(
    val listener:PaymentHistoryItemClickListener
):PagingDataAdapter<TransactionHistoryUI,PaymentChatListAdapter.ViewHolder>(PaymentDiffUtil) {

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.apply {
            if (getCurrentDate().equals(currentItem?.date)) {
                tv_date.text = "Today"
            } else {
                tv_date.text = currentItem?.date

            }
            rv_chat_details.apply {
                adapter = PaymentItemListAdapter(
                    ArrayList(currentItem?.transactions),listener
                )
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.row_payment_chat, parent, false)
    )
}