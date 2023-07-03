package com.example.chat_feature.view_models

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_feature.data.*
import com.example.chat_feature.data.experts.BotSeenRequest
import com.example.chat_feature.data.response.UploadPhotoResponse
import com.example.chat_feature.data.response.expert.SocketResponseByBot
import com.example.chat_feature.network.Api
import com.example.chat_feature.network.web_socket.EasyWS
import com.example.chat_feature.network.web_socket.WebSocketInterface
import com.example.chat_feature.network.web_socket.easyWebSocket
import com.example.chat_feature.utils.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val api: Api,
    private val application: Application

) : AndroidViewModel(application), SafeApiCall, WebSocketInterface {

    companion object {
        private const val TAG = "ChatViewModel"
    }


    val userId: String

    init {
        userId = application.applicationContext.getUserId(application).toString()
        Log.d("checkUserId", application.applicationContext.getUserId(application).toString())
    }


    var message by mutableStateOf("")
        private set


    private val gson by lazy { Gson() }
    override var easyWs: EasyWS? = null


    val messageList = mutableStateListOf<Resource<Message>>()

    private val _secondLastMsgText = MutableStateFlow("")
    val secondLastMsgText: StateFlow<String>
        get() = _secondLastMsgText


    fun updateMessage(newValue: String) {
        message = newValue
    }

    var isImageBoxEnabled by mutableStateOf(false)

    var imageUploadResponse = mutableStateOf<Resource<UploadPhotoResponse>>(Resource.Loading)
        private set
    var imageUri by mutableStateOf<String?>(null)
        private set
    val imageProgressState = mutableStateOf(0)
    fun updateUri(newValue: String?) {
        imageUri = newValue
    }


    fun <T> sendMessageToServer(data: T) = viewModelScope.launch {

        val response: Resource<Message> = when (data) {
            is PlainMessageRequest -> {

                messageList.add(Resource.Success(data.convertToMessage().copy(senderId = userId)))
                safeApiCall {
                    api.sendPlainMessage(data).convertToMessage().copy(receiverId = userId).also {
                        isImageBoxEnabled = it.message.contains("exact query")
                    }
                }
            }

            is InteractiveMessageRequest -> {
                messageList.add(Resource.Success(data.convertToMessage().copy(senderId = userId)))
                safeApiCall {
                    api.sendInteractiveMessage(data).convertToMessage().copy(receiverId = userId)
                        .also {
                            isImageBoxEnabled = it.message.contains("exact query")
                        }
                }
            }

            is ImgShareOnBotRequest -> {
                messageList.add(Resource.Success(data.convertToMessage().copy(senderId = userId)))
                val multipartBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("event_message", data.event_message)
                    .addFormDataPart("sender_id", data.sender_id)
                    .addFormDataPart("event_type", data.event_type)
                    .addFormDataPart("file", data.file.name, data.file.toRequestBody { })
                    .build()

                safeApiCall {
                    api.imgShareOnBot(multipartBody).convertToMessage().copy(receiverId = userId)
                }
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
                    Log.d("chatlist", chatList.toString())

                    chatList.forEach {
                        Log.d("chatlist_internal", it.convertToMessage().toString())
                        messageList.add(Resource.Success(it.convertToMessage()))
                    }

                    if (!messageList.isNullOrEmpty()) {
                        val lastmsg = messageList.last() as Resource.Success
                        if (!lastmsg.value.buttons.isNullOrEmpty()) {
                            lastmsg.value.isButtonEnabled = true
                            messageList.removeLast()
                            messageList.add(lastmsg)
                        }
                        if (lastmsg.value.message.contains("Please select a valid skill from this dropdown")) {
                            Log.d("lastMessageSelect", "called")
                            lastmsg.value.isDropDownEnabled = true
                            messageList.removeLast()
                            messageList.add(lastmsg)
                            Log.d("lastMessageSelect", "called +$lastmsg")
                        }
                    }
                }
            }

        }

    fun imgOnBot(data: MultipartBody) {
        viewModelScope.launch {
            safeApiCall {
                api.imgShareOnBot(data)
            }
        }

    }

    fun seenBotMessage() {
        viewModelScope.launch {
            safeApiCall {
                api.botMessageSeenRequest(BotSeenRequest(userId, ""))
            }
        }
    }


    /*----------------------------------- Web Socket --------------------------------*/

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("userIDD", "$userId")
            loadChatBetweenUserAndBot(userId)

        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun connectSocket(socketUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // /chat/676/

            try {
                easyWs = OkHttpClient().easyWebSocket(socketUrl, application.applicationContext)
                Log.d(TAG, "Connection: CONNECTION established!")
                listenUpdates()
            } catch (e: Exception) {
                Log.d(TAG, "connectSocket: ${e.message}")
            }
        }
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
                    val jsonObject = JSONObject(text)
                    //var responseObj = text!!

                    if (jsonObject.has("chat_messages")) return@consumeEach

                    val response = gson.fromJson(text, SocketResponseByBot::class.java)
                    if (response.data.type == "query" || response.data.type == "create_chat_box") {
                        Log.d(TAG, "onMessage: $response")
                        messageList.add(Resource.Success(response.data.convertToMessage()))
                    }


                }
            }
        }


    }


    override fun closeConnection() {
        easyWs?.webSocket?.close(1001, "Closing manually")
        Log.d(TAG, "closeConnection: CONNECTION CLOSED!")
    }

    override fun onCleared() {
        super.onCleared()

        closeConnection()
    }

}



