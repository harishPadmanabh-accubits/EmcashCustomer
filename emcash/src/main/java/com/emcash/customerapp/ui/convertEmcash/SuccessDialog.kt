package com.emcash.customerapp.ui.convertEmcash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.card_convert_emcash_success.*

class SuccessDialog(
    val activity: AppCompatActivity,
    val listener: SuccessDialogListener
) : DialogFragment() {
    var amount =""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_convert_emcash_success, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.4).toInt()
        dialog!!.window?.setLayout(width, height)
        dialog!!.setCancelable(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_dialog_message.text = amount
        btn_okay.setOnClickListener {
           listener.onNavigate()
        }

    }

}

interface SuccessDialogListener {
    fun onNavigate()
}