package com.example.chat_feature.view_models

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_feature.data.SimplifyHistoryMessage
import com.example.chat_feature.data.SimplifyPathMessage
import com.example.chat_feature.data.SuggestionResponse
import com.example.chat_feature.data.response.MessageResponse
import com.example.chat_feature.data.socketIoRequest
import com.example.chat_feature.network.Api
import com.example.chat_feature.network.web_socket.EasySocketIo
import com.example.chat_feature.network.web_socket.SocketHandler
import com.example.chat_feature.utils.Resource
import com.example.chat_feature.utils.SafeApiCall
import com.example.chat_feature.utils.getAccessToken
import com.example.chat_feature.utils.getCurrentTime
import com.example.chat_feature.utils.getCurrentTimeSimplify
import com.example.chat_feature.utils.getUserId
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SimplifyPathViewModel @Inject constructor(
    private val api: Api,
    private val mapplication: Application
) : AndroidViewModel(mapplication), SafeApiCall {

    companion object {
        private val TAG = "SimplifyPathViewModel"
    }

    val userId: String
    val token: String
    var userIdInt = 0

    init {
        userId = mapplication.applicationContext.getUserId(mapplication).toString()
        token = mapplication.applicationContext.getAccessToken(mapplication)!!
        Log.d("checkUserID", userId.toString())
        Log.d("checkAccessToken", token.toString())
    }

    init {
        userIdInt = mapplication.applicationContext.getUserId(mapplication)
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("userIDD", "$userId")
            loadChatHistory(userIdInt, token)
        }
    }

    var message by mutableStateOf("")
        private set

    fun updateMessage(newValue: String) {
        message = newValue
    }

    var flag by mutableStateOf(false)

    private val gson by lazy { Gson() }
    private var easySocketIO: EasySocketIo? = null
    val chatMessages = mutableStateListOf<SimplifyHistoryMessage>()
    val suggestions = mutableStateListOf<SuggestionResponse>()

    private var searchJob: Job?= null

    private fun loadChatHistory(userId: Int, token: String) =
        viewModelScope.launch {
            val response = safeApiCall {
                api.loadSimplifyHistory(token, userId)
            }

            when (response) {
                Resource.Loading -> Unit
                is Resource.Failure -> {
                    chatMessages.add(SimplifyHistoryMessage("Something went wrong", "user", getCurrentTime()))
                }

                is Resource.Success -> {
                    val msgList = response.value

                    msgList.forEach {
//                        val msg = SimplifyPathMessage(message = it.message)
                        chatMessages.add(it)
                    }
                }
            }
        }

    fun connetSocketIO(socketUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (easySocketIO == null)
                    easySocketIO = EasySocketIo()
                easySocketIO?.connect(socketUrl)
                Log.e("Socket IO", easySocketIO?.isConnected().toString())
                Log.e("Socket IO", "Connected")
                listenUpdate()
                // isSocketConnected()
            } catch (e: java.lang.Exception) {
                Log.d(SimplifyPathViewModel.TAG, "connectSocketIO: ${e.message}")
            }

        }
    }

    fun sendMessage(data: socketIoRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val msg = gson.toJson(data)
            Log.d(SimplifyPathViewModel.TAG, msg.toString())
            easySocketIO?.sendEvent("user_reply", msg)
            // Call the API to send the message to the server for history
            try {
                val response = sendToServerAPI(
                    SimplifyHistoryMessage(
                        data.message,
                        "user",
                        getCurrentTimeSimplify()
                    )
                )
                when (response) {
                    Resource.Loading -> Unit
                    is Resource.Success -> {
                        Log.e("Simplify msg", "Saved successfully")
                        val sentMessage = SimplifyHistoryMessage(
                            message = data.message,
                            sentby = "user",
                            timestamp = getCurrentTime()
                        )
                        chatMessages.add(sentMessage)
                    }
                    is Resource.Failure -> {
                        chatMessages.add(SimplifyHistoryMessage("Something went wrong", "user", getCurrentTime()))
                        Log.e("Simplify msg", "Not Saved successfully")
                    }
                }
            } catch (e: Exception) {
                // Handle API call failure, if needed
            }
//            val sentMessage = SimplifyPathMessage(message = data.message)
        }
    }

    private suspend fun sendToServerAPI(message: SimplifyHistoryMessage): Resource<Unit> {
        return safeApiCall {
            api.sendMessageToServer(token, userIdInt, message)
        }
    }

    private suspend fun listenUpdate() {
//        easySocketIO?.sendEvent("user_reply", "")
        easySocketIO?.onEvent("bot_reply") { data ->
            Log.e("Simplify socket event", "Bot rply")
            val message = data.optString("message")
            //            val newMessage = SimplifyPathMessage(message = message)
            val newMessage = SimplifyHistoryMessage(
                message = message,
                sentby = "bot",
                timestamp = getCurrentTimeSimplify()
            )
            try {
                viewModelScope.launch(Dispatchers.IO) {
                    val response = sendToServerAPI(
                        SimplifyHistoryMessage(
                            message,
                            "bot",
                            getCurrentTimeSimplify()
                        )
                    )
                    when (response) {
                        Resource.Loading -> Unit
                        is Resource.Success -> {
                            Log.e("Simplify msg", "Saved successfully")
                            chatMessages.add(newMessage)
                        }

                        is Resource.Failure -> {
                            chatMessages.add(SimplifyHistoryMessage("Something went wrong", "user", getCurrentTime()))
                            Log.e("Simplify msg", "Not Saved successfully")
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle API call failure, if needed
            }
            Log.d("newMessage", newMessage.message)
        }

        // Listen for 'disconnect' event from the server and update chatMessages
        easySocketIO?.onEvent(Socket.EVENT_DISCONNECT) {
            Log.d(SimplifyPathViewModel.TAG, "disconnectSocketIO")
        }
    }

    fun isSocketConnected(): Boolean {
        if (easySocketIO?.isConnected() == true) {
            Log.d("connected", "socket is connect")
            flag = true
            return true
        } else return false
    }

    fun disconnectSocket(){
        if(easySocketIO != null && easySocketIO?.isConnected() == true)
            easySocketIO?.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        easySocketIO?.disconnect()
    }

    fun getQuerySuggestions(query: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)

            val response = safeApiCall {
                api.getSuggestions(
                    query
                )
            }
            when (response) {
                Resource.Loading -> Unit
                is Resource.Success -> {
                    suggestions.clear()
                    response.value.forEach {
                        suggestions.add(it)
                    }
                }

                is Resource.Failure -> {
                    Log.e("Simplify msg", "Not Saved successfully")
                }
            }
        }
    }

}