package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.payments.TransactionDetailsResponse
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.payment_reciept.*
import kotlinx.android.synthetic.main.payment_reciept.iv_user_dp
import kotlinx.android.synthetic.main.receipt_card.*
import timber.log.Timber

class PaymentReceiptFragment:Fragment(R.layout.payment_reciept) {

    val viewModel:NewPaymentViewModel by activityViewModels()
    val loader by lazy{LoaderDialog(requireContext())}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val refId=viewModel.syncManager.initiatedRefId
        Timber.e("Ref id $refId")
        observe(refId)

    }

    private fun observe(refId: String?) {
        viewModel.apply {
            if(!refId.isNullOrEmpty()){
                getTransactionDetails(refId).observe(viewLifecycleOwner, Observer {
                    when(it.status){
                        ApiCallStatus.LOADING->loader.showLoader()
                        ApiCallStatus.SUCCESS->{
                            loader.hideLoader()
                            val details = it.data
                            beneficiaryId = details?.transferUserInfo?.userId ?: 0
                            details?.let {transactionDetails->
                                renderReceipt(transactionDetails)
                            }
                        }
                        ApiCallStatus.ERROR->{
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
        iv_user_dp.loadImageWithPlaceHolder(details.transferUserInfo.profileImage,R.drawable.ic_profile_placeholder)
        iv_receipient_dp.loadImageWithPlaceHolder(details.transferUserInfo.profileImage,R.drawable.ic_profile_placeholder)
        iv_user_dp_handshake.loadImageWithPlaceHolder(SyncManager(requireContext()).profileDetails?.profileImage,R.drawable.ic_profile_placeholder)
        tv_user_name.text = details.transferUserInfo.name
        tv_reciepient_name.text = details.transferUserInfo.name
        tv_user_phone.text = details.transferUserInfo.phoneNumber
        user_dp.setImage(viewModel.syncManager.profileDetails?.profileImage)
        tv_value_emcash.text = details.amount.toString()
        tv_date_time.text= toFormattedDate(details.createdAt) +" "+ toFormattedTime(details.createdAt.toString())
        tv_handshake_date.text = toFormattedDate(details.createdAt)


        val status = details.status
        val type = details.type
        if (status == 1) {
            tv_payment_type.text = "Payment Success"
          //  tv_payment_status2.text=getString(R.string.payment_success)

            if(type==1){
                tv_payment_type.text="Transfer Success"

            }else if(type==4){
                tv_payment_type.text="Request Success"

            }

            iv_status.loadImageWithResId(R.drawable.ic_payment_success)
           // iv_status.setBackgroundResource(R.drawable.ic_payment_success)
            //iv_status_point.setBackgroundResource(R.drawable.ic_green_ellipse)
        } else if (status == 2) {
            tv_payment_type.text = "Payment In Progress"
           // tv_payment_status2.text=getString(R.string.payment_inprogress)
          //  tv_request_status.text=getString(R.string.payment_inprogress)

            iv_status.loadImageWithResId(R.drawable.ic_payment_pending)
            if(type==1){
            //    tv_request_status.text="Transfer In Progress"
                tv_payment_type.text = "Transfer In Progress"


            }else if(type==4){
             //   tv_request_status.text="Request In Progress"
                tv_payment_type.text = "Request In Progress"


            }


        } else if (status == 3) {
            tv_payment_type.text = "Payment Failed"
         //   tv_payment_status2.text=getString(R.string.payment_failed)
       //     tv_request_status.text=getString(R.string.payment_failed)

            if(type==1){
                tv_payment_type.text="Transfer Failed"


            }else if(type==4){
                tv_payment_type.text="Request Failed"

            }
            iv_status.loadImageWithResId(R.drawable.ic_paymnet_failed)
         //   iv_status_point.setBackgroundResource(R.drawable.ic_red_ellipse)

        }else if(status==4){
            tv_payment_type.text = "Payment Rejected"
      //      tv_payment_status2.text=getString(R.string.payment_rejected)
         //   tv_request_status.text= getString(R.string.payment_rejected)

            if(type==1){
                tv_payment_type.text="Transfer Rejected"

            }else if(type==4){
                tv_payment_type.text="Request Rejected"

            }

            iv_status.setBackgroundResource(R.drawable.ic_payment_rejected)
            //iv_status_point.setBackgroundResource(R.drawable.ic_red_ellipse)
        }

        if(!details.handShakingStatus){
            if(status==4){
                iv_handshake.loadImageWithResId(R.drawable.ic_handshake_rejected)
                tv_handshake_date.visibility=View.GONE
                tv_handshake_status.visibility=View.INVISIBLE
            }else if(status==2){
                iv_handshake.loadImageWithResId(R.drawable.ic_handshakepending)
                tv_handshake_date.visibility=View.GONE
                tv_handshake_status.visibility=View.INVISIBLE
            }

        }else{
            iv_handshake.loadImageWithResId(R.drawable.handshake)
            tv_handshake_date.visibility=View.VISIBLE
            tv_handshake_status.visibility=View.VISIBLE

        }
        if (details.isReciever) {
            iv_payment_type.loadImageWithResId(R.drawable.ic_green_inverted_arrow)
        }else{
            iv_payment_type.loadImageWithResId(R.drawable.ic_send_chat)

        }
        tv_transaction_id.text= trimID(details.id)
        Timber.e("WalletId ${details.walletTransactionInfo?.walletId.toString()}")
        tv_wallet_id.text= trimID(details.walletTransactionInfo?.walletId.toString())
        tv_desc.text=details.description

    }


}