package com.example.chat_feature.network

import com.example.chat_feature.data.InteractiveMessageRequest
import com.example.chat_feature.data.PlainMessageRequest
import com.example.chat_feature.data.bot_history.BotHistoryResponse
import com.example.chat_feature.data.experts.ExpertListDemo
import com.example.chat_feature.data.response.MessageResponse
import com.example.chat_feature.data.response.UploadPhotoResponse
import com.example.chat_feature.data.response.expert_chat.ExpertChatHistory
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface Api {

    @POST("conversation/read/")
    suspend fun sendPlainMessage(
        @Body data: PlainMessageRequest,
    ): MessageResponse


    @POST("conversation/read/")
    suspend fun sendInteractiveMessage(
        @Body data: InteractiveMessageRequest,
    ): MessageResponse


    @GET("conversation/load_experts")
    suspend fun loadExpertList(
//        @Body data: ExpertListRequest,
        @Query("sentBy") senderId: String,
    ): ExpertListDemo


    @GET("conversation/load_chat")
    suspend fun loadChatBetweenUserAndBot(
        @Query("sentBy") senderId: String,
        @Query("queryId") queryId: String = "",
    ): BotHistoryResponse


    @GET("conversation/load_chat")
    suspend fun loadChatBetweenUserAndExpert(
        @Query("sentBy") senderId: String,
        @Query("queryId") queryId: String,
    ): ExpertChatHistory

    @Multipart
    @POST("conversation/upload-image/")
    suspend fun uploadPhoto(
        @Part file: MultipartBody.Part,
    ): UploadPhotoResponse

}