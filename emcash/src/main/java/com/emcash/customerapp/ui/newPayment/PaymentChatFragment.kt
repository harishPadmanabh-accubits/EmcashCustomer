package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.enums.TransactionType
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.payments.TransactionGroupResponse
import com.emcash.customerapp.model.payments.TransactionHistoryResponse
import com.emcash.customerapp.ui.newPayment.adapters.PaymentChatListAdapter
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.payment_chats.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PaymentChatFragment:Fragment(R.layout.payment_chats),PaymentHistoryItemClickListener {

    val viewModel : NewPaymentViewModel by activityViewModels()

    val chatAdapter by lazy {
        PaymentChatListAdapter(this)
    }

    val loader by lazy{LoaderDialog(requireContext())}

    var bundle = arguments


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_pay.setOnClickListener {
            gotoTransferScreen()
        }
        btn_request.setOnClickListener {
            gotoRequestScreen()
        }
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactionHistory.collectLatest {
                chatAdapter.submitData(it)
            }
        }

        rv_history.apply {
            setHasFixedSize(true)
            adapter = chatAdapter

        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
            viewModel.getHistory().observe(viewLifecycleOwner, Observer {
                when(it.status){
                    ApiCallStatus.LOADING-> loader.showLoader()
                    ApiCallStatus.SUCCESS->{
                        loader.hideLoader()
                        renderDetails(it.data)
                    }
                    ApiCallStatus.ERROR->{
                        loader.hideLoader()
                        requireActivity().showShortToast(it.errorMessage)
                    }
                }
            })
        }

    }

    private fun gotoRequestScreen() {
        val bundle = bundleOf(
            KEY_SELECTED_CONTACT to viewModel.beneficiaryId.toString(),
            KEY_TRANSACTION_TYPE to TYPE_REQUEST
        )
        viewModel.gotoScreen(NewPaymentScreens.TRANSFER,bundle)
    }

    private fun gotoTransferScreen() {
        val bundle = bundleOf(
            KEY_SELECTED_CONTACT to viewModel.beneficiaryId.toString(),
            KEY_TRANSACTION_TYPE to TYPE_TRANSFER
        )
        viewModel.gotoScreen(NewPaymentScreens.TRANSFER,bundle)
    }

    private fun renderDetails(data: TransactionHistoryResponse.Data?) {
        data?.let {
            val beneficiary= it.contact
            val wallet = it.wallet
            fl_user_level.setlevel(beneficiary.level)
            iv_user_dp.loadImageWithPlaceHolder(beneficiary.profileImage,R.drawable.ic_profile_placeholder)
            tv_name.text = beneficiary.name
            tv_contact_number.text = beneficiary.phoneNumber
            iv_user_coin_dp.setImage(viewModel.syncManager.profileDetails?.profileImage)
            tv_value_balance.text = wallet.amount.toString()
            cl_root.show()

        }
    }

    override fun onItemClick(transactionId: String) {
        viewModel.syncManager.initiatedRefId = transactionId
        viewModel.gotoScreen(NewPaymentScreens.RECEIPT)
    }

    override fun onAcceptPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction) {
        viewModel.syncManager.initiatedRefId = transaction.id
        val bundle = bundleOf(KEY_TRANSACTION_TYPE to TransactionType.ACCEPT)
        viewModel.gotoScreen(NewPaymentScreens.PIN,bundle)

    }

    override fun onRejectPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction) {
        viewModel.syncManager.initiatedRefId = transaction.id
        val bundle = bundleOf(KEY_TRANSACTION_TYPE to TransactionType.REJECT)
        viewModel.gotoScreen(NewPaymentScreens.PIN,bundle)
    }

}

interface PaymentHistoryItemClickListener{
   fun onItemClick(transactionId:String)
   fun onAcceptPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction)
   fun onRejectPayment(transaction: TransactionGroupResponse.Data.TransactionGroup.Transaction)
}