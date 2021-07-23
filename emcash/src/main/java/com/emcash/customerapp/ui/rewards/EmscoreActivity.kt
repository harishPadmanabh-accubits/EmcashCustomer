package com.emcash.customerapp.ui.rewards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emcash.customerapp.R
import kotlinx.android.synthetic.main.activity_emscore.*

class EmscoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emscore)
        pb_emscore.setPercentWithAnimation(50)
    }
}