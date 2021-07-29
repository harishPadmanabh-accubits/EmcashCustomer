package com.emcash.customerapp.ui.rewards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_my_rewards.*

class MyRewardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rewards)
        setupRewardsList()
        iv_back.setOnClickListener {
           openActivity(HomeActivity::class.java)
        }
        cv_more.setOnClickListener {
            openActivity(EmscoreActivity::class.java)
        }
    }

    private fun setupRewardsList() {
        rv_rewards.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        rv_rewards.adapter = RewardsCardAdapter()
    }

}