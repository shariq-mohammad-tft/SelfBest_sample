package com.example.chat_feature.view_models

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat_feature.data.SocketUpdate
import com.example.chat_feature.data.bot_history.ChatJson
import com.example.chat_feature.data.experts.BotUnseenCountRequest
import com.example.chat_feature.data.experts.ChatData
import com.example.chat_feature.data.experts.Expert
import com.example.chat_feature.data.experts.ExpertListRequest
import com.example.chat_feature.network.Api
import com.example.chat_feature.network.web_socket.EasyWS
import com.example.chat_feature.network.web_socket.easyWebSocket
import com.example.chat_feature.utils.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class ExpertListViewModel @Inject constructor(
    private val api: Api,
    application: Application,
) : AndroidViewModel(application), SafeApiCall {

    companion object {
        private const val TAG = "ExpertListViewModel"
    }

    //    val expertList = mutableStateListOf<Resource<List<Expert>>>()
    val experts = mutableStateOf<Resource<List<Expert>>>(Resource.Loading)


//    private val _experts = MutableStateFlow<Resource<List<Expert>>>(Resource.Loading)
//    val experts = _experts

    val userId: String

    init {
        userId = application.applicationContext.getUserId().toString()
    }
    /*init {
        viewModelScope.launch(Dispatchers.IO) {
            listenUpdates()
            loadExpertList(ExpertListRequest(senderId = sharedPref.getString("id")))
        }
    }*/

    private val gson by lazy { Gson() }
    private var easyWs: EasyWS? = null

    val messagess = mutableStateListOf<Resource<ChatData>>()
    var botMessageCount by mutableStateOf(0)
    var unseenMessageCount by mutableStateOf(0)
    private set


    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private var scheduledFuture: ScheduledFuture<*>? = null


    var stateSearch by mutableStateOf(SearchScreenState())

    fun onAction(userAction: UserAction) {
        when (userAction) {
            UserAction.CloseIconClicked -> {
                stateSearch = stateSearch.copy(
                    isSearchBarVisible = false
                )
            }
            UserAction.SearchIconClicked -> {
                stateSearch = stateSearch.copy(
                    isSearchBarVisible = true
                )
            }

            is UserAction.TextFieldInput -> {
                stateSearch = stateSearch.copy(
                    searchText = userAction.text
                )
                searchActorsInList(userAction.text)
            }

        }
    }


    private fun searchActorsInList(searchQuery: String) {
        when (val expertsResource = experts.value) {
            is Resource.Success -> {
                val newList = if (searchQuery.isNullOrEmpty()) {
                    expertsResource.value
                } else {
                    expertsResource.value.filter {
                        it.fullName.contains(searchQuery, ignoreCase = true)
                    }
                }
                stateSearch = stateSearch.copy(list = newList)
                Log.d("filtered", newList.toString())
            }
            else -> {
                // Handle loading and error states as necessary
            }
        }
    }


    //TODO use shredpref for apassing sender id
    fun loadExpertList(data: ExpertListRequest) = viewModelScope.launch {
        val response = safeApiCall {
            api.loadExpertList(
                senderId = data.senderId
               // senderId = "736"
            ).expertsList
        }
//        _experts.emit(response)
        experts.value = response
    }

    fun loadBotHistory(data: ChatJson) = viewModelScope.launch {
        val response = safeApiCall {
            api.loadChatBetweenUserAndExpert(
                senderId = data.sentBy!!, queryId = data.query_id!!


            )
        }
    }

    sealed class UserAction {
        object SearchIconClicked : UserAction()
        object CloseIconClicked : UserAction()

        data class TextFieldInput(val text: String) : UserAction()
    }

    data class SearchScreenState(
        val searchText: String = "",
        val isSearchBarVisible: Boolean = false,
        val isSortMenuVisible: Boolean = false,
        val list: List<Expert> = emptyList()
    )


    /*------------------------------------web socket--------------------------------*/

    fun connectSocket(socketUrl: String = Constants.SELF_BEST_SOCKET_URL) {
        viewModelScope.launch(Dispatchers.IO) {
            easyWs = OkHttpClient().easyWebSocket(socketUrl)
            listenUpdates()
        }
    }

    fun getBotUnseenCount(data: BotUnseenCountRequest) {
        val msg = gson.toJson(data)
        Log.d("unseenPayload", msg.toString())
        scheduledFuture=executorService.scheduleAtFixedRate({
            easyWs?.webSocket?.send(msg)
        },0,1,TimeUnit.SECONDS)

    }

    private suspend fun listenUpdates() {
        easyWs?.webSocket?.send("")
        easyWs?.textChannel?.consumeEach {
            when (it) {
                is SocketUpdate.Failure -> {
                    Log.d("unseenPayload", "failed")
                }
                is SocketUpdate.Success -> {
                    val text = it.text
                    Log.d("unseenPayloadText", "onMessage: $text")
                    val jsonObject = JSONObject(text)

                    if (jsonObject.has("data")) {
                        val dataObj = jsonObject.getJSONObject("data")
                        if (dataObj.has("chat_data")) {
                            val chatDataObj = dataObj.getJSONObject("chat_data")
                            if (chatDataObj.has("bot_message_count")) {
                                botMessageCount = chatDataObj.getInt("bot_message_count")
                            }
                        }
                    }
                    unseenMessageCount=botMessageCount
                    Log.d("unseenPayloadText", "botMessageCount: $botMessageCount")
                }
            }
        }
    }
    private fun closeConnection() {
        easyWs?.webSocket?.close(1001, "Closing manually")
        Log.d("expertlistviewmodel", "closeConnection: CONNECTION CLOSED!")
    }

    override fun onCleared() {
        super.onCleared()
        closeConnection()
    }
}


