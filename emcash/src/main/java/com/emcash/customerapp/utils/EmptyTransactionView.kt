package com.emcash.customerapp.utils

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.emcash.customerapp.R
import kotlinx.android.synthetic.main.custom_view_empty_transaction_layout.view.*

class EmptyTransactionView(context: Context, attrs: AttributeSet) : ConstraintLayout(context,attrs)  {
    var desc = ""
    init {
        inflate(context, R.layout.custom_view_empty_transaction_layout, this)
    }

    fun setDescription(desc:String){
        tv_empty_desc.text = desc
    }
}