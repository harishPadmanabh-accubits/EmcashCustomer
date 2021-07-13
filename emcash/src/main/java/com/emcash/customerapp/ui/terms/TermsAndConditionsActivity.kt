package com.emcash.customerapp.ui.terms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.intro.IntroViewModel
import com.emcash.customerapp.ui.prepare.PrepareEmCashActivity
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*

class TermsAndConditionsActivity : AppCompatActivity() {
    private lateinit var viewModel: TermsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        initViewmodel()
        setupViews()
    }

    private fun setupViews() {
        tv_terms.movementMethod = ScrollingMovementMethod()
        btn_reject.setOnClickListener {
            onBackPressed()
        }
        btn_accept.setOnClickListener {
            openActivity(PrepareEmCashActivity::class.java)
        }

    }

    private fun initViewmodel() {
        viewModel = obtainViewModel(TermsViewModel::class.java)
    }
}