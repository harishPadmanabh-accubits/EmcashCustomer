package com.emcash.customerapp.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.loadImageWithResId
import com.emcash.customerapp.extensions.loadImageWithUrl
import kotlinx.android.synthetic.main.custom_view_coin_profile_image.view.*

class CoinProfileImageView(context: Context, attrs: AttributeSet) :
    RelativeLayout(context, attrs)  {

    init{
        inflate(context, R.layout.custom_view_coin_profile_image, this)
    }

    fun setImage(url:String){
        //iv_dp.loadImageWithUrl(url)
    }

    fun setImage(resID:Int){
        //iv_dp.loadImageWithResId(resID)
    }

}