package com.emcash.customerapp.data.network.exceptions

import com.emcash.customerapp.utils.ERROR_NO_INTERNET
import java.io.IOException

class NoInternetException : IOException() {
    override val message: String
        get() = ERROR_NO_INTERNET
}