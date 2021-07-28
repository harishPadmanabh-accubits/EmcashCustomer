package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.loadImageWithResId
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.utils.LevelProfileImageView.UserProfileLevel.*
import kotlinx.android.synthetic.main.item_recent_contacts.view.*

class RecentContactsAdapter(val contacts: List<DummyUserData>,val listener: ContactsListener) :RecyclerView.Adapter<RecentContactsAdapter.ViewHolder>(){
    class  ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_contacts, parent, false)
    )
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = contacts[position]
        holder.itemView.apply {
            when(currentItem.level){
                RED-> fl_user_level.setBackgroundResource(R.drawable.red_round)
                GREEN->fl_user_level.setBackgroundResource(R.drawable.green_round)
                YELLOW->fl_user_level.setBackgroundResource(R.drawable.yellow_round)
                NONE->fl_user_level.setBackgroundResource(0)
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

            tv_user_name.text = currentItem.name
            setOnClickListener {
                listener.onContactSelected(null,currentItem)
            }
        }

    }

    override fun getItemCount(): Int=contacts.size
}