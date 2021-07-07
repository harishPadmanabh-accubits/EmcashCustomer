package com.emcash.customerapp.ui.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.model.dummyActivityData
import kotlinx.android.synthetic.main.wallet_screen_v2.*

class WalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_screen_v2)


        rv_activities.apply {
            layoutManager = LinearLayoutManager(this@WalletActivity,RecyclerView.VERTICAL,false)
            adapter = WalletActivityAdapter(dummyActivityData)

        }
    }
}