package com.emcash.customerapp.ui.settings

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.EmCashCommunicationHelper
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.terms.TermsAndConditionsActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.SCREEN_SETTINGS
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.fl_tint
import kotlinx.android.synthetic.main.layout_logout.*

class SettingsActivity : AppCompatActivity() {
    private val viewModel : SettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        iv_user_image.loadImageWithPlaceHolder(viewModel.profile?.profileImage,R.drawable.ic_profile_placeholder)
        fl_user_dp.setlevel(viewModel.profile?.level ?: 0)
        tv_user_name.text = viewModel.profile?.name
        observe()
        configureLogoutBottomSheet()
    }

    private fun configureLogoutBottomSheet() {
        iv_close.setOnClickListener {
            viewModel.logoutBottomSheetVisibility.value = false
        }
        btn_logout.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        viewModel.syncManager.logout()
        finish()
    }

    private fun observe() {
        viewModel.logoutBottomSheetVisibility.observe(this, Observer {
            when(it){
                true->showLogoutBottomSheet()
                false->hideLogoutBottomSheet()
            }
        })
    }

    private fun showLogoutBottomSheet() {
        val transition: Transition = Slide(Gravity.BOTTOM);
        transition.duration = 600
        transition.addTarget(fl_logout)
        transition.addListener(object : Transition.TransitionListener{
            override fun onTransitionEnd(p0: Transition?) {
            }

            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
                showTint()
            }
        })
        TransitionManager.beginDelayedTransition(fl_logout, transition)
        fl_logout.show()


    }

    private fun hideLogoutBottomSheet() {
        val transition: Transition = Slide(Gravity.BOTTOM);
        transition.duration = 500
        transition.addTarget(fl_logout)
        transition.addListener(object : Transition.TransitionListener{
            override fun onTransitionEnd(p0: Transition?) {
            }

            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
                hideTint()
            }
        })
        TransitionManager.beginDelayedTransition(fl_logout, transition)
        fl_logout.hide()

    }

    fun onClick(view: View) {
        when(view.id){
            R.id.iv_back->{
               onBackPressed()
            }
            R.id.cv_goto_wallet->{
                openActivity(WalletActivity::class.java)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()

            }
            R.id.ll_terms->{
                openActivity(TermsAndConditionsActivity::class.java){
                    this.putInt(LAUNCH_SOURCE, SCREEN_SETTINGS)
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()

            }
            R.id.ll_logout->{
                viewModel.logoutBottomSheetVisibility.value = true
            }
            R.id.iv_edit->{
                EmCashCommunicationHelper.getParentListener().onEditProfile()
            }


        }
    }

    fun showTint() {
        ObjectAnimator.ofFloat(fl_tint, "alpha", 90f).apply {
            duration = 500
            start()
        }
    }

    fun hideTint(){
        ObjectAnimator.ofFloat(fl_tint, "alpha", 0f).apply {
            duration = 500
            start()
        }
    }

    override fun onBackPressed() {
        if(viewModel.logoutBottomSheetVisibility.value == true)
            viewModel.logoutBottomSheetVisibility.value =false
        else{
            openActivity(HomeActivity::class.java)
            finish()
        }


    }
}

