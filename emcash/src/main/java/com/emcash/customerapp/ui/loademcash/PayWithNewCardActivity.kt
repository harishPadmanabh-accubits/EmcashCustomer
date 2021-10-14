package com.emcash.customerapp.ui.loademcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.bankCard.PaymentByNewCardRequest
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.*
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

        et_atmNumber.addTextChangedListener(BankCardNumberFormatter())
        et_expDate.apply {
            addTextChangedListener(BankCardExpiryDateFormatter(this))
        }

        observer()

        cb_saveCard.setOnCheckedChangeListener { buttonView, isChecked ->
            saveAfterTransaction = true
        }

        btn_continue.setOnClickListener {
            val cvv = et_cvv.text.toString()
            val expiryDate = et_expDate.text.toString().replace("/","")
            val cardHolderName = et_CardHolderName.text.toString()
            val atmNumber = et_atmNumber.text.toString().filter{!it.isWhitespace()}


            if (expiryDate.isEmpty() || cvv.isEmpty() || cardHolderName.isEmpty()) {
                showShortToast("Please enter all fields")
            } else {
                var billerId = "12001"
                var customer = PaymentByNewCardRequest.Customer("509842776", 1)
                var amountAuthorized =
                    PaymentByNewCardRequest.AmountAuthorized("AED", "999.00")
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
                        "",
                        null,
                        "411111",
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
                            if (it.data?.data?.decision.equals("PAYER_AUTH_REQUIRED")) {

                                openActivity(EmpayWebViewActivity::class.java) {
                                    this.putString(KEY_URL3D, it.data?.data?.payerAuthentication?.url3D.toString())
                                    this.putString(KEY_TOPUP_ORDERID, it.data?.data?.orderId)
                                    this.putString(KEY_TOPUP_SESSIONID,  it.data?.data?.sessionId)
                                    this.putInt(KEY_TOPUP_AMOUNT, amount)
                                    this.putString(KEY_TOPUP_DESC, desc)
                                }

                            } else {
                                showShortToast("Emcash loaded")
                                openActivity(WalletActivity::class.java)
                            }


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