package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.ContactsPageItems
import com.emcash.customerapp.model.DummyContactsRawData
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.model.GroupedContacts
import com.emcash.customerapp.model.contacts.ContactItem
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.ui.home.adapter.RecentTransactionsAdapter
import com.emcash.customerapp.utils.ITEM_ALL_CONTACTS
import com.emcash.customerapp.utils.ITEM_RECENT_CONTACTS
import com.emcash.customerapp.utils.SCREEN_CONTACTS
import timber.log.Timber
import java.lang.IllegalArgumentException

class ContactsScreenAdapter(var contactsPageItems: ArrayList<ContactsPageItems>,val listener: ContactsListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var querry =""

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

        fun bind(recentContactList: ArrayList<RecentTransactionItem>, listener: ContactsListener) {
            recentContactsRecyclerView.adapter = RecentTransactionsAdapter(recentContactList.toList(),listener).also {
                it.source = SCREEN_CONTACTS
            }
        }
    }

    class AllContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       private val allContactsRecyclerView: RecyclerView =itemView.findViewById(R.id.rv_contacts)
        fun bind(
            groupedContactList: ArrayList<GroupedContacts>,
            listener: ContactsListener,
            querry: String
        ) {
         allContactsRecyclerView.adapter = AllContactsAdapter(groupedContactList,listener,querry)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = contactsPageItems[position]
        when(holder){
            is RecentContactsViewHolder ->{
                currentItem.recentContactList?.let {
                    Timber.e("Recent transactions ${it.size}")
                    if(querry.isNotEmpty())
                        it.filter {
                            it.name.contains(querry,true)
                        }
                    holder.bind(it,listener)
                }
            }
            is AllContactsViewHolder ->{
                currentItem.allContactList?.let {
                    Timber.e("All contacts ${it.size}")
                    holder.bind(it,listener,querry)
                }
            }
        }
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,R.anim.contacts_anim)
    }

    override fun getItemCount(): Int = contactsPageItems.size
}

interface ContactsListener{
    fun onContactSelected(contact:DummyContactsRawData?,recentContact:DummyUserData?)
    fun onSelectedFromRecentContacts(contact:RecentTransactionItem)
    fun onSelectedFromAllContacts(contact: ContactItem)

}