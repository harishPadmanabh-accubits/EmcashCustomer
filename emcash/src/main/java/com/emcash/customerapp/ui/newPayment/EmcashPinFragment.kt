package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.afterTextChanged
import com.emcash.customerapp.extensions.onDeletePressed
import com.emcash.customerapp.extensions.showKeyboard
import com.emcash.customerapp.extensions.showShortToast
import kotlinx.android.synthetic.main.emcash_pin.*
import timber.log.Timber

class EmcashPinFragment : Fragment(R.layout.emcash_pin) {

    val viewModel: NewPaymentViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("onViewCreated")
        configurePinViews()
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().showKeyboard(et_pin_1)
    }

    private fun configurePinViews() {
        viewModel.apply {
            et_pin_1.requestFocus()
            et_pin_1.afterTextChanged {
                if (it.length == 1) {
                    addToPin(it)
                    et_pin_2.requestFocus()
                    et_pin_1.setBackgroundResource(R.drawable.pin_filled)
                } else if (it.length == 2) {
                    et_pin_2.setText(it.last().toString())
                    et_pin_2.setBackgroundResource(R.drawable.pin_filled)

                    et_pin_1.setText(it.first().toString())
                    et_pin_2.requestFocus()
                    et_pin_2.setSelection(1)
                    et_pin_1.setBackgroundResource(R.drawable.pin_filled)


                }
            }
            et_pin_2.afterTextChanged {
                if (it.length == 1) {
                    addToPin(it)
                    et_pin_3.requestFocus()
                    et_pin_2.setBackgroundResource(R.drawable.pin_filled)

                } else if (it.length == 2) {
                    et_pin_3.setText(it.last().toString())
                    et_pin_3.setBackgroundResource(R.drawable.pin_filled)

                    et_pin_2.setText(it.first().toString())
                    et_pin_3.requestFocus()
                    et_pin_3.setSelection(1)
                    et_pin_2.setBackgroundResource(R.drawable.pin_filled)


                }
            }
            et_pin_3.afterTextChanged {
                if (it.length == 1) {
                    addToPin(it)
                    et_pin_4.requestFocus()
                    et_pin_3.setBackgroundResource(R.drawable.pin_filled)

                } else if (it.length == 2) {
                    et_pin_4.setText(it.last().toString())
                    et_pin_4.setBackgroundResource(R.drawable.pin_filled)

                    et_pin_3.setText(it.first().toString())
                    et_pin_4.requestFocus()
                    et_pin_4.setSelection(1)
                    validate()
                    et_pin_3.setBackgroundResource(R.drawable.pin_filled)


                }
            }

            et_pin_4.afterTextChanged {
                et_pin_4.setBackgroundResource(R.drawable.pin_filled)
                validate()
            }

            et_pin_4.onDeletePressed {

                et_pin_4.text.clear()
                et_pin_4.setBackgroundResource(R.drawable.pin_empty)

                removeLastFromPin()
                et_pin_3.requestFocus()
                et_pin_3.setSelection(et_pin_3.text.lastIndex.plus(1))

            }

            et_pin_3.onDeletePressed {
                et_pin_3.text.clear()
                et_pin_3.setBackgroundResource(R.drawable.pin_empty)

                removeLastFromPin()
                et_pin_2.requestFocus()
                et_pin_2.setSelection(et_pin_2.text.lastIndex.plus(1))
            }
            et_pin_2.onDeletePressed {
                et_pin_2.text.clear()
                et_pin_2.setBackgroundResource(R.drawable.pin_empty)

                removeLastFromPin()
                et_pin_1.requestFocus()
                et_pin_1.setSelection(et_pin_1.text.lastIndex.plus(1))
            }

            et_pin_1.onDeletePressed {
                et_pin_1.text.clear()
                et_pin_1.setBackgroundResource(R.drawable.pin_empty)

                et_pin_1.requestFocus()
                et_pin_1.setSelection(0)
            }

        }

    }

    private fun validate() {
        val enteredPin = et_pin_1.text.toString().plus(et_pin_2.text.toString()).plus(et_pin_3.text.toString()).plus(et_pin_4.text.toString())
        Timber.e("entered Pin $enteredPin")
        if(enteredPin.length==4){
            if(viewModel.validPin == enteredPin){
                requireActivity().showShortToast("Valid Pin")
            }
            else{
                requireActivity().showShortToast("InvalidPin")
            }

        }

    }

    private fun refreshWithShake(){

    }

}