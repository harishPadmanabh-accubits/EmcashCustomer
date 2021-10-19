package com.emcash.customerapp

object EmCashCommunicationHelper {
    lateinit var listener:EmCashListener
    lateinit var fallbackScreen:String

    fun setParentListener(listener: EmCashListener){
      this.listener = listener
    }

    fun getParentListener() = listener

    fun setFallbackTo(launchClassName:String){
        this.fallbackScreen = launchClassName
    }

    fun getFallBack() = fallbackScreen


}