package com.emcash.customerapp.ui.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.convert_emcash.ConvertEmcashActivity
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.loademcash.LoadEmcashActivity
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.LoaderDialog
import com.emcash.customerapp.utils.SCREEN_WALLET
import kotlinx.android.synthetic.main.wallet_screen_v2.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        renderWalletDetails()

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

        pagedAdapter.addLoadStateListener {loadState ->
            if (loadState.refresh is LoadState.Loading){
               loader.show()
            }
            else{
                loader.hide()

                // getting the error
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }



        lifecycleScope.launch {
            viewModel.walletActivities.collect {
                pagedAdapter.submitData(it)
            }
        }
    }

    private fun renderWalletDetails() {
        viewModel.apply {
            if(profile!=null){
                appCompatImageView.setImage(profile.profileImage)
                tv_balance.text = profile.wallet.amount.toString()
                tv_safe_box_id.text = profile.wallet.walletAddress
            }
        }



    }

    override fun onBackPressed() {
        openActivity(HomeActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
