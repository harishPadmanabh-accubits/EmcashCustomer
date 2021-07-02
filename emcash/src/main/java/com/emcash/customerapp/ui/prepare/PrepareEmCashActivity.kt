package com.emcash.customerapp.ui.prepare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_prepare_em_cash.*

class PrepareEmCashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare_em_cash)

        tv_lets_start.setOnClickListener {
            openActivity(HomeActivity::class.java)
        }
    }
}