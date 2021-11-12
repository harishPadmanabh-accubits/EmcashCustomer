package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.contacts.ContactsGroup
import kotlinx.android.synthetic.main.item_inner_contact_details.view.*
import timber.log.Timber
import java.util.*


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
        Timber.e("Contact item $currentItem")
        holder.itemView.apply {
            setLevel(this,currentItem.level)
            setUserDp(this,currentItem.profileImage.toString(),currentItem.name)
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

    private fun setUserDp(itemView: View, profileImage: String, name: String) {
        itemView.apply {
            Timber.e("Profile img in all contacts $profileImage")
            iv_user_dp.loadImageWithErrorCallback(profileImage,onError = {
                tv_user_name_letter.apply {
                    text = name.first().toString().toUpperCase(Locale.ROOT)
                    show()
                }
            })
            tv_user_name_letter.hide()
        }
    }

    private fun setLevel(itemView: View, level: Int) {
      itemView.apply {
          when (level) {
              1 -> fl_user_level.setBackgroundResource(R.drawable.green_round)
              2 -> fl_user_level.setBackgroundResource((R.drawable.yellow_round))
              3 -> fl_user_level.setBackgroundResource(R.drawable.red_round)
              else -> fl_user_level.setBackgroundResource(0)
          }
      }

    }

    override fun getItemCount(): Int = contacts.size
}