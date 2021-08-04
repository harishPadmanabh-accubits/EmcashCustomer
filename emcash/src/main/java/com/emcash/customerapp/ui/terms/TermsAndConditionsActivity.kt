package com.emcash.customerapp.ui.terms

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.MotionEvent.*
import androidx.annotation.RequiresApi
import androidx.core.text.toSpannable
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.intro.IntroActivity
import com.emcash.customerapp.ui.intro.IntroViewModel
import com.emcash.customerapp.ui.prepare.PrepareEmCashActivity
import com.emcash.customerapp.utils.INTRO_SCREEN
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.SCREEN_SETTINGS
import kotlinx.android.synthetic.main.terms_v2.*

class TermsAndConditionsActivity : AppCompatActivity() {

    private val source by lazy {
        intent.getIntExtra(LAUNCH_SOURCE, INTRO_SCREEN)
    }

    private lateinit var viewModel: TermsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.terms_v2)
        configureCTAs()
        initViewmodel()

    }

    private fun configureCTAs() {
        if(source== SCREEN_SETTINGS){
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
            openActivity(IntroActivity::class.java)
        }
        btn_accept.setOnClickListener {
            openActivity(PrepareEmCashActivity::class.java)
            finish()
        }

    }

    private fun initViewmodel() {
        viewModel = obtainViewModel(TermsViewModel::class.java)
    }
}