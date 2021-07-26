package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.DummyContactsRawData

class ContactDetailsAdapter(val contacts: ArrayList<DummyContactsRawData>) : RecyclerView.Adapter<ContactDetailsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_inner_contact_details, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = contacts[position]
        holder.itemView.apply {

        }
    }

    override fun getItemCount(): Int = contacts.size
}