package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.payments.TransactionDetailsResponse
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_FAILED
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_IN_PROGRESS
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_REJECTED
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_SUCCESS
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_TYPE_REQUEST
import com.emcash.customerapp.utils.EmCashUtils.PAYMENT_TYPE_TRANSFER
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.payment_reciept.*
import kotlinx.android.synthetic.main.payment_reciept.iv_user_dp
import kotlinx.android.synthetic.main.receipt_card.*
import timber.log.Timber
import java.util.*

class PaymentReceiptFragment : Fragment(R.layout.payment_reciept) {

    val viewModel: NewPaymentViewModel by activityViewModels()
    val loader by lazy { LoaderDialog(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val refId = viewModel.syncManager.initiatedRefId
        Timber.e("Ref id $refId")
        observe(refId)

    }

    private fun observe(refId: String?) {
        viewModel.apply {
            if (!refId.isNullOrEmpty()) {
                getTransactionDetails(refId).observe(viewLifecycleOwner, Observer {
                    when (it.status) {
                        ApiCallStatus.LOADING -> loader.showLoader()
                        ApiCallStatus.SUCCESS -> {
                            loader.hideLoader()
                            val details = it.data
                            beneficiaryId = details?.transferUserInfo?.userId ?: 0
                            details?.let { transactionDetails ->
                                renderReceipt(transactionDetails)
                            }
                        }
                        ApiCallStatus.ERROR -> {
                            loader.hideLoader()
                            requireActivity().showShortToast(it.errorMessage)
                        }
                    }
                })
            }
        }
    }

    private fun renderReceipt(details: TransactionDetailsResponse.Data) {
        fl_user_level.setlevel(details.transferUserInfo.level)
        iv_user_dp.loadImageWithErrorCallback(details.transferUserInfo.profileImage, onError = {
            tv_user_name_letter.apply {
                text = details.transferUserInfo.name.first().toString().toUpperCase(Locale.ROOT)
                show()
            }
        })

        tv_user_name.text = details.transferUserInfo.name
        tv_user_phone.text = details.transferUserInfo.phoneNumber
        user_dp.setImage(viewModel.syncManager.profileDetails?.profileImage)
        tv_value_emcash.text = details.amount.toString()
        tv_date_time.text =
            details.createdAt.toLocalDate().plus(" , ").plus(details.createdAt.toLocalTime())


        val status = details.status
        val type = details.type
        if (status == PAYMENT_SUCCESS) {
            tv_payment_type.text = "Payment Success"
            if (type == PAYMENT_TYPE_TRANSFER) {
                tv_payment_type.text = "Transfer Success"

            } else if (type == PAYMENT_TYPE_REQUEST) {
                tv_payment_type.text = "Request Success"

            }

            iv_status.loadImageWithResId(R.drawable.ic_payment_success)
        } else if (status == PAYMENT_IN_PROGRESS) {
            tv_payment_type.text = "Payment In Progress"

            iv_status.loadImageWithResId(R.drawable.ic_payment_pending)
            if (type == PAYMENT_TYPE_TRANSFER) {
                tv_payment_type.text = "Transfer In Progress"
            } else if (type == PAYMENT_TYPE_REQUEST) {
                tv_payment_type.text = "Request In Progress"
            }


        } else if (status == PAYMENT_FAILED) {
            tv_payment_type.text = "Payment Failed"
            if (type == PAYMENT_TYPE_TRANSFER) {
                tv_payment_type.text = "Transfer Failed"
            } else if (type == PAYMENT_TYPE_REQUEST) {
                tv_payment_type.text = "Request Failed"
            }
            iv_status.loadImageWithResId(R.drawable.ic_paymnet_failed)

        } else if (status == PAYMENT_REJECTED) {
            tv_payment_type.text = "Payment Rejected"

            if (type == PAYMENT_TYPE_TRANSFER) {
                tv_payment_type.text = "Transfer Rejected"

            } else if (type == PAYMENT_TYPE_REQUEST) {
                tv_payment_type.text = "Request Rejected"

            }

            iv_status.setBackgroundResource(R.drawable.ic_payment_rejected)
        }

        renderHandshakeCard(details, status)

        if (details.isReciever) {
            iv_payment_type.loadImageWithResId(R.drawable.ic_green_inverted_arrow)
        } else {
            iv_payment_type.loadImageWithResId(R.drawable.ic_send_chat)

        }
        tv_transaction_id.text = trimID(details.id)
        Timber.e("WalletId ${details.walletTransactionInfo?.walletId.toString()}")
        tv_wallet_id.text = trimID(details.walletTransactionInfo?.walletId.toString())
        tv_desc.text = details.description

    }

    private fun renderHandshakeCard(details: TransactionDetailsResponse.Data, status: Int) {
        iv_sender_dp.loadImageWithErrorCallback(details.transferUserInfo.profileImage,onError = {
            tv_sender_name_letter.text = details.transferUserInfo.name.first().toString()
        })
        iv_current_user_dp.loadImageWithErrorCallback(
            SyncManager(requireContext()).profileDetails?.profileImage,
            onError = {
               tv_current_user_name_letter.text = SyncManager(requireContext()).profileDetails?.name?.first().toString()
            }
        )
        tv_sender_name.text = details.transferUserInfo.name
        tv_handshake_date.text = details.createdAt.toLocalDate()
        if (!details.handShakingStatus) {
            if (status == PAYMENT_REJECTED) {
                iv_handshake.loadImageWithResId(R.drawable.ic_handshake_rejected)
                iv_handshake.setBackgroundResource(0)
                tv_handshake_date.hide()
                tv_handshake_status.hide()
            } else if (status == PAYMENT_IN_PROGRESS) {
                iv_handshake.loadImageWithResId(R.drawable.ic_handshakepending)
                iv_handshake.setBackgroundResource(0)
                tv_handshake_date.hide()
                tv_handshake_status.hide()
            }

        } else {
            iv_handshake.apply {
                loadImageWithResId(R.drawable.handshake)
                 setPadding(24)
            }
            tv_handshake_date.show()
            tv_handshake_status.apply {
                text = context.getString(R.string.handshaked)
                show()
            }
        }
    }


}