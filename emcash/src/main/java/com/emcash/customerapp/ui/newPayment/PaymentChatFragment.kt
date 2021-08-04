package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.item_chat.*
import kotlinx.android.synthetic.main.payment_chats.*

class PaymentChatFragment:Fragment(R.layout.payment_chats) {

    val viewModel :NewPaymentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cl_chats.setOnClickListener {
            viewModel.gotoScreen(NewPaymentScreens.RECEIPT)
        }

        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}