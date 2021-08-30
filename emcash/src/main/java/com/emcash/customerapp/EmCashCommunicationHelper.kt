package com.emcash.customerapp

object EmCashCommunicationHelper {
    lateinit var listener:EmCashListener

    fun setParentListener(listener: EmCashListener){
      this.listener = listener
    }

    fun getParentListener() = listener
}