package com.example.chat_feature.view_models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_feature.data.SocketUpdate
import com.example.chat_feature.data.experts.ChatBetweenUserAndExpertRequest
import com.example.chat_feature.data.response.UploadPhotoResponse
import com.example.chat_feature.data.response.expert_chat.ChatJson
import com.example.chat_feature.data.response.expert_chat.ExpertChatRequest
import com.example.chat_feature.network.Api
import com.example.chat_feature.network.web_socket.EasyWS
import com.example.chat_feature.network.web_socket.easyWebSocket
import com.example.chat_feature.utils.Constants
import com.example.chat_feature.utils.Resource
import com.example.chat_feature.utils.SafeApiCall
import com.example.chat_feature.utils.toRequestBody
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ExpertChatViewModel @Inject constructor(
    private val api: Api,
) : ViewModel(), SafeApiCall {

    companion object {
        private const val TAG = "ExpertChatViewModel"
    }

    var imageUploadResponse = mutableStateOf<Resource<UploadPhotoResponse>>(Resource.Loading)
        private set

//    val response: LiveData<Resource<UploadPhotoResponse>> = _response

    var imageUri by mutableStateOf<String?>(null)
        private set

    val imageProgressState = mutableStateOf(0)

    private val gson by lazy { Gson() }
    private var easyWs: EasyWS? = null

    var message by mutableStateOf("")
        private set


    val messageList = mutableStateListOf<Resource<ChatJson>>()

    fun updateMessage(newValue: String) {
        message = newValue
    }

    fun updateUri(newValue: String?) {
        imageUri = newValue
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // listenUpdates()

            /* messageList.last().let {
                 when(it){
                     is Resource.Failure -> TODO()
                     Resource.Loading -> TODO()
                     is Resource.Success -> {
                         it.value.copy(progress)
                     }
                 }
             }*/

//            (messageList.last() as Resource.Success<ChatJson>).value.copy(_progress = 50)

        }
    }


    /*----------------------------------- Load Chats Between Users --------------------------------*/

    fun loadChatBetweenUserAndExpert(data: ChatBetweenUserAndExpertRequest) =
        viewModelScope.launch {
            val response = safeApiCall {
                api.loadChatBetweenUserAndExpert(
                    senderId = data.senderId,
                    queryId = data.queryId,
                )
            }

            when (response) {
                Resource.Loading -> Unit
                is Resource.Failure -> {
                    messageList.add(Resource.Failure(response.errorCode))
                }

                is Resource.Success -> {
                    val chatList = response.value.chat_messages
                    chatList.forEach {
                        messageList.add(Resource.Success(it.chatJson))
                    }
                }
            }
        }


    /*----------------------------------- Upload Image --------------------------------*/

    fun uploadImage(
        image: File,
        data: ExpertChatRequest,
    ) =
        viewModelScope.launch(Dispatchers.IO) {

            ChatJson(
                sentBy = data.senderId,
                sentTo = data.receiverId,
                queryId = data.queryId,
                message = data.message,
                image = image.absolutePath
            ).also {
                it.isFilePath = true
                messageList.add(Resource.Success(it))
                updateUri(null)
                updateMessage("")
            }

            val body = MultipartBody.Part.createFormData(
                "file",
                image.name,
                image.toRequestBody {
                    Log.d(TAG, "uploadIMAGE: PROGRESS is ---  $it")
                    imageProgressState.value = it
                    (messageList.last() as Resource.Success<ChatJson>).value.progress = it

                }
            )

//            val requestFile = image.asRequestBody("*/*".toMediaTypeOrNull())
//            val body = MultipartBody.Part.createFormData("file", image.name, requestFile)

            val response = safeApiCall { api.uploadPhoto(body) }
            Log.d("fileName", body.toString())
            when (response) {

                is Resource.Success -> {
                    sendMessage(
                        data.copy(
                            imageLink = response.value.imageLink,
                            message = data.message
                        )
                    )

                    messageList.removeLast()


                }

                Resource.Loading -> {
                    imageUploadResponse.value = Resource.Loading
                }
                is Resource.Failure -> {
                    imageUploadResponse.value = response
                }

            }
        }


    private fun buildImageUploadMessage(
        caption: String,
        senderId: String,
        receiverId: String,
        queryId: String,
        imageLink: String?,
    ): ExpertChatRequest {
        return ExpertChatRequest(
            senderId = senderId,
            receiverId = receiverId,
            message = caption,
            queryId = queryId,
            imageLink = imageLink
        )
    }

    /*----------------------------------- Web Socket --------------------------------*/

    fun connectSocket(socketUrl: String = Constants.SELF_BEST_SOCKET_URL) =
        viewModelScope.launch(Dispatchers.IO) {
            // /chat/676/
            easyWs = OkHttpClient().easyWebSocket(socketUrl)
            Log.d(TAG, "connectSocket: Called Listen Channel")
            listenUpdates()
        }

    fun sendMessage(data: ExpertChatRequest) {

        val msg = gson.toJson(data)
        Log.d("sendMsg", msg.toString())
        easyWs?.webSocket?.send(msg)
        messageList.add(Resource.Success(data.convertToChatJson()))
    }

    private suspend fun listenUpdates() {
        easyWs?.webSocket?.send("")
        easyWs?.textChannel?.consumeEach {
            // easyWs?.channel?.consumeEach {
            when (it) {
                is SocketUpdate.Failure -> {
                    messageList.add(Resource.Failure(it.exception?.message!!.toInt()))
                }

                is SocketUpdate.Success -> {

                    val text = it.text
                    Log.d(TAG, "onMessage: $text")


                    val jsonObject = JSONObject(text)
                    var responseObj = text!!

                    if (jsonObject.has("data")) {
                        responseObj = jsonObject.getString("data")
                    }

                    val response = gson.fromJson(responseObj, ChatJson::class.java)
                    if(response.type=="chat"){
                        Log.d(TAG, "onMessage: $response")

                        messageList.add(Resource.Success(response))
                    }


                }
            }
        }
    }
}


