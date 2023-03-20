package com.tft.selfbest.network

import com.tft.selfbest.BuildConfig
import com.tft.selfbest.data.SelfBestPreference
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelfBestApiClient @Inject constructor(
    val pref: SelfBestPreference,
) {

    var apis: SelfBestApi

    init {
        apis = defaultRetrofit().create(SelfBestApi::class.java)
    }

    private fun buildTokenApi(): TokenRefreshApi {
        return Retrofit.Builder()
            .baseUrl("https://backend-staging.self.best/")
            .client(getRetrofitClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenRefreshApi::class.java)
    }

    private fun getRetrofitClient(authenticator: Authenticator? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Accept", "application/json")
                }.build())
            }.also { client ->
                authenticator?.let { client.authenticator(it) }
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    client.addInterceptor(logging)
                }
            }.build()
    }

    private fun defaultRetrofit(): Retrofit {
        val authenticator = TokenAuthenticator(pref, buildTokenApi())

//        val logging = Interceptor { chain ->
//            val newRequest: Request = chain.request().newBuilder()
//                .addHeader("AuthToken", "${pref.getLoginData?.accessToken}")
//                .build()
//            chain.proceed(newRequest)
//        }
//            HttpLoggingInterceptor()
//        if (BuildConfig.DEBUG) {
//            logging.level = HttpLoggingInterceptor.Level.BODY
//        } else {
//            logging.level = HttpLoggingInterceptor.Level.NONE
//        }


        val httpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(AuthInterceptor(pref))
            .also { client ->
                authenticator.let { client.authenticator(it) }
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    client.addInterceptor(logging)
                }
            }.build()

        val PROD_URL = "https://backend.self.best/"
        val STAG_URL = "https://backend-staging.self.best/"
        return Retrofit.Builder()
            .baseUrl(PROD_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }
}