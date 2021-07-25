package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.afterTextChanged
import com.emcash.customerapp.extensions.onDeletePressed
import com.emcash.customerapp.extensions.showKeyboard
import com.emcash.customerapp.extensions.showShortToast
import kotlinx.android.synthetic.main.emcash_pin.*
import timber.log.Timber

class EmcashPinFragment : Fragment(R.layout.emcash_pin), TextWatcher {

    val viewModel: NewPaymentViewModel by activityViewModels()

    var numTemp = ""

    private val editTextArray: ArrayList<EditText> = ArrayList(4)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("onViewCreated")

        editTextArray.add(et_pin_1)
        editTextArray.add(et_pin_2)
        editTextArray.add(et_pin_3)
        editTextArray.add(et_pin_4)
        editTextArray[0].requestFocus()
        editTextArray.forEachIndexed { index, editText ->
            editText.addTextChangedListener(this)

        }
        for(index in 0..editTextArray.size.minus(1)){
            editTextArray[index].setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    //backspace
                    if (index != 0) { //Don't implement for first digit
                        editTextArray[index - 1].requestFocus()
                        editTextArray[index - 1]
                            .setSelection(editTextArray[index - 1].length())
                    }
                }
                false
            }
        }






    }


    override fun afterTextChanged(s: Editable) {

        (0 until editTextArray.size)
            .forEach { i ->
                if (s === editTextArray[i].editableText) {

                    if (s.isBlank()) {
                        return
                    }
                    if (s.length >= 2) {//if more than 1 char
                        val newTemp = s.toString().substring(s.length - 1, s.length)
                        if (newTemp != numTemp) {
                            editTextArray[i].setText(newTemp)
                        } else {
                            editTextArray[i].setText(s.toString().substring(0, s.length - 1))
                        }
                    } else if (i != editTextArray.size - 1) { //not last char
                        editTextArray[i + 1].requestFocus()
                        editTextArray[i + 1].setSelection(editTextArray[i + 1].length())
                        return
                    } else

                    //will verify code the moment the last character is inserted and all digits have a number
                        verifyCode()


                }
            }
    }

    private fun verifyCode() {

    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        numTemp = s.toString()
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }


}