package com.emcash.customerapp.ui.loadEmcash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.bankCard.PaymentByNewCardRequest
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.activity_pay_with_new_card.*
import timber.log.Timber

class PayWithNewCardActivity : AppCompatActivity() {
    private val viewModel: LoadEmCashViewModel by viewModels()
    private var saveAfterTransaction: Boolean = false

    private val amount by lazy {
        intent.getStringExtra(KEY_TOPUP_AMOUNT) ?: "0.00"
    }

    private val desc by lazy {
        intent.getStringExtra(KEY_TOPUP_DESC) ?: " "
    }

    private val loader by lazy {
        LoaderDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_with_new_card)
        Timber.e("Amount in new card $amount  desc $desc")
        setupViews()
        observe()
    }

    private fun setupViews() {
        iv_back.setOnClickListener {
            onBackPressed()
        }
        btn_continue.setOnClickListener {
            val cvv = et_cvv.text.toString()
            val expiryDate = et_expDate.text.toString().replace("/", "")
            val cardHolderName = et_CardHolderName.text.toString()
            val atmNumber = et_atmNumber.text.toString().filter { !it.isWhitespace() }
            if (expiryDate.isEmpty() || cvv.isEmpty() || cardHolderName.isEmpty())
                showShortToast(getString(R.string.enter_all_field))
            else if (amount.toDouble() < 0.00)
                showShortToast(getString(R.string.invalid_amount))
            else
                payWithNewCard(cvv, expiryDate, cardHolderName, atmNumber)
        }
        cb_saveCard.setOnCheckedChangeListener { _, isChecked ->
            saveAfterTransaction = isChecked
        }
        et_atmNumber.addTextChangedListener(BankCardNumberFormatter())
        et_expDate.apply {
            addTextChangedListener(BankCardExpiryDateFormatter(this))
        }
    }

    override fun onResume() {
        super.onResume()
        et_atmNumber.requestFocus()
    }

    private fun payWithNewCard(
        cvv: String,
        expiryDate: String,
        cardHolderName: String,
        atmNumber: String
    ) {
        val billerId = "12001"
        val customerId = viewModel.syncManager.switchAccountData?.phoneNumber
        val customer = customerId?.let { PaymentByNewCardRequest.Customer(it, 1) }
        val amountAuthorized =
            PaymentByNewCardRequest.AmountAuthorized("AED", amount)
        val card = PaymentByNewCardRequest.Card(
            cvv,
            "manual",
            expiryDate,
            cardHolderName,
            atmNumber, saveAfterTransaction
        )

        val paymentByNewCardRequest =
            customer?.let {
                PaymentByNewCardRequest(
                    amountAuthorized,
                    billerId,
                    card,
                    it,
                    desc,
                    null,
                    "411111",
                    12.00,
                    true,
                    12.00,
                    false
                )
            }
        paymentByNewCardRequest?.let { viewModel.paymentByNewCard(it) }

    }

    private fun observe() {
        viewModel.apply {
            paymentByNewCardStatus.observe(
                this@PayWithNewCardActivity,
                Observer {
                    when (it.status) {
                        ApiCallStatus.LOADING -> {
                            loader.showLoader()
                        }
                        ApiCallStatus.SUCCESS -> {
                            loader.hideLoader()
                            if (it.data?.data?.decision.equals(TOPUP_AUTH_REQUIRED)) {
                                openActivity(EmpayWebViewActivity::class.java) {
                                    this.putString(
                                        KEY_URL3D,
                                        it.data?.data?.payerAuthentication?.url3D.toString()
                                    )
                                    this.putString(KEY_TOPUP_ORDERID, it.data?.data?.orderId)
                                    this.putString(KEY_TOPUP_SESSIONID, it.data?.data?.sessionId)
                                    this.putString(KEY_TOPUP_AMOUNT, amount)
                                    this.putString(KEY_TOPUP_DESC, desc)
                                }

                            } else {
                                showShortToast(getString(R.string.emcash_load_success))
                                gotoWalletScreen()
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

    private fun gotoWalletScreen() {
        openActivity(WalletActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onBackPressed() {
        openActivity(TransactionActivity::class.java) {
            this.putString(KEY_TOPUP_AMOUNT, amount)
            this.putString(KEY_TOPUP_DESC, desc)
        }
    }


}