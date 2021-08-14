package com.emcash.customerapp.data.network

import android.content.Context
import com.emcash.customerapp.BuildConfig
import com.emcash.customerapp.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmCashApiManager(appContext: Context) {
    val api:EmCashApis
    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val okHttpClient = OkHttpClient.Builder()
          //  .addInterceptor(NetworkConnectionInterceptor(appContext))
            .addInterceptor(loggingInterceptor)
            .build()
        val restAdapter = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
          //  .client(okHttpClient)
            .build()
        api = restAdapter.create(EmCashApis::class.java)

    }

}