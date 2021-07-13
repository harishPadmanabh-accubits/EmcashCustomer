package com.emcash.customerapp.ui.loademcash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.DummyCardResponse
import kotlinx.android.synthetic.main.item_payment_account.view.*


class AccountsAdapter(private val accounts: ArrayList<DummyCardResponse>) :
    RecyclerView.Adapter<AccountsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var selectedId = 0
    private var defaultCardPos =-1




    override fun getItemCount() = accounts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment_account, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentItem = accounts[position]

        holder.itemView.apply {

            tv_card.text = currentItem.cardnumber
            tv_amount.text = currentItem.amount

            if (currentItem.default) {
                selectedId = currentItem.id
                defaultCardPos = position
            }
            ll_item_main.setOnClickListener {
                selectedId = currentItem.id
                accounts[defaultCardPos].default =false
                notifyDataSetChanged()
            }

            if(selectedId == currentItem.id)
                selectCard(this)
            else
                unSelectCard(this)
        }
    }

    private fun selectCard(itemView: View){
        itemView.apply {
            rb_select.isChecked = true
            ll_item_main.setBackgroundResource(R.drawable.grey_rounded_bg)
        }
    }

    private fun unSelectCard(itemView: View){
        itemView.apply {
            rb_select.isChecked = false
            ll_item_main.setBackgroundResource(R.color.white)
        }
    }


}