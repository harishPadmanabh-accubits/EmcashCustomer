package com.emcash.customerapp.ui.history.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.toFormattedTime
import com.emcash.customerapp.model.transactions.Transaction
import kotlinx.android.synthetic.main.item_inner_history_item.view.*


class AllTransactionsDetailsAdapter(val transactions: List<Transaction>) :
    RecyclerView.Adapter<AllTransactionsDetailsAdapter.ViewHolder>() {

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllTransactionsDetailsAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inner_history_item, parent, false)
        return AllTransactionsDetailsAdapter.ViewHolder(view)

    }

    override fun onBindViewHolder(holder: AllTransactionsDetailsAdapter.ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_time.text = toFormattedTime(transactions[position].updatedAt)
            val type = transactions[position].type
            val status = transactions[position].status

            tv_time.text = toFormattedTime(transactions[position].updatedAt)

            if (type == 1) {
                tv_type_indicator.text = transactions[position].transferUserInfo.name

                if (status == 1) {//success
                    tv_balance.text =
                        transactions[position].walletTransactionInfo.balance.toString()

                    if (transactions[position].walletTransactionInfo.mode == 1) {
                        tv_value_changed.text =
                            "+" + transactions[position].amount.toString() + " EmCash"

                        iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_inbound)

                        tv_balance.apply {
                            setTextColor(ContextCompat.getColor(context, R.color.green))
                            text =
                                transactions.get(position).walletTransactionInfo.balance.toString()
                        }
                    } else {
                        tv_value_changed.text =
                            "-" + transactions[position].amount.toString() + " EmCash"

                        iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_outbound)
                        tv_balance.apply {
                            setTextColor(ContextCompat.getColor(context, R.color.red))
                            text =
                                transactions.get(position).walletTransactionInfo.balance.toString()
                        }
                    }
                } else if (status == 2) {//pending
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_pending)

                    tv_value_changed.text = transactions[position].amount.toString() + " EmCash"
                    //need to add pending icon
                    tv_info_balance.text = "Transfer Pending"
                    tv_balance.visibility = View.GONE

                } else if (status == 3) {//failed

                    //need to add failed icon
                    tv_value_changed.text =  transactions[position].amount.toString() + " EmCash"
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_failed)
                    tv_info_balance.text = "Failed"
                    tv_balance.visibility = View.GONE

                }else if (status == 4) {//rejected

                    //need to add reject icon
                    tv_value_changed.text =  transactions[position].amount.toString() + " EmCash"
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_rejected)
                    tv_info_balance.text = "Rejected"
                    tv_balance.visibility = View.GONE

                }


            } else if (type == 2) {//emcash loaded cases
                if (status == 1) {
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_icon_load_emcash)
                    tv_balance.text =
                        transactions[position].walletTransactionInfo.balance.toString()
                    tv_type_indicator.text = "Emcash Loaded"
                    tv_value_changed.text =
                        "+" + transactions[position].amount.toString() + " EmCash"

                    tv_balance.apply {

                        setTextColor(ContextCompat.getColor(context, R.color.green))
                        text = transactions.get(position).walletTransactionInfo.balance.toString()
                    }

                } else if (status == 2) {
                    tv_value_changed.text = transactions[position].amount.toString() + " EmCash"
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_pending)

                    tv_type_indicator.text = "Emcash Load"
                    tv_info_balance.text = "Pending"
                    tv_balance.visibility = View.GONE

                } else if (status == 3) {
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_failed)
                    tv_value_changed.text = transactions[position].amount.toString() + " EmCash"
                    tv_type_indicator.text = "Emcash Load"
                    tv_info_balance.text = "Failed"
                    tv_balance.visibility = View.GONE

                }else if (status == 4) {//rejected

                    //need to add reject icon
                    tv_value_changed.text =  transactions[position].amount.toString() + " EmCash"
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_rejected)
                    tv_info_balance.text = "Rejected"
                    tv_balance.visibility = View.GONE

                }


            } else if (type == 3) {//emcash converted cases
                if (status == 1) {
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_icon_convert)
                    tv_value_changed.text =
                        "-" + transactions[position].amount.toString() + " EmCash"
                    tv_balance.text =
                        transactions[position].walletTransactionInfo.balance.toString()
                    tv_type_indicator.text = "Emcash Converted"
                    tv_balance.apply {
                        setTextColor(ContextCompat.getColor(context, R.color.red))
                        text = transactions.get(position).walletTransactionInfo.balance.toString()
                    }

                } else if (status == 2) {
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_pending)
                    tv_value_changed.text = transactions[position].amount.toString() + " EmCash"
                    tv_type_indicator.text = "Emcash Conversion"
                    tv_info_balance.text = "Pending"
                    tv_balance.visibility = View.GONE

                } else if (status == 3) {
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_failed)
                    tv_value_changed.text = transactions[position].amount.toString() + " EmCash"
                    tv_type_indicator.text = "Emcash Conversion"
                    tv_info_balance.text = "Failed"
                    tv_balance.visibility = View.GONE

                }else if (status == 4) {//rejected

                    //need to add reject icon
                    tv_value_changed.text =  transactions[position].amount.toString() + " EmCash"
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_rejected)
                    tv_info_balance.text = "Rejected"
                    tv_balance.visibility = View.GONE

                }



            } else if (type == 4) {//request
//                iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_pending)
//                tv_value_changed.text = transactions[position].amount.toString() + " EmCash"
//                tv_type_indicator.text = transactions.get(position).transferUserInfo.name
//                tv_info_balance.text = "Request Pending"
//                tv_balance.visibility = View.GONE

                tv_type_indicator.text = transactions[position].transferUserInfo.name

                if (status == 1) {//success
                    tv_balance.text =
                        transactions[position].walletTransactionInfo.balance.toString()

                    if (transactions[position].walletTransactionInfo.mode == 1) {
                        tv_value_changed.text = "+" + transactions[position].amount.toString() + " EmCash"

                        iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_inbound)

                        tv_balance.apply {
                            setTextColor(ContextCompat.getColor(context, R.color.green))
                            text =
                                transactions.get(position).walletTransactionInfo.balance.toString()
                        }
                    } else {
                        tv_value_changed.text = "-" + transactions[position].amount.toString() + " EmCash"

                        iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_outbound)
                        tv_balance.apply {
                            setTextColor(ContextCompat.getColor(context, R.color.red))
                            text =
                                transactions.get(position).walletTransactionInfo.balance.toString()
                        }
                    }
                } else if (status == 2) {//pending
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_pending)

                    tv_value_changed.text =
                        transactions[position].amount.toString() + " EmCash"
                    //need to add pending icon
                    tv_info_balance.text = "Request Pending"
                    tv_balance.visibility = View.GONE

                } else if (status == 3) {//failed

                    //need to add failed icon
                    tv_value_changed.text =  transactions[position].amount.toString() + " EmCash"
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_failed)
                    tv_info_balance.text = "Failed"
                    tv_balance.visibility = View.GONE

                }else if (status == 4) {//rejected

                    //need to add reject icon
                    tv_value_changed.text =  transactions[position].amount.toString() + " EmCash"
                    iv_type_indicator_load_emcash.setBackgroundResource(R.drawable.ic_rejected)
                    tv_info_balance.text = "Rejected"
                    tv_balance.visibility = View.GONE

                }


            }


        }


    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}