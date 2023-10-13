package com.example.chat_feature.network

import com.example.chat_feature.data.ImgShareOnBotRequest
import com.example.chat_feature.data.InteractiveMessageRequest
import com.example.chat_feature.data.Message
import com.example.chat_feature.data.PlainMessageRequest
import com.example.chat_feature.data.SimplifyHistoryMessage
import com.example.chat_feature.data.SuggestionResponse
import com.example.chat_feature.data.bot_history.BotHistoryResponse
import com.example.chat_feature.data.experts.BotSeenRequest
import com.example.chat_feature.data.experts.ExpertListDemo
import com.example.chat_feature.data.response.BotUnseenCountResponse
import com.example.chat_feature.data.response.ImageShareResponseOnBotResponse
import com.example.chat_feature.data.response.MessageResponse
import com.example.chat_feature.data.response.UploadPhotoResponse
import com.example.chat_feature.data.response.expert_chat.ExpertChatHistory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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

    @POST("conversation/update-message-status/")
    suspend fun botMessageSeenRequest(
        @Body data:BotSeenRequest
    )

    @GET("conversation/unseen_count")
    suspend fun botUnseenCount(
        @Query("sentBy") senderId: String
    ): BotUnseenCountResponse


    @POST("conversation/read/")
    suspend fun imgShareOnBot(
        @Body data:MultipartBody
    ):MessageResponse

    @GET("https://backend.self.best/user/{user_id}/simplify/chat")
    suspend fun loadSimplifyHistory(
        @Header("AuthToken") authToken: String,
        @Path("user_id") userId: Int
    ): List<SimplifyHistoryMessage>

    @POST("https://backend.self.best/user/{user_id}/simplify/chat")
    suspend fun sendMessageToServer(
        @Header("AuthToken") authToken: String,
        @Path("user_id") userId: Int,
        @Body data:SimplifyHistoryMessage
    )

    @GET("https://simplifypath.self.best/suggestion/{query}")
    suspend fun getSuggestions(
//        @Header("AuthToken") authToken: String,
//        @Path("user_id") userId: Int,
        @Path("query") query: String
    ): List<SuggestionResponse>
}