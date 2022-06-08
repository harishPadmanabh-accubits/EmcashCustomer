package com.emcash.customerapp.data.network

import android.content.Context
import com.emcash.customerapp.BuildConfig
import com.emcash.customerapp.data.network.interceptors.EmCashSessionAuthInterceptor
import com.emcash.customerapp.data.network.interceptors.NetworkConnectionInterceptor
import com.emcash.customerapp.utils.BASE_URL
import com.emcash.customerapp.utils.RELEASE_URL
import com.emcash.customerapp.utils.STAGING_URL
import com.emcash.customerapp.utils.TIME_OUT
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class EmCashApiManager(context: Context) {
    val api:EmCashApis
    init {
        val gson = GsonBuilder().serializeNulls().create()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .addInterceptor(EmCashSessionAuthInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .build()
        

        val restAdapter = Retrofit.Builder()
            .baseUrl(RELEASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        api = restAdapter.create(EmCashApis::class.java)
    }

}