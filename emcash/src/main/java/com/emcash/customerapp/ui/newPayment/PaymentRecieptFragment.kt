package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.emcash.customerapp.R
import kotlinx.android.synthetic.main.payment_reciept.*

class PaymentReceiptFragment:Fragment(R.layout.payment_reciept) {
    val viewModel:NewPaymentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}