package com.emcash.customerapp.ui.wallet

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.ui.convertEmcash.ConvertEmcashActivity
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.loadEmcash.LoadEmcashActivity
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.LoaderDialog
import com.emcash.customerapp.utils.SCREEN_WALLET
import kotlinx.android.synthetic.main.wallet_screen_v2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect

class WalletActivity : AppCompatActivity() {

    val pagedAdapter by lazy {
        WalletPagedAdapter()
    }

    val loader by lazy {
        LoaderDialog(this)
    }

    private val viewModel:WalletViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_screen_v2)
        configureViews()

        lifecycleScope.async (Dispatchers.Main) {
            renderWalletDetails()
        }

        lifecycleScope.async {
            viewModel.walletActivities.collect {
                pagedAdapter.submitData(it)
            }
        }
    }

    private fun configureViews() {

        iv_back.setOnClickListener {
            onBackPressed()
        }

        btn_load_emcash.setOnClickListener {
            openActivity(LoadEmcashActivity::class.java){
                this.putInt(LAUNCH_SOURCE, SCREEN_WALLET)
            }
        }
        btn_convert.setOnClickListener {
            openActivity(ConvertEmcashActivity::class.java)
        }

        rv_activities.apply {
            layoutManager = LinearLayoutManager(this@WalletActivity,RecyclerView.VERTICAL,false)
            adapter = pagedAdapter

        }

    }

    private fun renderWalletDetails() {
        viewModel.apply {
            syncProfileFromServer().observe(this@WalletActivity, Observer {
                when(it.status){
                    ApiCallStatus.SUCCESS->{
                        val profile= it.data
                        profile?.let{
                            renderProfileDetails(it)
                        }
                    }
                    ApiCallStatus.ERROR->{
                        loadProfileDetailsFromCache()
                        showShortToast(it.errorMessage)
                    }
                }
            })
        }
    }

    private fun loadProfileDetailsFromCache() {
        viewModel.homeRepository.syncManager.profileDetails?.let {
            renderProfileDetails(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderProfileDetails(profile: ProfileDetailsResponse.Data) {
        appCompatImageView.setImage(profile.profileImage)
        tv_balance.text = profile.wallet.amount.toString()
        val safeboxId = profile.wallet.id
        if(safeboxId.isNotBlank())
        tv_safe_box_id.text = "SafeBox ID : ${safeboxId.split("-").first()}"
    }

    override fun onBackPressed() {
        openActivity(HomeActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
