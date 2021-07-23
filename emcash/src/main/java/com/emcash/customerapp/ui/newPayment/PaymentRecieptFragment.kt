package com.emcash.customerapp.ui.newPayment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.emcash.customerapp.R

class PaymentReceiptFragment:Fragment(R.layout.payment_reciept) {
    val viewModel:NewPaymentViewModel by activityViewModels()

}