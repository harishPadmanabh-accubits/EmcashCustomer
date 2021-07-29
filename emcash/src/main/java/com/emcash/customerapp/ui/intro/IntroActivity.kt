package com.emcash.customerapp.ui.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.intro.adapter.IntroPagerAdapter
import com.emcash.customerapp.ui.terms.TermsAndConditionsActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

     val viewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
     //   initViewModel()
        setupObservers()
    }



    private fun setupObservers() {
        viewModel.apply {
            screenPosition.observe(this@IntroActivity, Observer {
                when (it) {
                    0 ->openFirstIntroFragment()
                    1 -> openSecondIntroFragment()
                    2 -> openThirdIntroFragment()
                    3 -> openFourthIntroFragment()
                    4 -> {
                        openActivity(TermsAndConditionsActivity::class.java)
                        finish()
                    }
                }
            })
        }
    }

    private fun openFirstIntroFragment(){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FirstIntroFragment>(R.id.container)
        }
    }
    private fun openSecondIntroFragment(){
        supportFragmentManager.commit {
            this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
            setReorderingAllowed(true)
            replace<SecondIntroFragment>(R.id.container)
        }
    }
     fun openThirdIntroFragment(){
        supportFragmentManager.commit {
           // this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
           // setReorderingAllowed(true)
            replace<ThirdIntroFragment>(R.id.container)
        }
    }
     fun openFourthIntroFragment(){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
            replace<FourthIntroFragment>(R.id.container)
        }
    }

    private fun initViewModel() {
       // viewModel = obtainViewModel(IntroViewModel::class.java)
    }


}