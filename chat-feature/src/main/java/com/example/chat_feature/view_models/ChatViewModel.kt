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
import com.example.chat_feature.data.experts.BotSeenRequest
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val api: Api,
    application: Application,
    private val context: Context

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

    private val _secondLastMsgText = MutableStateFlow("")
    val secondLastMsgText: StateFlow<String>
        get() = _secondLastMsgText


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
                    Log.d("chatlist",chatList.toString())

                    chatList.forEach {
                        Log.d("chatlist_internal",it.convertToMessage().toString())
                        messageList.add(Resource.Success(it.convertToMessage()))
                    }
                    val lastmsg=messageList.last() as Resource.Success
                    if(!lastmsg.value.buttons.isNullOrEmpty()){
                        lastmsg.value.isButtonEnabled=true
                        messageList.removeLast()
                        messageList.add(lastmsg)
                    }
                    else if(lastmsg.value.message=="Please select a valid skill from this dropdown"){
                        val secondLastmsg=messageList[messageList.size-2] as? Resource.Success
                        if(secondLastmsg!=null){
                            _secondLastMsgText.value= secondLastmsg.value.eventMessage!!
                            secondLastmsg.value.wrongskill=secondLastmsg.value.eventMessage
                           // Log.d("secondLastmsgvale",_secondLastMsgText.value.toString())
                        }
                        Log.d("secondLastmsg",secondLastmsg.toString())
                        Log.d("secondLastmsg", secondLastmsg?.value?.wrongskill.toString())


                    }

                }
            }

        }

    fun seenBotMessage(){
        viewModelScope.launch {
            safeApiCall {
                api.botMessageSeenRequest(BotSeenRequest(userId))
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
            easyWs = OkHttpClient().easyWebSocket(socketUrl, context)
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
                    val jsonObject = JSONObject(text)
                    //var responseObj = text!!

                    if (jsonObject.has("chat_messages")) return@consumeEach

                    val response = gson.fromJson(text, SocketResponseByBot::class.java)
                    if(response.data.type=="query" || response.data.type=="create_chat_box"){
                        Log.d(TAG, "onMessage: $response")
                        messageList.add(Resource.Success(response.data.convertToMessage()))
                    }



                }
            }
        }


    }


     fun closeConnection() {
        easyWs?.webSocket?.close(1001, "Closing manually")
        Log.d(TAG, "closeConnection: CONNECTION CLOSED!")
    }

    override fun onCleared() {
        super.onCleared()

        closeConnection()
    }

}



