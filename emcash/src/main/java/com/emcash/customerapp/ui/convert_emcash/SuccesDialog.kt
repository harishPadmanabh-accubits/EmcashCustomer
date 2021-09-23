package com.emcash.customerapp.ui.convert_emcash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import kotlinx.android.synthetic.main.layout_dialog_emcash_successful.*

class SuccesDialog(
    val activity: AppCompatActivity,
    val listener: SuccessDialogListener
) : DialogFragment() {
    var amount =""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_success, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.6).toInt()
        dialog!!.window?.setLayout(width, height)
        dialog!!.setCancelable(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_dialog_message.text = amount
        btn_okay.setOnClickListener {
           // closeAndNavigate()
           listener.onNavigate() //from listenr

        }

    }

    fun closeAndNavigate() {
        activity.openActivity(HomeActivity::class.java)
        activity.finish()
    }



}

interface SuccessDialogListener {
    fun onNavigate()
}