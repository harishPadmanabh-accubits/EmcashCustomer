package com.emcash.customerapp.model

import android.os.Bundle
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens

data class ScreenConfig(
    val screen: NewPaymentScreens,
    val bundle: Bundle? = null
)