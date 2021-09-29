package com.emcash.customerapp.utils

class TransactionUtils {
    companion object {
        const val PAYMENT_TYPE_TRANSFER = 1
        const val PAYMENT_TYPE_REQUEST = 2
        const val PAYMENT_TYPE_QR = 3

        const val TRANSACTION_TYPE_TRANSFER = 1
        const val TRANSACTION_TYPE_TOPUP = 2
        const val TRANSACTION_TYPE_WITHDRAW = 3
        const val TRANSACTION_TYPE_REQUEST = 4

        const val TRANSACTION_STATUS_SUCCESS = 1
        const val TRANSACTION_STATUS_PENDING = 2
        const val TRANSACTION_STATUS_FAILED = 3
        const val TRANSACTION_STATUS_REJECTED = 4

        const val TRANSACTION_METHOD_EMPAY = 1
        const val TRANSACTION_METHOD_GATEWAY = 2

        const val TRANSACTION_MODE_CREDIT = 1
        const val TRANSACTION_MODE_DEBIT = 2
    }
}