package com.emcash.customerapp.ui.loadEmcash
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.emcashmerchant.ui.transactionActivity.adapter.CardsAdapter
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.bankCard.BankCardsListingResponse
import com.emcash.customerapp.model.bankCard.PaymentByExistingCardRequest
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.DEFAULT_LOAD_EMCASH_DESCRIPTION
import com.emcash.customerapp.utils.KEY_TOPUP_AMOUNT
import com.emcash.customerapp.utils.KEY_TOPUP_DESC
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.activity_transaction.*
import timber.log.Timber

class TransactionActivity : AppCompatActivity(), CardsAdapter.CardsItemClickListener {

    val viewModel: LoadEmCashViewModel by viewModels()
    var useExistingCard: String? = null

    val amount by lazy {
        intent.getStringExtra(KEY_TOPUP_AMOUNT) ?: "0.00"
    }

    val desc by lazy {
        intent.getStringExtra(KEY_TOPUP_DESC) ?: "0.00"
    }

    val loader by lazy {
        LoaderDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        viewModel.getBankCards()
        setupViews()
        setupObservers()
    }

    private fun setupViews() {

        tab_empay.setOnClickListener {
            viewModel._accountMode.value = AccountMode.EMPAY
        }
        tab_bank_card.setOnClickListener {
            viewModel._accountMode.value = AccountMode.BANK_CARD
        }

        cl_addCard.setOnClickListener {
            openActivity(PayWithNewCardActivity::class.java) {
                putString(KEY_TOPUP_AMOUNT,amount)
                putString(KEY_TOPUP_DESC,desc)
            }
        }

        btn_continue.setOnClickListener {

            val phoneNumber = viewModel.syncManager.switchAccountData?.phoneNumber
            val customer = phoneNumber?.let { _phone -> PaymentByExistingCardRequest.Customer(_phone, 1) }
            val amountDouble =
                PaymentByExistingCardRequest.Amount("AED", amount)
            val paymentByExisitingCardRequest =
                customer?.let { _customer ->
                    PaymentByExistingCardRequest(
                        amountDouble,
                        "12001",
                        null,
                        _customer,
                        if (desc.isNullOrEmpty()) DEFAULT_LOAD_EMCASH_DESCRIPTION else desc,
                        useExistingCard.toString(), "07", 12.00, true, 12.00, true
                    )
                }

            paymentByExisitingCardRequest?.let { it1 -> viewModel.paymentByExistingCard(it1) }
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }

        tv_info_currency.text = amount.replace(".00","")
        iv_user_dp.setImage(viewModel.syncManager.profileDetails?.profileImage)
    }

    private fun setupObservers() {
        viewModel._accountMode.observe(this, Observer {
            when (it) {
                AccountMode.EMPAY -> selectEmpayTab()
                AccountMode.BANK_CARD -> selectBankTab()
            }
        })
        viewModel.bankCards.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                ApiCallStatus.LOADING -> {
                    loader.showLoader()
                }
                ApiCallStatus.SUCCESS -> {
                    loader.hideLoader()

                    rv_bankCard.apply {
                        adapter = it.data?.let { data ->
                            CardsAdapter(
                                data.cards,
                                this@TransactionActivity
                            )
                        }
                    }

                }
                ApiCallStatus.ERROR -> {
                    loader.hideLoader()
                    showShortToast(it.errorMessage)

                }
            }

        })

        viewModel.paymentByExistingCardStatus.observe(this, androidx.lifecycle.Observer {
            try{

                when (it.status) {
                    ApiCallStatus.LOADING -> {
                        loader.showLoader()
                    }
                    ApiCallStatus.SUCCESS -> {
                        loader.hideLoader()
                        showShortToast(getString(R.string.emcash_load_success))
                        gotoWalletScreen()
                    }
                    ApiCallStatus.ERROR -> {
                        loader.hideLoader()
                        showShortToast(it.errorMessage)

                    }
                }

            }catch (e:Exception){
                Timber.e("Exception paymentByExistingCardStatus $e")
            }

        })


    }

    private fun selectEmpayTab() {
        tab_empay.setBackgroundResource(R.drawable.blue_stroke_light_blue_fill_round_bg)
        iv_empay_selected.show()

        tab_bank_card.setBackgroundResource(R.drawable.grey_rounded_bg)
        iv_bank_selected.hide()
        ll_bankCards.visibility = View.GONE



    }

    private fun selectBankTab() {
        tab_empay.setBackgroundResource(R.drawable.grey_rounded_bg)
        iv_empay_selected.hide()

        tab_bank_card.setBackgroundResource(R.drawable.blue_stroke_light_blue_fill_round_bg)
        iv_bank_selected.show()
        ll_bankCards.visibility = View.VISIBLE

    }


    private fun gotoWalletScreen() {
        openActivity(WalletActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onCardClicked(card: BankCardsListingResponse.Data.Card) {
        useExistingCard = card.token
    }
}

