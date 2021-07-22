package com.emcash.customerapp.ui.home

import android.os.Bundle
import android.transition.Transition
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.ChangeBounds
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.model.users
import com.emcash.customerapp.ui.history.TransactionHistory
import com.emcash.customerapp.ui.home.adapter.RecentTransactionsAdapter
import com.emcash.customerapp.ui.loademcash.LoadEmcashActivity
import com.emcash.customerapp.ui.rewards.MyRewardsActivity
import com.emcash.customerapp.ui.settings.SettingsActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.LevelProfileImageView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var shouldAnimateCoin = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.sharedElementEnterTransition.duration = 500

        setupViews()


    }

    private fun setupViews() {
        cv_balance.setOnClickListener {
            openActivity(WalletActivity::class.java)
        }

        rv_recent_transactions.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 5)
            adapter = RecentTransactionsAdapter(users)
        }

        tv_load_emcash.setOnClickListener {
            openLoadEmcash()
        }

        iv_user_image.setOnClickListener {
            openSettings()
        }

        tv_info_history.setOnClickListener {
            openHistory()
        }

        tv_info_rewards.setOnClickListener {
            openRewards()
        }

    }

    private fun openRewards() {
        openActivity(MyRewardsActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openHistory() {
        openActivity(TransactionHistory::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openSettings() {
        openActivity(SettingsActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openLoadEmcash() {
        openActivity(LoadEmcashActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}