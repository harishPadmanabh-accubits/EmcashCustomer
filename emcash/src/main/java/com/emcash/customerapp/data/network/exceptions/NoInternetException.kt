package com.emcash.customerapp.data.network.exceptions

import java.io.IOException

class NoInternetException : IOException() {
    override val message: String
        get() = "You are offline.Please check your internet connection"
}