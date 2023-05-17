package com.tft.selfbest.ui.activites


import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.chat_feature.data.SocketUpdate
import com.example.chat_feature.data.experts.TotalUnseenCountRequest
import com.example.chat_feature.network.web_socket.EasyWS
import com.example.chat_feature.network.web_socket.easyWebSocket
import com.example.chat_feature.utils.Constants
import com.example.chat_feature.utils.createSocketUrl
import com.google.gson.Gson
import com.tft.selfbest.data.SelfBestPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

@RequiresApi(Build.VERSION_CODES.M)
@HiltViewModel
class HomeActivityViewModel @Inject constructor() : ViewModel() {

    private val gson = Gson()
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private var scheduledFuture: ScheduledFuture<*>? = null
    private var easyWS: EasyWS? = null
    private var botMessageCount = 0
    private var _unseenMessageCount = MutableLiveData<Int>()
    val unseenMessageCount: LiveData<Int> = _unseenMessageCount

    @Inject
    lateinit var preferences: SelfBestPreference

    @Inject
    @Named("appContext")
    lateinit var appContext: Context

    @RequiresApi(Build.VERSION_CODES.M)
    fun connectSocket(socketurl:String=Constants.SELF_BEST_SOCKET_URL){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                easyWS = OkHttpClient().easyWebSocket(socketurl, appContext)
                Log.d("HomeActivity", "Connection: CONNECTION established!")
                listenUpdates()
            }
        } catch (e: Exception) {
            Log.d("HomeActivityWebSocket",e.toString())
        }
    }

    fun getTotalUnseenCount(data: TotalUnseenCountRequest) {
        val msg = gson.toJson(data)
        Log.d("unseenPayload", msg.toString())
        scheduledFuture=executorService.scheduleAtFixedRate({
            easyWS?.webSocket?.send(msg)
        },0,1, TimeUnit.SECONDS)
    }

    private suspend fun listenUpdates() {
        try { easyWS?.webSocket?.send("")

            easyWS?.textChannel?.consumeEach {

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
                                if (chatDataObj.has("total_message_count")) {
                                    botMessageCount = chatDataObj.getInt("total_message_count")

                                    // updateUnseenMessageCountBadge(unseenMessageCount)
                                }
                            }
                        }
                        _unseenMessageCount.postValue(botMessageCount)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("HomeActivityWebSocket",e.toString())
        }
    }



    fun closeConnection() {
        easyWS?.webSocket?.close(1001, "Closing manually")
        Log.d("HomeActivity", "closeConnection: CONNECTION CLOSED!")
    }


}
