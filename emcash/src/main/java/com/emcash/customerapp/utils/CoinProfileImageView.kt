package com.emcash.customerapp.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.loadImageWithResId
import com.emcash.customerapp.extensions.loadImageWithUrl
import kotlinx.android.synthetic.main.custom_view_coin_profile_image.view.*

class CoinProfileImageView(context: Context, attrs: AttributeSet) :
    RelativeLayout(context, attrs) {

    enum class CoinSize {
        DP_112,  //semi_large
        DP_157,  //large
        DP_52,   //small
        DP_15,   //tiny
        DP_57,   //normal
        DP_64    //medium
    }

    var coinSize: CoinSize

    init {
        val coinSizeArray = context.obtainStyledAttributes(attrs, R.styleable.CoinProfileImageView)
        coinSize =
            CoinSize.values()[coinSizeArray.getInt(R.styleable.CoinProfileImageView_coin_size, 0)]
        when (coinSize) {
            CoinSize.DP_112 -> inflate(context, R.layout.custom_view_coin_image_112, this)
            CoinSize.DP_157 -> inflate(context, R.layout.custom_view_coiin_profile_dp_157, this)
            CoinSize.DP_52 -> inflate(context, R.layout.custom_view_coin_profile_image, this)
            CoinSize.DP_57 -> inflate(context, R.layout.custom_view_coin_profile_57, this)
            CoinSize.DP_15 -> inflate(context, R.layout.custom_view_coin_profile_15, this)
            CoinSize.DP_64 -> inflate(context, R.layout.custom_view_coin_profile_64, this)

        }
    }

    fun setImage(url: String?) {
        val dpView = findViewById<ImageView>(R.id.iv_profile_image)
        Glide.with(dpView.context).load(IMAGE_BASE_URL.plus(url)).placeholder(R.drawable.ic_profile_placeholder).into(dpView)
    }


}