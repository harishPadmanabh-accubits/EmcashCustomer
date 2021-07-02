package com.emcash.customerapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.DummyUserData
import kotlinx.android.synthetic.main.item_recent_payment.view.*
import java.lang.IllegalStateException

class RecentTransactionsAdapter(val users : List<DummyUserData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_USER ->{
                RecentTransactionsViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_recent_payment, parent, false)
                )
            }
            TYPE_VIEW_ALL->{
                ViewAllViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_view_all, parent, false)
                )
            }
            else -> throw IllegalStateException("Invalid view type in recent payments Recycler View")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==users.size)
            TYPE_VIEW_ALL
        else
            TYPE_USER
    }


    override fun getItemCount(): Int = users.size.plus(1)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is RecentTransactionsViewHolder->{
                holder.bind(users[position])
            }
            is ViewAllViewHolder->{
                holder.bind()
            }
        }
    }

    class RecentTransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(user:DummyUserData){

            itemView.level_image.setProfileName(user.name)

            if(user.image!==null){
                itemView.level_image.setProfileImage(user.image)
            }else{
                itemView.level_image.setFirstLetter(user.name)
            }
        }

    }

    class ViewAllViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(){

        }

    }


}


const val TYPE_USER = 1000
const val  TYPE_VIEW_ALL=1001