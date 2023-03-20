package com.tft.selfbest.network

import com.tft.selfbest.models.RefreshTokenResponse
import retrofit2.http.*

interface TokenRefreshApi : SelfBestApi {

    @GET("/re-auth")
    suspend fun refreshAccessToken(@Header("RefreshToken") RefreshToken: String)
    : RefreshTokenResponse
}