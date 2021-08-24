package com.emcash.emcashcustomer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.emcash.customerapp.EmCashHelper
import com.emcash.customerapp.EmCashListener
import com.emcash.customerapp.extensions.afterTextChanged
import com.emcash.customerapp.extensions.onDeletePressed
import com.emcash.customerapp.extensions.showKeyboard
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens
import kotlinx.android.synthetic.main.activity_pin_screen.*

class PinScreen : AppCompatActivity(),EmCashListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_screen)
        val imm =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(cl_root, InputMethodManager.SHOW_IMPLICIT)
        configurePinViews()

    }
    override fun onResume() {
        super.onResume()
        refresh( arrayListOf<EditText>(et_pin_1, et_pin_2, et_pin_3, et_pin_4))
    }

    private fun configurePinViews() {
            et_pin_1.requestFocus()
            et_pin_1.afterTextChanged {
                if (it.length == 1) {
                    et_pin_2.requestFocus()
                    et_pin_1.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)
                } else if (it.length == 2) {
                    et_pin_2.setText(it.last().toString())
                    et_pin_2.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)

                    et_pin_1.setText(it.first().toString())
                    et_pin_2.requestFocus()
                    et_pin_2.setSelection(1)
                    et_pin_1.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)


                }
            }
            et_pin_2.afterTextChanged {
                if (it.length == 1) {
                    et_pin_3.requestFocus()
                    et_pin_2.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)

                } else if (it.length == 2) {
                    et_pin_3.setText(it.last().toString())
                    et_pin_3.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)

                    et_pin_2.setText(it.first().toString())
                    et_pin_3.requestFocus()
                    et_pin_3.setSelection(1)
                    et_pin_2.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)


                }
            }
            et_pin_3.afterTextChanged {
                if (it.length == 1) {
                    et_pin_4.requestFocus()
                    et_pin_3.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)

                } else if (it.length == 2) {
                    et_pin_4.setText(it.last().toString())
                    et_pin_4.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)

                    et_pin_3.setText(it.first().toString())
                    et_pin_4.requestFocus()
                    et_pin_4.setSelection(1)
                    validate()
                    et_pin_3.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)


                }
            }

            et_pin_4.afterTextChanged {
                et_pin_4.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_filled)
                validate()
            }

            et_pin_4.onDeletePressed {

                et_pin_4.text.clear()
                et_pin_4.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_empty)

                et_pin_3.requestFocus()
                et_pin_3.setSelection(et_pin_3.text.lastIndex.plus(1))

            }

            et_pin_3.onDeletePressed {
                et_pin_3.text.clear()
                et_pin_3.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_empty)

                et_pin_2.requestFocus()
                et_pin_2.setSelection(et_pin_2.text.lastIndex.plus(1))
            }
            et_pin_2.onDeletePressed {
                et_pin_2.text.clear()
                et_pin_2.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_empty)

                et_pin_1.requestFocus()
                et_pin_1.setSelection(et_pin_1.text.lastIndex.plus(1))
            }

            et_pin_1.onDeletePressed {
                et_pin_1.text.clear()
                et_pin_1.setBackgroundResource(com.emcash.customerapp.R.drawable.pin_empty)

                et_pin_1.requestFocus()
                et_pin_1.setSelection(0)
            }


    }
    private fun validate() {
        val enteredPin =
            et_pin_1.text.toString().plus(et_pin_2.text.toString()).plus(et_pin_3.text.toString())
                .plus(et_pin_4.text.toString())
        if (enteredPin.length == 4) {
            if ("0000"== enteredPin) {
                onValidPin()
            } else {
                showShortToast("You entered an Incorrect Pin")
                refreshWithShake()
            }

        }

    }

    private fun refreshWithShake() {

        val shake = AnimationUtils.loadAnimation(this,
            com.emcash.customerapp.R.anim.shake
        )
        val pinVIews = arrayListOf<EditText>(et_pin_1, et_pin_2, et_pin_3, et_pin_4)
        et_pin_1.startAnimation(shake)
        et_pin_2.startAnimation(shake)
        et_pin_3.startAnimation(shake)
        et_pin_4.startAnimation(shake)
        et_pin_4.postOnAnimation {
            refresh(pinVIews)
        }


    }

    private fun refresh(pinVIews: ArrayList<EditText>) {
        pinVIews.forEach {
            it.apply {
                text.clear()
                setBackgroundResource(com.emcash.customerapp.R.drawable.pin_empty)
            }
            pinVIews[0].requestFocus()
        }

    }

    private fun onValidPin(){
        EmCashHelper(applicationContext,this).proceedToTransfer()
    }

    override fun onLoginSuccess(status: Boolean) {

    }


}