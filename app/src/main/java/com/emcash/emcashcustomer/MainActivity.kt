package com.emcash.emcashcustomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.emcash.customerapp.ui.convert_emcash.ConvertEmcashActivity
import com.emcash.customerapp.ui.history.TransactionHistory
import com.emcash.customerapp.ui.intro.IntroActivity
import com.emcash.customerapp.ui.loademcash.TransactionActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.ui.prepare.PrepareEmCashActivity
import com.emcash.customerapp.ui.rewards.MyRewardsActivity
import com.emcash.customerapp.ui.rewards.RewardDetails
import com.emcash.customerapp.ui.wallet.WalletActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        val intent=Intent(applicationContext,IntroActivity::class.java)
        startActivity(intent)
    }
}