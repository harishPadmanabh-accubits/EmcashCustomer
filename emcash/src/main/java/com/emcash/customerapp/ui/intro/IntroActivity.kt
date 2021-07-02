package com.emcash.customerapp.ui.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.intro.adapter.IntroPagerAdapter
import com.emcash.customerapp.ui.terms.TermsAndConditionsActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    private lateinit var viewModel: IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        initViewModel()
        setupObservers()
        setupViewPager()
    }

    private fun setupViewPager() {
        vp_intro_pager.isUserInputEnabled = false
        vp_intro_pager.adapter = IntroPagerAdapter(this)
    }

    private fun setupObservers() {
        viewModel.apply {
            screenPosition.observe(this@IntroActivity, Observer {
                when (it) {
                    0 -> vp_intro_pager.currentItem = 0
                    1 -> vp_intro_pager.currentItem = 1
                    2 -> vp_intro_pager.currentItem = 2
                    3 -> vp_intro_pager.currentItem = 3
                    4 -> {
                        openActivity(TermsAndConditionsActivity::class.java)
                        finish()
                    }
                }
            })
        }
    }

    private fun initViewModel() {
        viewModel = obtainViewModel(IntroViewModel::class.java)
    }


}