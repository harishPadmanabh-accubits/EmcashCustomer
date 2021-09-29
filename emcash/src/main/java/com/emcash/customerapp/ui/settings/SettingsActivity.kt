package com.emcash.customerapp.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.emcash.customerapp.EmCashCommunicationHelper
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.loadImageWithPlaceHolder
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.setlevel
import com.emcash.customerapp.ui.terms.TermsAndConditionsActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.SCREEN_SETTINGS
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    private val viewModel : SettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        iv_user_image.loadImageWithPlaceHolder(viewModel.profile?.profileImage,R.drawable.ic_profile_placeholder)
        fl_user_dp.setlevel(viewModel.profile?.ppp ?: 0)
        tv_user_name.text = viewModel.profile?.name
    } 

    fun onClick(view: View) {
        when(view.id){
            R.id.iv_back->{
                onBackPressed()
            }
            R.id.cv_goto_wallet->{
                openActivity(WalletActivity::class.java)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
            R.id.ll_terms->{
                openActivity(TermsAndConditionsActivity::class.java){
                    this.putInt(LAUNCH_SOURCE, SCREEN_SETTINGS)
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
            R.id.iv_edit->{
                EmCashCommunicationHelper.getParentListener().onEditProfile()
            }
        }
    }
}

