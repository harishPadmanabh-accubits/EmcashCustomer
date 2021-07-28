package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.showKeyboard
import kotlinx.android.synthetic.main.transfer_fragment.*

class TransferFragment:Fragment(R.layout.transfer_fragment) {

    val viewModel:NewPaymentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_value.requestFocus()
        fab_transfer.setOnClickListener {
            viewModel.gotoScreen(NewPaymentScreens.PIN)
        }
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().showKeyboard(et_value)
    }
}