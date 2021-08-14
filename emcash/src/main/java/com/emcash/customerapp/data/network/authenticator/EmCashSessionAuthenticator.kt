package com.emcash.customerapp.data.network.authenticator
//
//import android.content.Context
//import com.emcash.customerapp.data.network.EmCashApis
//import com.emcash.customerapp.data.network.Resource
//import okhttp3.Authenticator
//import okhttp3.Request
//import okhttp3.Response
//import okhttp3.Route
//
//class EmCashSessionAuthenticator(
//    context: Context,
//    private val tokenApi: EmCashApis
//) : Authenticator {
//    private val appContext = context.applicationContext
//
//    override fun authenticate(route: Route?, response: Response): Request? {
//        when (val tokenResponse = getUpdatedToken()) {
//            is Resource.Success -> {
//                userPreferences.saveAccessTokens(
//                    tokenResponse.value.access_token!!,
//                    tokenResponse.value.refresh_token!!
//                )
//                response.request.newBuilder()
//                    .header("Authorization", "Bearer ${tokenResponse.value.access_token}")
//                    .build()
//            }
//            else -> null
//        }
//    }
//    private  fun getUpdatedToken(): Resource<TokenResponse> {
//        val refreshToken = userPreferences.refreshToken.first()
//        return  { tokenApi.refreshAccessToken(refreshToken) }
//    }
//
//
//}
//
//
