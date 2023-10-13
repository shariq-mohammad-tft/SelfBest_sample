package com.tft.selfbest.network

import android.util.Log
import com.tft.selfbest.data.SelfBestPreference
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    val pref: SelfBestPreference,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authenticationRequest = pref.getLoginData?.accessToken.let {
            Log.e("Token2 ", "$it")
            originalRequest.newBuilder().addHeader("AuthToken", "$it")
                .build()
        }

        return chain.proceed(authenticationRequest)
    }
}