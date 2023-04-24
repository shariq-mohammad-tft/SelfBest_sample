package com.tft.selfbest.network

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import com.example.chat_feature.utils.Resource
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.models.LogedInData
import com.tft.selfbest.models.RefreshTokenResponse
import com.tft.selfbest.repository.BaseRepository
import com.tft.selfbest.ui.activites.MainActivity
import com.tft.selfbest.ui.login.LoginActivity
import com.tft.selfbest.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import kotlin.math.log

class TokenAuthenticator @Inject constructor(
    private val pref: SelfBestPreference,
    private val tokenApi: TokenRefreshApi
) : Authenticator, BaseRepository(tokenApi) {
    private val accessToken = pref.getLoginData?.accessToken
    override fun authenticate(route: Route?, response: Response): Request? {
        val mutex = Mutex()
        return runBlocking {
            mutex.withLock {
                Log.e("Expired", "Token")
                val newToken = pref.getLoginData?.accessToken
                if (!(accessToken.equals(newToken)) && newToken != null) {
                    Log.e("Token", "No need to refresh")
                    response.request.newBuilder()
                        .header("AuthToken", newToken)
                        .build()
                } else if (pref.getLoginData?.refreshToken != null) {
                    Log.e("Token", "Need to refresh")
                    when (val tokenResponse = getUpdatedToken()) {
                        is NetworkResponse.Success -> {
                            pref.setLoginData(
                                LogedInData(
                                    tokenResponse.data?.accessToken!!,
                                    pref.getLoginData?.approved,
                                    tokenResponse.data.email,
                                    tokenResponse.data.firstName,
                                    tokenResponse.data.userid,
                                    pref.getLoginData?.lastName!!,
                                    pref.getLoginData?.redirect,
                                    tokenResponse.data.refreshToken,
                                    pref.getLoginData?.roles!!,
                                    pref.getLoginData?.userName,
                                    pref.getLoginData?.isNewUser!!
                                )
                            )
                            response.request.newBuilder()
                                .header("AuthToken", tokenResponse.data.accessToken)
                                .build()
                        }
                        is NetworkResponse.Error -> {
                            //pref.clear()
                            //logout()
                            null
                        }
                        else -> null
                    }
                } else {
                    null
                }
            }
        }
    }

    /*private suspend fun getUpdatedToken(): NetworkResponse<RefreshTokenResponse> {
        val refreshToken = pref.getLoginData?.refreshToken!!
        return safeApiCall { tokenApi.refreshAccessToken(RefreshToken = refreshToken) }
    }*/
    private suspend fun getUpdatedToken() =
        safeApiCall { tokenApi.refreshAccessToken(RefreshToken = pref.getLoginData?.refreshToken!!) }

}