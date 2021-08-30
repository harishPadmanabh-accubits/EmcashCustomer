package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.toFormattedTime
import com.emcash.customerapp.model.payments.TransactionHistory
import com.emcash.customerapp.ui.newPayment.PaymentHistoryItemClickListener
import com.emcash.customerapp.utils.KEY_REF_ID
import kotlinx.android.synthetic.main.row_payment_chat_item.view.*

class PaymentItemListAdapter(
    val transactions: ArrayList<TransactionHistory>,
    val listener: PaymentHistoryItemClickListener
) : RecyclerView.Adapter<PaymentItemListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_payment_chat_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val status = transactions[position].status
        val type = transactions[position].type
        val mode = transactions[position].mode

        holder.itemView.apply {

            ll_chat_receive.setOnClickListener {
                listener.onItemClick(transactions[position].id)
            }

            ll_chat_send.setOnClickListener {
                listener.onItemClick(transactions[position].id)

            }
            bt_reject.setOnClickListener {
                // ClickListener.onChatRejectClicked(transactions[position])
                listener.onRejectPayment(transactions[position])
            }
            bt_accept.setOnClickListener {
                // ClickListener.onChatAcceptClicked(transactions[position])
                listener.onAcceptPayment(transactions[position])

            }
            if (transactions[position].isReciever) {
                ll_chat_receive.visibility = View.VISIBLE
                ll_chat_send.visibility = View.GONE

                if (transactions[position].isConfirm) {
                    ll_button_holder.visibility = View.VISIBLE
                } else {
                    ll_button_holder.visibility = View.GONE

                }

                tv_time_receive.text = toFormattedTime(transactions[position].createdAt)
                tv_cash_receive.text = transactions[position].amount.toString()
                if (status == 1) {
                    if (mode == 2) {
                        tv_payment_type_label_receive.text = "Payment Received"

                    } else {
                        tv_payment_type_label_receive.text = "Payment Sent"

                    }
                    iv_image_receive_status.setBackgroundResource(R.drawable.ic_success)

                    if (type == 1) {
                        tv_status_receive.text = "Payment Success"
                    } else if (type == 4) {
                        tv_status_receive.text = "Request Success"

                    }
                } else if (status == 2) {
                    iv_image_receive_status.setBackgroundResource(R.drawable.ic_pending)

                    tv_payment_type_label_receive.text = "Payment Pending"

                    if (type == 1) {
                        tv_status_receive.text = "Payment Pending"


                    } else if (type == 4) {
                        tv_status_receive.text = "Request Pending"


                    }
                } else if (status == 3) {
                    iv_image_receive_status.setBackgroundResource(R.drawable.ic_failed)

                    tv_payment_type_label_receive.text = "Payment Failed"

                    if (type == 1) {
                        tv_status_receive.text = "Payment Failed"


                    } else if (type == 4) {
                        tv_status_receive.text = "Request Failed"

                    }
                } else if (status == 4) {
                    iv_image_receive_status.setBackgroundResource(R.drawable.ic_rejected)

                    tv_payment_type_label_receive.text = "Payment Rejected"

                    if (type == 1) {
                        tv_status_receive.text = "Payment Rejected"


                    } else if (type == 4) {
                        tv_status_receive.text = "Request Rejected"

                    }
                }


            } else {

                ll_chat_receive.visibility = View.GONE
                ll_chat_send.visibility = View.VISIBLE

                tv_time_send.text = toFormattedTime(transactions[position].createdAt)
                tv_cash_send.text = transactions[position].amount.toString()


                if (status == 1) {

                    if (mode == 2) {
                        tv_payment_type_label_send.text = "Payment Received"

                    } else {
                        tv_payment_type_label_send.text = "Payment Sent"

                    }
                    iv_image_send_status.setBackgroundResource(R.drawable.ic_chat_success)

                    if (type == 1) {
                        tv_status_send.text = "Payment Success"
                    } else if (type == 4) {
                        tv_status_send.text = "Request Success"

                    }
                } else if (status == 2) {
                    iv_image_send_status.setBackgroundResource(R.drawable.ic_chat_pending)
                    tv_payment_type_label_send.text = "Payment Intiated"

                    if (type == 1) {
                        tv_status_send.text = "Payment Pending"

                    } else if (type == 4) {
                        tv_status_send.text = "Request Pending"

                    }
                } else if (status == 3) {
                    iv_image_send_status.setBackgroundResource(R.drawable.ic_failed)
                    tv_payment_type_label_send.text = "Payment Failed"

                    if (type == 1) {
                        tv_status_send.text = "Payment Failed"

                    } else if (type == 4) {
                        tv_status_send.text = "Request Failed"

                    }
                } else if (status == 4) {
                    iv_image_send_status.setBackgroundResource(R.drawable.ic_rejected)
                    tv_payment_type_label_send.text = "Payment Rejected"

                    if (type == 1) {
                        tv_status_send.text = "Payment Rejected"


                    } else if (type == 4) {
                        tv_status_send.text = "Request Rejected"

                    }
                }

            }


        }

    }

    override fun getItemCount(): Int = transactions.size
}