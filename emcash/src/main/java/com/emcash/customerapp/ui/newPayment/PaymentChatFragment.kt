package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.loadImageWithPlaceHolder
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.setlevel
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.payments.TransactionHistoryResponse
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.newPayment.adapters.PaymentChatListAdapter
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.item_chat.*
import kotlinx.android.synthetic.main.payment_chats.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PaymentChatFragment:Fragment(R.layout.payment_chats) {

    val viewModel : NewPaymentViewModel by activityViewModels()

    val chatAdapter by lazy {
        PaymentChatListAdapter()
    }

    val loader by lazy{LoaderDialog(requireContext())}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        cl_chats.setOnClickListener {
//            viewModel.gotoScreen(NewPaymentScreens.RECEIPT)
//        }

        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        rv_history.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = chatAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactionHistory.collectLatest {
                chatAdapter.submitData(it)
            }
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

        }
    }

}