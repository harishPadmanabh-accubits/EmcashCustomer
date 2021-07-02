package com.emcash.customerapp.utils

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.emcash.customerapp.R
import kotlinx.android.synthetic.main.custom_view_onboarding_next_btn.view.*
import timber.log.Timber

class OnboardingNextButton(context: Context, attrs: AttributeSet) : ConstraintLayout(context,attrs) {

    init {
        inflate(context, R.layout.custom_view_onboarding_next_btn, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.OnboardingNextButton)
        val text = attributes.getString(R.styleable.OnboardingNextButton_buttonText)
        Timber.e("Button text $text")
        tv_next.text = text

    }

}