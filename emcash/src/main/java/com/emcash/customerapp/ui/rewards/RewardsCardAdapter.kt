package com.emcash.customerapp.ui.rewards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.ui.rewards.RewardsCardAdapter.*
import kotlinx.android.synthetic.main.item_my_reward_card.view.*

class RewardsCardAdapter:RecyclerView.Adapter<ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_my_reward_card,parent,false)
    )

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            if(position>5){
                fl_earn_reward.hide()
                fl_reward_info.show()
            }else{
                fl_earn_reward.show()
                fl_reward_info.hide()
                setOnClickListener {
                    context.openActivity(RewardDetails::class.java)
                }
            }

        }

    }
}