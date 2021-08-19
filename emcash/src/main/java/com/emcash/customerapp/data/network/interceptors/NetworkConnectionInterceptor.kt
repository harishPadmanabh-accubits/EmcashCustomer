package com.emcash.customerapp.data.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.emcash.customerapp.data.network.exceptions.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class NetworkConnectionInterceptor(context: Context) : Interceptor{
    val appContext = context.applicationContext
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!hasInternet(appContext)){
            Timber.e("Throw no internet exception")
            throw NoInternetException()
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

     private fun hasInternet(context: Context):Boolean{
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false

        }
    }


}