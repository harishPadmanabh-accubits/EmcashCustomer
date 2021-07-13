package com.emcash.customerapp.ui.loademcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emcash.customerapp.R
import com.emcash.customerapp.model.DummyCardResponse
import kotlinx.android.synthetic.main.activity_transaction.*

class TransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        val dummyAccounts = ArrayList<DummyCardResponse>()

        dummyAccounts.add(DummyCardResponse(1,"Empay - Prepaid- XXXXX 145", "AED 234.20", true))
        dummyAccounts.add(DummyCardResponse(2,"Empay - Credit- XXXXX 607", "AED 234.20", false))

        rv_accounts.apply {
            adapter = AccountsAdapter(dummyAccounts)
        }
    }
}