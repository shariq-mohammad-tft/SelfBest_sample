package com.tft.selfbest.ui.activites


import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.chat_feature.data.SocketUpdate
import com.example.chat_feature.data.experts.TotalUnseenCountRequest
import com.example.chat_feature.network.web_socket.EasyWS
import com.example.chat_feature.network.web_socket.WebSocketInterface
import com.example.chat_feature.network.web_socket.easyWebSocket
import com.google.gson.Gson
import com.tft.selfbest.data.SelfBestPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
@HiltViewModel
class HomeActivityViewModel @Inject constructor(
    private val application: Application,
    private val preferences: SelfBestPreference
) : AndroidViewModel(application), WebSocketInterface {

    companion object {
        private const val TAG = "HomeActivityVM"
    }

    private val gson = Gson()
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private var scheduledFuture: ScheduledFuture<*>? = null
    private var botMessageCount = 0
    private var _unseenMessageCount = MutableLiveData<Int>()
    val unseenMessageCount: LiveData<Int> = _unseenMessageCount
    private var job: Job? = null
    private var isPaused = false


    override var easyWs: EasyWS? = null

    init {
        val userId = preferences.getLoginData?.id.toString()
        getTotalUnseenCount(TotalUnseenCountRequest(sentBy = userId))
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun connectSocket(socketUrl: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                easyWs = OkHttpClient().easyWebSocket(socketUrl, application.applicationContext)
                Log.d("HomeActivity", "Connection: CONNECTION established!")
                listenUpdates()
            } catch (e: Exception) {
                Log.d(TAG, "connectSocket: ${e.message}")
            }
        }
    }

    private fun getTotalUnseenCount(data: TotalUnseenCountRequest) {
        val msg = gson.toJson(data)
        Log.d("unseenPayload", msg.toString())

        stopUpdates()
        job = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                if (!isPaused) {
                    easyWs?.webSocket?.send(msg) // the function that should be ran every second
                    isPaused = true
                    delay(1000)
                }
            }
        }


        /*scheduledFuture = executorService.scheduleAtFixedRate({
            easyWs?.webSocket?.send(msg)
        }, 0, 1, TimeUnit.SECONDS)*/

    }

    private suspend fun listenUpdates() {
        easyWs?.webSocket?.send("")

        easyWs?.textChannel?.consumeEach {

            when (it) {
                is SocketUpdate.Failure -> {
                    Log.d("unseenPayload", "failed")
                    isPaused = false
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
                    isPaused = false
                    _unseenMessageCount.postValue(botMessageCount)
                }
            }
        }

    }


    override fun closeConnection() {
        easyWs?.webSocket?.close(1001, "Closing manually")
        Log.d("HomeActivity", "closeConnection: CONNECTION CLOSED!")
    }

    private fun stopUpdates() {
        job?.cancel()
        job = null
    }

    override fun onCleared() {
        super.onCleared()

        closeConnection()
        stopUpdates()

    }

}
