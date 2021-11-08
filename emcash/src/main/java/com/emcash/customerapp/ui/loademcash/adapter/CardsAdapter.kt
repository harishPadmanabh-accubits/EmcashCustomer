package com.app.emcashmerchant.ui.transactionActivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.bankCard.BankCardsListingResponse
import kotlinx.android.synthetic.main.item_payment_account.view.*

class CardsAdapter(
    val list: List<BankCardsListingResponse.Data.Card>,
    private val cardsItemClickListener: CardsItemClickListener) :
    RecyclerView.Adapter<CardsAdapter.ViewHolder>() {
    private var selectedId = 0
    private var defaultCardPos = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment_account, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: CardsAdapter.ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_card.text = list[position].alias
            tv_amount.text = "Ending XXXXXX".plus(list[position].last4)

            val currentItem = list[position]

            holder.itemView.apply {

                tv_card.text = currentItem.alias
                tv_amount.text = "Ending XXXXXX".plus(currentItem.last4)

                if (currentItem.isDefault) {
                    selectedId = currentItem.last4.toInt()
                    defaultCardPos = position
                    cardsItemClickListener.onCardClicked(currentItem)

                }
                ll_item_main.setOnClickListener {
                    selectedId = currentItem.last4.toInt()
                    list[defaultCardPos].isDefault = false
                    notifyDataSetChanged()
                    cardsItemClickListener.onCardClicked(currentItem)

                }



                rb_select.setOnClickListener {
                    selectedId = currentItem.last4.toInt()
                    list[defaultCardPos].isDefault = false
                    notifyDataSetChanged()
                    cardsItemClickListener.onCardClicked(currentItem)

                }

                if (selectedId == currentItem.last4.toInt())
                    selectCard(this)
                else
                    unSelectCard(this)
            }
        }
    }

    private fun selectCard(itemView: View) {
        itemView.apply {
            rb_select.isChecked = true
            ll_item_main.setBackgroundResource(R.drawable.grey_rounded_bg)
        }
    }

    private fun unSelectCard(itemView: View) {
        itemView.apply {
            rb_select.isChecked = false
            ll_item_main.setBackgroundResource(R.color.white)
        }
    }


    interface CardsItemClickListener {
        fun onCardClicked(card: BankCardsListingResponse.Data.Card)
    }

}