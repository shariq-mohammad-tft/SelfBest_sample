package com.example.chat_feature.network.web_socket

import android.util.Log
import com.example.chat_feature.data.SocketUpdate
import com.example.chat_feature.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

/*
private val pieSocketUrl =
    "wss://demo.piesocket.com/v3/channel_1?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self"
private val client by lazy { OkHttpClient() }

private const val TAG = "TestJob"

fun main() = runBlocking {
    println("main Start!")
    socketJob()

//    testingChannelAndFlow()
//    client.dispatcher.executorService.shutdown()
    println("main Finished!")
}

private fun CoroutineScope.testingChannelAndFlow() {
//    val channel = MutableSharedFlow<String>()
    val channel = Channel<String>()

    launch {
        for (num in channel) {
            delay(3000)
            println("[RECEIVED - 1] <-- $num")
        }

        */
/*channel.collect { num ->
            delay(3000)
            println("[RECEIVED - 1] <-- $num")
        }*//*

    }
    launch {
        for (num in channel) {
            delay(1000)
            println("[RECEIVED - 2] <-- $num")
        }


        */
/* channel.collect { num ->
             delay(1000)
             println("[RECEIVED - 2] <-- $num")
         }*//*

    }

    launch {
        repeat(10) {
            channel.send(it.toString())
            delay(500)
        }
    }
}

private suspend fun socketJob() = withContext(Dispatchers.IO) {
    try {
       val easyWS = client.easyWebSocket(Constants.SELF_BEST_SOCKET_URL,)
        println("[socketJob] Open: ${easyWS.response}")

        */
/*launch {
            val msg = "{\n" +
                    "    \"type\": \"subscribe\",\n" +
                    "    \"channels\": [{ \"name\": \"ticker\", \"product_ids\": [\"BTC-EUR\"] }]\n" +
                    "}"

            easyWS.webSocket.send(msg)
        }*//*

        */
/*launch {
            repeat(5) {
                easyWS.webSocket.send(it.toString())
                println("[socketJob] --> $it")
                delay(1000)
            }
        }*//*


        */
/*launch {
            repeat(10) {
                delay(1000)
                if (it > 5) {
                    easyWS.webSocket.close(1000, "Bye! [socketJob]")
//                client.dispatcher.executorService.shutdown()
                }
            }
        }*//*


        easyWS.textChannel.consumeEach { msg ->
            when (msg) {
                is SocketUpdate.Failure -> println("[socketJob] <-- ${msg.exception}")
                is SocketUpdate.Success -> println("[socketJob] <-- ${msg.text}")
            }
        }

        println("[socketJob] Finish!")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d(TAG, "socketJob: ${e.message}")
    }
}*/
