package com.emcash.customerapp.firebaseHelper

class EmCashPushHelper {
    public companion object {

        private var instance: EmCashPushHelper? = null

        @JvmStatic
        public fun getInstance(): EmCashPushHelper {
            return instance ?: synchronized(EmCashPushHelper::class.java) {
                val inst = instance ?: EmCashPushHelper()
                instance = inst
                inst
            }
        }
    }
}