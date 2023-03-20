package com.example.chat_feature.view_models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_feature.data.InteractiveMessageRequest
import com.example.chat_feature.data.Message
import com.example.chat_feature.data.PlainMessageRequest
import com.example.chat_feature.data.SocketUpdate
import com.example.chat_feature.data.response.expert.SocketResponseByBot
import com.example.chat_feature.network.Api
import com.example.chat_feature.network.web_socket.EasyWS
import com.example.chat_feature.network.web_socket.easyWebSocket
import com.example.chat_feature.utils.Constants
import com.example.chat_feature.utils.Resource
import com.example.chat_feature.utils.SafeApiCall
import com.example.chat_feature.utils.getUserId
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val api: Api,
    application: Application

) : AndroidViewModel(application), SafeApiCall {

    companion object {
        private const val TAG = "ChatViewModel"
    }


    val userId:String
    init {
        userId=application.applicationContext.getUserId().toString()
        Log.d("checkUserId",application.applicationContext.getUserId().toString())
    }


    var message by mutableStateOf("")
        private set


    private val gson by lazy { Gson() }
    private var easyWs: EasyWS? = null


    val messageList = mutableStateListOf<Resource<Message>>()


    fun updateMessage(newValue: String) {
        message = newValue
    }


    fun <T> sendMessageToServer(data: T) = viewModelScope.launch {

        val response: Resource<Message> = when (data) {
            is PlainMessageRequest -> {

                messageList.add(Resource.Success(data.convertToMessage().copy(senderId =userId )))
                safeApiCall { api.sendPlainMessage(data).convertToMessage().copy(receiverId =userId ) }
            }

            is InteractiveMessageRequest -> {
                messageList.add(Resource.Success(data.convertToMessage().copy(senderId =userId )))
                safeApiCall { api.sendInteractiveMessage(data).convertToMessage().copy(receiverId =userId ) }
            }

            else -> safeApiCall {
                api.sendPlainMessage(data as PlainMessageRequest).convertToMessage()
            }   // Will never called
        }

        messageList.add(response)
    }


    /*----------------------------------- Load Chats Between Bot and User --------------------------------*/

    private fun loadChatBetweenUserAndBot(senderId: String) =
        viewModelScope.launch {
            val response = safeApiCall {
                api.loadChatBetweenUserAndBot(
                   // senderId = senderId,
                    senderId = userId,
                )
            }


            when (response) {
                Resource.Loading -> Unit
                is Resource.Failure -> {
                    messageList.add(Resource.Failure(response.errorCode))
                }

                is Resource.Success -> {
                    val chatList = response.value.chatMessages
                    chatList.forEach {
                        messageList.add(Resource.Success(it.convertToMessage()))
                    }
                }

            }
        }


    /*----------------------------------- Web Socket --------------------------------*/

    init {
        viewModelScope.launch(Dispatchers.IO) {
//            listenUpdates()
            Log.d("userIDD","$userId")
            // todo change the ID from static to dynamic (using shared pref)
            loadChatBetweenUserAndBot(userId)

        }

    }


    fun connectSocket(socketUrl: String = Constants.SELF_BEST_SOCKET_URL) =
        viewModelScope.launch(Dispatchers.IO) {
            // /chat/676/
            easyWs = OkHttpClient().easyWebSocket(socketUrl)
            listenUpdates()
        }


    private suspend fun listenUpdates() {

        easyWs?.textChannel?.consumeEach {
            when (it) {
                is SocketUpdate.Failure -> {
                    messageList.add(Resource.Failure())
                }

                is SocketUpdate.Success -> {

                    val text = it.text
                    Log.d(TAG, "onMessage: $text")

                    val response = gson.fromJson(text, SocketResponseByBot::class.java)

                    Log.d(TAG, "onMessage: $response")

                    messageList.add(Resource.Success(response.data.convertToMessage()))

                }
            }
        }


    }


    private fun closeConnection() {
        easyWs?.webSocket?.close(1001, "Closing manually")
        Log.d(TAG, "closeConnection: CONNECTION CLOSED!")
    }

    override fun onCleared() {
        super.onCleared()

        closeConnection()
    }

}



