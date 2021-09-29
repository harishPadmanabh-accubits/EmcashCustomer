package com.emcash.customerapp.ui.loademcash
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
import com.emcash.customerapp.model.dummyAccounts
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.DEFAULT_LOAD_EMCASH_DESCRIPTION
import com.emcash.customerapp.utils.KEY_TOPUP_AMOUNT
import com.emcash.customerapp.utils.KEY_TOPUP_DESC
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.activity_transaction.*
import timber.log.Timber
import java.text.DecimalFormat

class TransactionActivity : AppCompatActivity(), CardsAdapter.CardsItemClickListener {

    val viewModel: LoadEmCashViewModel by viewModels()
    var useExistingCard: String? = null

    val amount by lazy {
        intent.getDoubleExtra(KEY_TOPUP_AMOUNT, 0.00)
    }

    val desc by lazy {
        intent.getStringExtra(KEY_TOPUP_DESC)
    }

    val loader by lazy {
        LoaderDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        rv_accounts.apply {
            adapter = AccountsAdapter(dummyAccounts)
        }

        viewModel.bankCardListing()

        tab_empay.setOnClickListener {
            viewModel._accountMode.value = AccountMode.EMPAY
            ll_bankCards.visibility = View.GONE

        }
        tab_bank_card.setOnClickListener {
            viewModel._accountMode.value = AccountMode.BANK_CARD
            ll_bankCards.visibility = View.VISIBLE
        }

        cl_addCard.setOnClickListener {
            openActivity(PayWithNewCardActivity::class.java) {
                KEY_TOPUP_AMOUNT to amount
                KEY_TOPUP_DESC to desc
            }

        }
        btn_continue.setOnClickListener {
            val customer = PaymentByExistingCardRequest.Customer("509842776", 1)
            val amountDouble =
                PaymentByExistingCardRequest.Amount("AED", DecimalFormat("0.00").format(amount))
            val paymentByExisitingCardRequest =
                PaymentByExistingCardRequest(
                    amountDouble,
                    "12001",
                    null,
                    customer,
                    if (desc.isNullOrEmpty()) DEFAULT_LOAD_EMCASH_DESCRIPTION else desc,
                    useExistingCard.toString(), "07", 12.00, true, 12.00, true
                )

            viewModel.paymentByExistingCard(paymentByExisitingCardRequest)

        }

        iv_back.setOnClickListener {
            onBackPressed()
        }

        tv_info_currency.text = DecimalFormat("0.00").format(amount).toString()
        iv_user_dp.setImage(viewModel.syncManager.profileDetails?.profileImage)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel._accountMode.observe(this, Observer {
            when (it) {
                AccountMode.EMPAY -> selectEmpayTab()
                AccountMode.BANK_CARD -> selectBankTab()
            }
        })
        viewModel.bankCardsStatus.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                ApiCallStatus.LOADING -> {
                    loader.showLoader()
                }
                ApiCallStatus.SUCCESS -> {
                    loader.hideLoader()

                    rv_bankCard.apply {
                        adapter = it.data?.let { it1 ->
                            CardsAdapter(
                                it1.cards,
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
                       showShortToast("Emcash Loaded")
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

    }

    private fun selectBankTab() {
        tab_empay.setBackgroundResource(R.drawable.grey_rounded_bg)
        iv_empay_selected.hide()

        tab_bank_card.setBackgroundResource(R.drawable.blue_stroke_light_blue_fill_round_bg)
        iv_bank_selected.show()
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

