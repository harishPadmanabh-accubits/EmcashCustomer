package com.emcash.customerapp.ui.viewAllTransactions.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.RecentTransactionsResponse
import com.emcash.customerapp.model.transactions.RecentTransactionResponse

import kotlinx.android.synthetic.main.item_recent_payment.view.*
import kotlinx.android.synthetic.main.layout_item_recent_payment.view.*
import kotlinx.android.synthetic.main.layout_item_recent_payment.view.fl_user_level

class ViewAllTransactionsAdapterV2() : PagingDataAdapter
<RecentTransactionsResponse.Data.Row, ViewAllTransactionsAdapterV2.ViewHolder>(
    DiffUtilCallBack()
) {
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewAllTransactionsAdapterV2.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recent_payment, parent, false)
        return ViewAllTransactionsAdapterV2.ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewAllTransactionsAdapterV2.ViewHolder, position: Int) {

        val currentActivity = getItem(position)

        holder.itemView.apply {
            currentActivity?.let {
                val firstname = it.name.split(" ")
                level_image.setProfileName(firstname[0])
                level_image.setLevel(it.ppp)
                if (it.profileImage !== null) {
                    level_image.setProfileImage(it.profileImage)
                } else {
                    level_image.setFirstLetter(it.name)
                }


            }


        }

    }


    class DiffUtilCallBack : DiffUtil.ItemCallback<RecentTransactionsResponse.Data.Row>() {
        override fun areItemsTheSame(
            oldItem: RecentTransactionsResponse.Data.Row,
            newItem: RecentTransactionsResponse.Data.Row
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: RecentTransactionsResponse.Data.Row,
            newItem: RecentTransactionsResponse.Data.Row
        ): Boolean {
            return false


        }

    }
}