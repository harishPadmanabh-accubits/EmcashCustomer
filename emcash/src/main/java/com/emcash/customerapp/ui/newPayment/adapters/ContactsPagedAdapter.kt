package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.contacts.ContactsGroup
import kotlinx.android.synthetic.main.item_contact.view.*

object ContactsDiffUtils : DiffUtil.ItemCallback<ContactsGroup>() {
    override fun areItemsTheSame(oldItem: ContactsGroup, newItem: ContactsGroup): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ContactsGroup, newItem: ContactsGroup): Boolean {
        return false
    }

}

class ContactsPagedAdapter(
    val listener: ContactsListener
): PagingDataAdapter<ContactsGroup,ContactsPagedAdapter.ViewHolder>(ContactsDiffUtils) {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == itemCount-1){
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 100
            holder.itemView.layoutParams = params
        }
        val currentItem = getItem(position)
        currentItem?.let {
            holder.itemView.apply {
                tv_contact_group_letter.text = currentItem.letter
                rv_contacts.adapter = ContactDetailsAdapter(currentItem.contacts,listener)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
        )
    }
}