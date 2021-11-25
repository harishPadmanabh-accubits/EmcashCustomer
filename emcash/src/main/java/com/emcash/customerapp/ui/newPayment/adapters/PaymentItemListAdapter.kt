package com.emcash.customerapp.ui.newPayment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.loadImageWithResId
import com.emcash.customerapp.extensions.toFormattedTime
import com.emcash.customerapp.extensions.toLocalTime
import com.emcash.customerapp.model.payments.TransactionGroupResponse
import com.emcash.customerapp.ui.newPayment.PaymentHistoryItemClickListener
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_FAILED
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_IN_PROGRESS
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_MODE_CREDIT
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_MODE_SENT
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_REJECTED
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_SUCCESS
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_TYPE_REQUEST
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_TYPE_TRANSFER
import kotlinx.android.synthetic.main.row_payment_chat_item.view.*

class PaymentItemListAdapter(
    val transactions: List<TransactionGroupResponse.Data.TransactionGroup.Transaction>,
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
                listener.onInfoCardClicked(transactions[position].id)
            }

            ll_chat_send.setOnClickListener {
                listener.onInfoCardClicked(transactions[position].id)
            }
            bt_reject.setOnClickListener {
                listener.onRejectPayment(transactions[position])
            }
            bt_accept.setOnClickListener {
                listener.onAcceptPayment(transactions[position])
            }
            if (transactions[position].isReciever) {
                ll_chat_receive.visibility = View.VISIBLE
                ll_chat_send.visibility = View.GONE

                if (transactions[position].isConfirm)
                    ll_button_holder.visibility = View.VISIBLE
                else
                    ll_button_holder.visibility = View.GONE

                tv_time_receive.text = transactions[position].createdAt.toLocalTime()
                tv_cash_receive.text = transactions[position].amount.toString()

                when (status) {
                    PAYMENT_SUCCESS -> {
                        if (mode == PAYMENT_MODE_SENT) {
                            tv_payment_type_label_receive.text =
                                context.getString(R.string.payment_recieved)
                        } else {
                            tv_payment_type_label_receive.text =
                                context.getString(R.string.payment_sent)
                            iv_type_send.loadImageWithResId(R.drawable.ic_send_chat)

                        }
                        iv_image_receive_status.setBackgroundResource(R.drawable.ic_chat_success)

                        if (type == PAYMENT_TYPE_TRANSFER) {
                            tv_status_receive.text = context.getString(R.string.payment_success)
                        } else if (type == PAYMENT_TYPE_REQUEST) {
                            tv_status_receive.text = context.getString(R.string.request_success)

                        }
                    }
                    PAYMENT_IN_PROGRESS -> {
                        iv_image_receive_status.setBackgroundResource(R.drawable.ic_chat_pending)

                        tv_payment_type_label_receive.text =
                            context.getString(R.string.payment_pending)

                        if (type == PAYMENT_TYPE_TRANSFER) {
                            tv_status_receive.text = context.getString(R.string.payment_pending)


                        } else if (type == PAYMENT_TYPE_REQUEST) {
                            tv_status_receive.text = context.getString(R.string.request_pending)


                        }
                    }
                    PAYMENT_FAILED -> {
                        iv_image_receive_status.setBackgroundResource(R.drawable.ic_failed)

                        tv_payment_type_label_receive.text =
                            context.getString(R.string.payment_failed)

                        if (type == PAYMENT_TYPE_TRANSFER) {
                            tv_status_receive.text = context.getString(R.string.payment_failed)
                        } else if (type == PAYMENT_TYPE_REQUEST) {
                            tv_status_receive.text = context.getString(R.string.request_failed)
                        }
                    }
                    PAYMENT_REJECTED -> {
                        iv_image_receive_status.setBackgroundResource(R.drawable.ic_rejected)

                        tv_payment_type_label_receive.text =
                            context.getString(R.string.payment_rejected)

                        if (type == PAYMENT_TYPE_TRANSFER) {
                            tv_status_receive.text = context.getString(R.string.payment_rejected)
                        } else if (type == PAYMENT_TYPE_REQUEST) {
                            tv_status_receive.text = context.getString(R.string.request_rejected)
                        }
                    }
                }
            } else {

                ll_chat_receive.visibility = View.GONE
                ll_chat_send.visibility = View.VISIBLE

                tv_time_send.text = transactions[position].createdAt.toLocalTime()
                tv_cash_send.text = transactions[position].amount.toString()


                when (status) {
                    PAYMENT_SUCCESS -> {
                        if (mode == PAYMENT_MODE_CREDIT) {
                            tv_payment_type_label_send.text =
                                context.getString(R.string.payment_recieved)
                            iv_type_send.loadImageWithResId(R.drawable.ic_recieve_chat)

                        } else {
                            tv_payment_type_label_send.text = context.getString(R.string.payment_sent)

                        }
                        iv_image_send_status.setBackgroundResource(R.drawable.ic_chat_success)

                        if (type == PAYMENT_TYPE_TRANSFER) {
                            tv_status_send.text = context.getString(R.string.payment_success)
                        } else if (type == PAYMENT_TYPE_REQUEST) {
                            tv_status_send.text = context.getString(R.string.request_success)

                        }
                    }
                    PAYMENT_IN_PROGRESS -> {
                        iv_image_send_status.setBackgroundResource(R.drawable.ic_chat_pending)
                        tv_payment_type_label_send.text = context.getString(R.string.payment_initiated)

                        if (type == PAYMENT_TYPE_TRANSFER) {
                            tv_status_send.text = context.getString(R.string.payment_pending)

                        } else if (type == PAYMENT_TYPE_REQUEST) {
                            tv_status_send.text = context.getString(R.string.request_pending)

                        }
                    }
                    PAYMENT_FAILED -> {
                        iv_image_send_status.setBackgroundResource(R.drawable.ic_failed)
                        tv_payment_type_label_send.text = context.getString(R.string.payment_failed)

                        if (type == PAYMENT_TYPE_TRANSFER) {
                            tv_status_send.text = context.getString(R.string.payment_failed)

                        } else if (type == PAYMENT_TYPE_REQUEST) {
                            tv_status_send.text = context.getString(R.string.request_failed)

                        }
                    }
                    PAYMENT_REJECTED -> {
                        iv_image_send_status.setBackgroundResource(R.drawable.ic_rejected)
                        tv_payment_type_label_send.text = context.getString(R.string.payment_rejected)

                        if (type == PAYMENT_TYPE_TRANSFER) {
                            tv_status_send.text = context.getString(R.string.payment_rejected)
                        } else if (type == PAYMENT_TYPE_REQUEST) {
                            tv_status_send.text = context.getString(R.string.request_rejected)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = transactions.size
}