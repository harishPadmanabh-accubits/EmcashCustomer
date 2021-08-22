package com.emcash.customerapp

object CommunicationHelper {
    lateinit var listener:EmCashListener
    fun setParentInstance(listener: EmCashListener){
      this.listener = listener
    }

    fun getParentInstance() = listener
}