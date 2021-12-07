package com.emcash.customerapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.ui.newPayment.adapters.ContactsListener
import com.emcash.customerapp.utils.SCREEN_HOME_RECENT_CONTACTS
import com.emcash.customerapp.utils.TYPE_USER
import com.emcash.customerapp.utils.TYPE_VIEW_ALL
import kotlinx.android.synthetic.main.item_recent_payment.view.*
import java.lang.IllegalStateException

class RecentTransactionDiffUtils : DiffUtil.ItemCallback<RecentTransactionItem>() {
    override fun areItemsTheSame(
        oldItem: RecentTransactionItem,
        newItem: RecentTransactionItem
    ): Boolean = oldItem.userId == newItem.userId

    override fun areContentsTheSame(
        oldItem: RecentTransactionItem,
        newItem: RecentTransactionItem
    ): Boolean = oldItem == newItem

}

class RecentTransactionAdapterV2(private val listener: ContactsListener) :
    ListAdapter<RecentTransactionItem, RecyclerView.ViewHolder>(RecentTransactionDiffUtils()) {
    private var shouldShowViewAll = itemCount >= 9
    var source = SCREEN_HOME_RECENT_CONTACTS
    override fun getItemViewType(position: Int): Int {
        return if (shouldShowViewAll) {
            if (position == itemCount)
                TYPE_VIEW_ALL
            else
                TYPE_USER

        } else {
            TYPE_USER
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_USER -> {
                if (source == SCREEN_HOME_RECENT_CONTACTS)
                    RecentTransactionsViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_recent_payment, parent, false)
                    )
                else
                    RecentTransactionsViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_recent_contacts_screen, parent, false)
                    )
            }
            TYPE_VIEW_ALL -> {
                ViewAllViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_view_all, parent, false)
                )
            }
            else -> throw IllegalStateException("Invalid view type in recent payments Recycler View")
        }
    }


    class RecentTransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: RecentTransactionItem, listener: ContactsListener) {
            val firstname = user.name.split(" ")
            itemView.level_image.setProfileName(firstname[0])
            itemView.level_image.setLevel(user.level)
            if (user.profileImage !== null) {
                itemView.level_image.setProfileImage(user.profileImage)
            } else {
                itemView.level_image.setFirstLetter(user.name)
            }
            itemView.setOnClickListener {
                listener.onSelectedFromRecentContacts(user)
            }
        }

    }

    class ViewAllViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(listener: ContactsListener) {
            itemView.setOnClickListener {
                listener.onSelectedViewAllTransactions()
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecentTransactionsViewHolder -> {
                holder.bind(getItem(position), listener)
            }
            is ViewAllViewHolder -> {
                holder.bind(listener)
            }
        }
    }
}