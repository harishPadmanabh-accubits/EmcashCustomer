package com.emcash.customerapp.data.network.interceptors

import android.content.Context
import com.emcash.customerapp.data.SyncManager
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class EmCashSessionAuthInterceptor(val context: Context): Interceptor {
    private val appContext: Context = context.applicationContext
    private val sessionId =  SyncManager(appContext).sessionId
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
        if(!sessionId.isNullOrEmpty()){
            builder.header("sessionId",sessionId)
            Timber.e("Session id added to Interceptor $sessionId")
        }else{
            Timber.e("Session id null from interceptor")
        }
        val request = builder.build()
        return chain.proceed(request)
    }
}