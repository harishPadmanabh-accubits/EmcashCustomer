package com.emcash.customerapp.ui.rewards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emcash.customerapp.R
import kotlinx.android.synthetic.main.activity_my_rewards.*

class MyRewardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rewards)
        rv_rewards.adapter = RewardsCardAdapter()
        iv_back.setOnClickListener {
            onBackPressed()
        }
    }

}