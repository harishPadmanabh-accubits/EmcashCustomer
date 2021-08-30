package com.emcash.customerapp.utils

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.*
import kotlinx.android.synthetic.main.layout_item_recent_payment.view.*
import java.util.*

class LevelProfileImageView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.layout_item_recent_payment, this)
    }

    enum class UserProfileLevel {
        RED, GREEN, YELLOW, NONE
    }

    fun setLevel(level: UserProfileLevel) {
        when (level) {
            UserProfileLevel.RED -> {
                fl_user_level.setBackgroundResource(R.drawable.red_round)
            }
            UserProfileLevel.GREEN -> {
                fl_user_level.setBackgroundResource(R.drawable.green_round)
            }
            UserProfileLevel.YELLOW -> {
                fl_user_level.setBackgroundResource((R.drawable.yellow_round))
            }
            UserProfileLevel.NONE -> {
                fl_user_level.makeInvisible()
            }
        }
    }

    fun setLevel(level: Int) {
        when (level) {
            1 -> fl_user_level.setBackgroundResource(R.drawable.green_round)
            2 -> fl_user_level.setBackgroundResource((R.drawable.yellow_round))
            3 -> fl_user_level.setBackgroundResource(R.drawable.red_round)
            else -> fl_user_level.setBackgroundResource(0)
        }
    }

    fun setProfileImage(imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            iv_user_image.loadImageWithPlaceHolder(imageUrl,R.drawable.ic_profile_placeholder)
        } else {
            iv_user_image.setBackgroundColor(ContextCompat.getColor(context, R.color.ash))
        }
    }

    fun setProfileImage(resId: Int?) {
        if (!resId.isNull()) {
            iv_user_image.loadImageWithResId(resId)
            tv_user_name_letter.hide()
        } else {
            iv_user_image.setBackgroundColor(ContextCompat.getColor(context, R.color.ash))
        }
    }

    fun setProfileName(name: String) {
        tv_user_name_.text = name
    }

    fun setFirstLetter(name: String) {
        tv_user_name_letter.text = name.first().toString().toUpperCase(Locale.getDefault())
        tv_user_name_letter.show()
        Glide.with(context).load(R.drawable.round_ash).into(iv_user_image)
    }
}