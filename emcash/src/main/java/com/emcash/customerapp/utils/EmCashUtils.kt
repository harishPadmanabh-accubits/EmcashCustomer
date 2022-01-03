package com.emcash.customerapp.utils

object EmCashUtils {
    /**
     * Notification Type Constants
     * @see(NotificationDetailsAdapter.kt)
     */
    const val NOTIFICATION_STATUS_PENDING = 1
    const val NOTIFICATION_STATUS_SUCCESS = 2
    const val NOTIFICATION_STATUS_REJECTED = 3
    const val NOTIFICATION_STATUS_REJECTED_FROM_MERCHANT = 5
    const val NOTIFICATION_STATUS_COMPLETE_REGISTRATION = 6

    /**
     *Transaction Type Constants
     */
    const val PAYMENT_SUCCESS = 1
    const val PAYMENT_IN_PROGRESS = 2
    const val PAYMENT_FAILED = 3
    const val PAYMENT_REJECTED = 4

    const val PAYMENT_MODE_SENT = 2

    const val PAYMENT_TYPE_TRANSFER = 1
    const val PAYMENT_TYPE_REQUEST = 4
    const val PAYMENT_TYPE_TOPUP=2
    const val PAYMENT_TYPE_WITHDRAW = 3

    const val PAYMENT_MODE_DEBIT=2
    const val PAYMENT_MODE_CREDIT=1

}