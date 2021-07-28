package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.loadImageWithResId
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.model.DummyContactsRawData
import com.emcash.customerapp.utils.LevelProfileImageView
import kotlinx.android.synthetic.main.item_inner_contact_details.view.*


class ContactDetailsAdapter(
    val contacts: ArrayList<DummyContactsRawData>,
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
            when(currentItem.level){
                LevelProfileImageView.UserProfileLevel.RED -> fl_user_level.setBackgroundResource(R.drawable.red_round)
                LevelProfileImageView.UserProfileLevel.GREEN ->fl_user_level.setBackgroundResource(R.drawable.green_round)
                LevelProfileImageView.UserProfileLevel.YELLOW ->fl_user_level.setBackgroundResource(R.drawable.yellow_round)
                LevelProfileImageView.UserProfileLevel.NONE ->fl_user_level.setBackgroundResource(0)
            }
            if(currentItem.image!=null){
                iv_user_dp.loadImageWithResId(currentItem.image)
                tv_user_name_letter.hide()
            }else{
                tv_user_name_letter.apply {
                    text = currentItem.name.first().toString()
                    show()
                }
            }
            tv_name.text=currentItem.name
            tv_contact_number.text = currentItem.number
            setOnClickListener {
                listener.onContactSelected(currentItem,null)
            }
        }
    }

    override fun getItemCount(): Int = contacts.size
}