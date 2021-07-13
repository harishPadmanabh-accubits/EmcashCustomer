package com.emcash.customerapp.ui.home

import android.os.Bundle
import android.transition.Transition
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.ChangeBounds
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.ui.home.adapter.RecentTransactionsAdapter
import com.emcash.customerapp.ui.loademcash.LoadEmcashActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.LevelProfileImageView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var shouldAnimateCoin = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.sharedElementEnterTransition.duration=500

        cv_balance.setOnClickListener {
            openActivity(WalletActivity::class.java)
        }

        val users = listOf<DummyUserData>(
            DummyUserData(
                "Mary ",
                R.drawable.sample_dp,
                LevelProfileImageView.UserProfileLevel.GREEN
            ),
            DummyUserData(
                "Alan",
                R.drawable.sample_img,
                LevelProfileImageView.UserProfileLevel.RED
            ),
            DummyUserData(
                "Milan",
                R.drawable.sample_dp,
                LevelProfileImageView.UserProfileLevel.NONE
            ),
            DummyUserData(
                "Anise",
                R.drawable.sample_img,
                LevelProfileImageView.UserProfileLevel.RED
            ),
            DummyUserData(
                "Fijui",
                R.drawable.sample_dp,
                LevelProfileImageView.UserProfileLevel.NONE
            ),
            DummyUserData(
                "Lewis",
                R.drawable.sample_img,
                LevelProfileImageView.UserProfileLevel.YELLOW
            ),
            DummyUserData(
                "Kamal",
               null,
                LevelProfileImageView.UserProfileLevel.GREEN
            ), DummyUserData(
                "Michel",
                R.drawable.sample_img,
                LevelProfileImageView.UserProfileLevel.YELLOW
            ))

    rv_recent_transactions.apply {
        layoutManager = GridLayoutManager(this@HomeActivity,5)
        adapter = RecentTransactionsAdapter(users)
    }



        tv_load_emcash.setOnClickListener {
            openActivity(LoadEmcashActivity::class.java)
        }


    }

    override fun onResume() {
        super.onResume()
        shouldAnimateCoin = intent.getBooleanExtra("shouldAnimate",false)

    }


}