package com.emcash.customerapp.ui.loademcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.bankCard.PaymentByNewCardRequest
import com.emcash.customerapp.utils.KEY_TOPUP_AMOUNT
import com.emcash.customerapp.utils.KEY_TOPUP_DESC
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.activity_pay_with_new_card.*

class PayWithNewCardActivity : AppCompatActivity() {
    val viewModel: LoadEmCashViewModel by viewModels()

    var saveAfterTransaction: Boolean = false
    val amount by lazy {
        intent.getIntExtra(KEY_TOPUP_AMOUNT, 0)
    }
    val desc by lazy {
        intent.getStringExtra(KEY_TOPUP_DESC)
    }

    val loader by lazy {
        LoaderDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_with_new_card)

        observer()

        cb_saveCard.setOnCheckedChangeListener { buttonView, isChecked ->
            saveAfterTransaction = true
        }

        btn_continue.setOnClickListener {
            var cvv = et_cvv.text.toString()
            var expiryDate = et_expDate.text.toString()
            var cardHolderName = et_CardHolderName.text.toString()
            var atmNumber = et_atmNumber.text.toString()


            if (expiryDate.isEmpty() || cvv.isEmpty() || cardHolderName.isEmpty()) {
                showShortToast("Please enter all fields")
            } else {
                var billerId = "9000001"
                var customer = PaymentByNewCardRequest.Customer("540000010", 1)
                var amountAuthorized =
                    PaymentByNewCardRequest.AmountAuthorized("AED", amount.toString())
                var card = PaymentByNewCardRequest.Card(
                    cvv,
                    "manual",
                    expiryDate,
                    cardHolderName,
                    atmNumber, saveAfterTransaction
                )

                var paymentByNewCardRequest =
                    PaymentByNewCardRequest(
                        amountAuthorized,
                        billerId,
                        card,
                        customer,
                        desc.toString(),
                        null,
                        "07",
                        12.00,
                        true,
                        12.00,
                        false
                    )
                viewModel.paymentByNewCard(paymentByNewCardRequest)
            }


        }

    }

    private fun observer() {
        viewModel.apply {
            paymentByNewCardStatus.observe(
                this@PayWithNewCardActivity,
                androidx.lifecycle.Observer {
                    when (it.status) {
                        ApiCallStatus.LOADING -> {
                            loader.showLoader()
                        }
                        ApiCallStatus.SUCCESS -> {
                            loader.hideLoader()
                            showShortToast("Emcash Loaded")


                        }
                        ApiCallStatus.ERROR -> {
                            loader.hideLoader()
                            showShortToast(it.errorMessage)

                        }
                    }

                })
        }
    }


}