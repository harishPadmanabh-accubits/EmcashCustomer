package com.emcash.customerapp.ui.rewards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emcash.customerapp.R
import kotlinx.android.synthetic.main.activity_reward_details.*

class RewardDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward_details)
        iv_back.setOnClickListener {
            onBackPressed()
        }

    }

}