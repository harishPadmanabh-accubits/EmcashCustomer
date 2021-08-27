package com.emcash.customerapp.ui.history.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.transactions.FilterDurationResponse
import kotlinx.android.synthetic.main.item_duration.view.*

class DurationAdapter(
    private val duration: ArrayList<FilterDurationResponse>,
    private val clickListener: DurationItemClickListener
) :
    RecyclerView.Adapter<DurationAdapter.ViewHolder>() {
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var selectedId = 0
    private var defaultDurationPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DurationAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_duration, parent, false)
        return DurationAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DurationAdapter.ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_duration.text = duration[position].duration
            ll_duration.setOnClickListener {
                selectedId = duration[position].id
                notifyDataSetChanged()
                clickListener.onDurationClicked(duration[position])

            }
            if (selectedId == duration[position].id)
                select(this)
            else
                unSelect(this)
        }
    }

    override fun getItemCount(): Int {
        return duration.size
    }
}

interface DurationItemClickListener {
    fun onDurationClicked(duration: FilterDurationResponse)
}

private fun select(itemView: View) {
    itemView.apply {
        tv_duration.setTextColor(ContextCompat.getColor(context, R.color.white))
        ll_duration.setBackgroundResource(R.drawable.blue_borderd_5dp)
    }
}

private fun unSelect(itemView: View) {
    itemView.apply {
        tv_duration.setTextColor(ContextCompat.getColor(context, R.color.light_black))
        ll_duration.setBackgroundResource(R.drawable.white_greyborderd_5dp)
    }
}

