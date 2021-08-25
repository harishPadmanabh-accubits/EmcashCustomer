package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.newPayment.adapters.PaymentChatListAdapter
import kotlinx.android.synthetic.main.item_chat.*
import kotlinx.android.synthetic.main.payment_chats.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PaymentChatFragment:Fragment(R.layout.payment_chats) {

    val viewModel : NewPaymentViewModel by activityViewModels()

    val chatAdapter by lazy {
        PaymentChatListAdapter()
    }

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

    }

}