package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.ContactsPageItems
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.model.GroupedContacts
import com.emcash.customerapp.ui.home.adapter.RecentTransactionsAdapter
import com.emcash.customerapp.utils.ITEM_ALL_CONTACTS
import com.emcash.customerapp.utils.ITEM_RECENT_CONTACTS
import java.lang.IllegalArgumentException

class ContactsScreenAdapter(var contactsPageItems: ArrayList<ContactsPageItems>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return contactsPageItems[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_RECENT_CONTACTS -> {
                RecentContactsViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_recent_contacts, parent, false)
                )
            }
            ITEM_ALL_CONTACTS -> {
                AllContactsViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_contacts, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Invalid Item type encountered")
        }
    }

    class RecentContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recentContactsRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_recent_contacts)

        fun bind(recentContactList: ArrayList<DummyUserData>) {
            recentContactsRecyclerView.adapter = RecentTransactionsAdapter(recentContactList.toList())
        }
    }

    class AllContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       private val allContactsRecyclerView: RecyclerView =itemView.findViewById(R.id.rv_contacts)
        fun bind(groupedContactList: ArrayList<GroupedContacts>) {
         allContactsRecyclerView.adapter = AllContactsAdapter(groupedContactList)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = contactsPageItems[position]
        when(holder){
            is RecentContactsViewHolder ->{
                currentItem.recentContactList?.let {
                    holder.bind(it)
                }
            }
            is AllContactsViewHolder ->{
                currentItem.allContactList?.let {
                    holder.bind(it)
                }
            }
        }
    }

    override fun getItemCount(): Int = contactsPageItems.size
}