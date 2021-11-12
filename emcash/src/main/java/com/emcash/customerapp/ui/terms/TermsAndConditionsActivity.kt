package com.emcash.customerapp.ui.terms

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.MotionEvent.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.text.toSpannable
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiCallStatus.*
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.ui.intro.IntroActivity
import com.emcash.customerapp.ui.intro.IntroViewModel
import com.emcash.customerapp.ui.prepare.PrepareEmCashActivity
import com.emcash.customerapp.utils.INTRO_SCREEN
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.LoaderDialog
import com.emcash.customerapp.utils.SCREEN_SETTINGS
import kotlinx.android.synthetic.main.terms_v2.*

class TermsAndConditionsActivity : AppCompatActivity() {

    private val source by lazy {
        intent.getIntExtra(LAUNCH_SOURCE, INTRO_SCREEN)
    }

    private val viewModel: TermsViewModel by viewModels()

    val loader by lazy {
        LoaderDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.terms_v2)
        configureCTAs()
        viewModel.getTnc()
        observe()

    }

    private fun observe() {
        viewModel.apply {
           tncData.observe(this@TermsAndConditionsActivity, Observer {
               when(it.status){
                   LOADING -> loader.show()
                   SUCCESS->{
                       val tnc = it.data
                           renderTerms(tnc)
                   }
                   ERROR->{
                       loader.dismiss()
                       showShortToast(it.errorMessage)
                   }
               }
           })
        }
    }

    private fun renderTerms(tnc: String?) {
        tv_terms.text = tnc
        tv_terms.movementMethod = ScrollingMovementMethod()
        loader.dismiss()
    }

    private fun configureCTAs() {
        if(source== SCREEN_SETTINGS || viewModel.syncManager.tncStatus==TncStatus.ACCEPTED){
            btn_accept.hide()
            btn_reject.hide()
            tv_terms.movementMethod = ScrollingMovementMethod()
        }else{
            setupViews()
        }
    }

    private fun setupViews() {
        tv_terms.movementMethod = ScrollingMovementMethod()
        btn_reject.setOnClickListener {
            viewModel.syncManager.tncStatus = TncStatus.REJECTED
            openActivity(IntroActivity::class.java)
        }
        btn_accept.setOnClickListener {
            viewModel.syncManager.tncStatus = TncStatus.ACCEPTED
            openActivity(PrepareEmCashActivity::class.java)
            finish()
        }

    }

    override fun onBackPressed() {
        if(source== INTRO_SCREEN)
            openActivity(INTRO_SCREEN::class.java)
        else
            super.onBackPressed()
    }

}