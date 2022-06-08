package com.emcash.customerapp.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.emcash.customerapp.R
import java.lang.Exception

class LoaderDialog(val ctx: Context):Dialog(ctx) {

init {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(R.layout.progress_dialog)
    setCancelable(false)
    setCanceledOnTouchOutside(false)
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}

    fun hideLoader(){
        if(isShowing)
            dismiss()
    }

    fun showLoader(){
        show()
    }

}