/*

@HiltViewModel
//class ExpertChatViewModel @Inject constructor(
    private val api: Api
) : ViewModel(), SafeApiCall {

    companion object {
        private const val TAG = "ExpertChatViewModel"
    }

    private val gson by lazy { Gson() }
    private var easyWs: EasyWS? = null

    var message by mutableStateOf("")
        private set

    //TODO can be remove
    private val existingMessageList =
        mutableStateOf<Resource<ChatBetweenUserAndExpertResponse>>(Resource.Loading)

    val messageList = mutableStateListOf<Resource<ExpertChatResponse>>()

    fun updateMessage(newValue: String) {
        message = newValue
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
//            listenUpdates()
        }
    }

    fun loadChatBetweenUserAndExpert(data: ChatBetweenUserAndExpertRequest) =
        viewModelScope.launch {
            val response = safeApiCall {
                api.loadChatBetweenUserAndExpert(
                    senderId = data.senderId,
                    queryId = data.queryId,
                )
            }

            when (response) {
                Resource.Loading -> Unit
                is Resource.Failure -> {
                    messageList.add(response)
                }

                is Resource.Success -> {
                    response.value.chatMessages.forEach {
                        messageList.add(Resource.Success(it))
                    }
                }

            }
            existingMessageList.value = response
        }


    */
/*----------------------------------- Web Socket --------------------------------*//*


    fun connectSocket(socketUrl: String = Constants.SELF_BEST_SOCKET_URL) =
        viewModelScope.launch(Dispatchers.IO) {
            // /chat/676/
            easyWs = OkHttpClient().easyWebSocket(socketUrl)
            Log.d(TAG, "connectSocket: Called Listen Channel")
            listenUpdates()
        }


    //TODO to store sender msg
    fun sendMessage(data: ExpertChatRequest) {
        val msg = gson.toJson(data)
        easyWs?.webSocket?.send(msg)
        messageList.add(Resource.Success(data.convertToExpertChatResponse()))
    }


    private suspend fun listenUpdates() {

        easyWs?.textChannel?.consumeEach {
            when (it) {
                is SocketUpdate.Failure -> {
                    messageList.add(Resource.Failure(message = it.exception?.message!!))
                }

                is SocketUpdate.Success -> {

                    val text = it.text
                    Log.d(TAG, "onMessage: $text")


                    val jsonObject = JSONObject(text)
                    var responseObj = text!!

                    if (jsonObject.has("data")) {
                        responseObj = jsonObject.getString("data")
                    }

                    val response = gson.fromJson(responseObj, ExpertChatResponse::class.java)
                    Log.d(TAG, "onMessage: $response")

                    messageList.add(Resource.Success(response))

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




*/
