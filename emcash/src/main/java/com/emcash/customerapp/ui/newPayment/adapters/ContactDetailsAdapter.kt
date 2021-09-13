package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.contacts.ContactsGroup
import kotlinx.android.synthetic.main.item_inner_contact_details.view.*


class ContactDetailsAdapter(
    val contacts: List<ContactsGroup.ContactInfo>,
    val listener: ContactsListener
) : RecyclerView.Adapter<ContactDetailsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_inner_contact_details, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = contacts[position]
        holder.itemView.apply {
            setLevel(this,currentItem.level)
            setUserDp(this,currentItem.profileImage.toString())
            setDetails(this, currentItem.name, currentItem.phoneNumber)
            setOnClickListener {
                listener.onSelectedFromAllContacts(currentItem)
            }


        }
    }

    private fun setDetails(itemView: View, name: String, phoneNumber: String) {
        itemView.apply {
            tv_name.text=name
            tv_contact_number.text = phoneNumber
        }
    }

    private fun setUserDp(itemView: View, profileImage: String) {
        itemView.apply {
            iv_user_dp.loadImageWithPlaceHolder(profileImage,R.drawable.ic_profile_placeholder)
            tv_user_name_letter.hide()
        }
    }

    private fun setLevel(itemView: View, level: Int) {
      itemView.apply {
          when (level) {
              1 -> fl_user_level.setBackgroundResource(R.drawable.green_round)
              2 -> fl_user_level.setBackgroundResource((R.drawable.yellow_round))
              3 -> fl_user_level.setBackgroundResource(R.drawable.red_round)
              else -> fl_user_level.makeInvisible()
          }
      }

    }

    override fun getItemCount(): Int = contacts.size
}