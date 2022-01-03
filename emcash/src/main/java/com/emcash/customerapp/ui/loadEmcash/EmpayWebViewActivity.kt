package com.emcash.customerapp.ui.loadEmcash

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.bankCard.PayerAuthenticatorRequest
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.activity_empay_web_view.*
import timber.log.Timber

class EmpayWebViewActivity : AppCompatActivity() {

    val viewModel: LoadEmCashViewModel by viewModels()
    val amount by lazy {
        intent.getStringExtra(KEY_TOPUP_AMOUNT) ?: "0.00"
    }

    val desc by lazy {
        intent.getStringExtra(KEY_TOPUP_DESC) ?: " "
    }
    val sessionId by lazy {
        intent.getStringExtra(KEY_TOPUP_SESSIONID)
    }
    val orderId by lazy {
        intent.getStringExtra(KEY_TOPUP_ORDERID)
    }

    val url3D by lazy {
        intent.getStringExtra(KEY_URL3D)
    }


    val loader by lazy {
        LoaderDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empay_web_view)


        Timber.e("urlwebview $url3D")

        wv_empay.settings.javaScriptEnabled = true
        wv_empay.loadUrl(url3D.toString())

        wv_empay.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return  false
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (url.toString().contains("success")) {

                    var payerAuthenticator: PayerAuthenticatorRequest =
                        PayerAuthenticatorRequest(
                            amount,
                            "",
                            "411111",
                            12.00,
                            12.00,
                            orderId.toString(),
                            sessionId.toString()
                        )

                    viewModel.authenticatePayer(payerAuthenticator)

                }else if(url.toString().contains("Error")){
                    showShortToast("Payment Error")
                    openActivity(WalletActivity::class.java)

                }
            }


        }

        observer()
    }

    private fun observer() {
        viewModel.apply {
            payerAuthenticatorStatus.observe(this@EmpayWebViewActivity, androidx.lifecycle.Observer {
                when (it.status) {
                    ApiCallStatus.LOADING -> {
                        loader.showLoader()
                    }

                    ApiCallStatus.SUCCESS -> {
                        loader.hideLoader()

                        openActivity(WalletActivity::class.java)
                        showShortToast("Emcash loaded")
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