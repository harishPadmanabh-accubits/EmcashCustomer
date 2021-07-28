package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.GroupedContacts
import kotlinx.android.synthetic.main.item_contact.view.*

class AllContactsAdapter(
    val groupedContactList: ArrayList<GroupedContacts>,
    val listener: ContactsListener
) : RecyclerView.Adapter<AllContactsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = groupedContactList[position]
        holder.itemView.apply {
            tv_contact_group_letter.text = currentItem.letter
            rv_contacts.adapter = ContactDetailsAdapter(currentItem.contacts,listener)
        }
    }

    override fun getItemCount(): Int = groupedContactList.size
}