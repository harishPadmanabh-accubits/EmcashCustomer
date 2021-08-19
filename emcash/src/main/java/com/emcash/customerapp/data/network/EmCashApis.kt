package com.emcash.customerapp.data.network

import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountRequest
import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountResponse
import com.emcash.customerapp.model.auth.userExists.UserExistCheckRequest
import com.emcash.customerapp.model.auth.userExists.UserExistCheckResponse
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.model.tnc.TncResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EmCashApis {

    @POST("v1/customers/auth/usercheck")
    fun checkIfUserExist(@Body checkRequest: UserExistCheckRequest):Call<UserExistCheckResponse>

    @POST("v1/customers/auth/switch")
    fun switchAccount(@Body switchAccountRequest: SwitchAccountRequest): Call<SwitchAccountResponse>

    @GET("v1/customers/profile")
    fun getProfileDetails():Call<ProfileDetailsResponse>

    @GET("v1/customers/termsandconditions")
    fun getTnc():Call<TncResponse>

}