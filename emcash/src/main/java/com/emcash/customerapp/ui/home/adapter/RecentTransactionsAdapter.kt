package com.emcash.customerapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.ui.newPayment.adapters.ContactsListener
import kotlinx.android.synthetic.main.item_recent_payment.view.*
import java.lang.IllegalStateException

class RecentTransactionsAdapter(val transactions : List<RecentTransactionItem>,val listener:ContactsListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var shouldShowViewAll = transactions.size>=9

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
        val type : Int = if(shouldShowViewAll){
            if(position==transactions.size)
                TYPE_VIEW_ALL
            else
                TYPE_USER

        }else{
            TYPE_USER
        }
        return type



//        return if(position==transactions.size)
//            TYPE_VIEW_ALL
//        else
//            TYPE_USER
    }


    override fun getItemCount(): Int {
        return if(shouldShowViewAll)
            transactions.size.plus(1)
        else
            transactions.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is RecentTransactionsViewHolder->{
                holder.bind(transactions[position],listener)
            }
            is ViewAllViewHolder->{
                holder.bind()
            }
        }
    }

    class RecentTransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(user: RecentTransactionItem, listener: ContactsListener){

            itemView.level_image.setProfileName(user.name)
            itemView.level_image.setLevel(user.level)
            if(user.profileImage!==null){
                itemView.level_image.setProfileImage(user.profileImage)
            }else{
                itemView.level_image.setFirstLetter(user.name)
            }
            itemView.setOnClickListener {
                listener.onSelectedFromRecentContacts(user)
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