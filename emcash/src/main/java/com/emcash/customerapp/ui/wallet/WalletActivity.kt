package com.emcash.customerapp.ui.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.model.dummyActivityData
import com.emcash.customerapp.ui.loademcash.LoadEmcashActivity
import kotlinx.android.synthetic.main.wallet_screen_v2.*

class WalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_screen_v2)

        iv_back.setOnClickListener {
            onBackPressed()
        }

        btn_load_emcash.setOnClickListener {
            openActivity(LoadEmcashActivity::class.java)
        }


        rv_activities.apply {
            layoutManager = LinearLayoutManager(this@WalletActivity,RecyclerView.VERTICAL,false)
            adapter = WalletActivityAdapter(dummyActivityData)

        }
    }
}