package com.emcash.customerapp.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class BankCardExpiryDateFormatter(
    val editText: EditText
):TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
        if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
            editText.setText(p0.toString() + "/")
            editText.setSelection(p0.lastIndex.plus(2))
        } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {
            /* commenting this as QA reported not to remove "/"
             * on back pressed
             * */
//            editText.setText(p0.toString().replace("/", ""))
            editText.setSelection(p0.lastIndex)

        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }
}