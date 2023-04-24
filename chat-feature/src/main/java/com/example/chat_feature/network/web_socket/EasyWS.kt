package com.example.chat_feature.network.web_socket

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.chat_feature.data.SocketUpdate
import com.example.chat_feature.data.SocketUpdate.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.UnknownHostException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class EasyWS(val webSocket: WebSocket, val response: Response) {
    val textChannel = Channel<SocketUpdate>()
}


@RequiresApi(Build.VERSION_CODES.M)
@OptIn(DelicateCoroutinesApi::class)
suspend fun OkHttpClient.easyWebSocket(url: String, context: Context)= suspendCoroutine {

    println("easyWebSocket: $url")
    var easyWs: EasyWS? = null

    if (!isNetworkConnected(context)) {
        val errorMessage = "No network connectivity"
        println(errorMessage)
        //it.resumeWithException(IOException(errorMessage))
        return@suspendCoroutine
    } else{
        newWebSocket(Request.Builder().url(url).build(), object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("onOpen: $response")
                easyWs = EasyWS(webSocket, response)        // DID Type Casting
                it.resume(easyWs!!)
            }


            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                if(t is UnknownHostException){
                    MainScope().launch {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()

                        // Notify the textChannel of the failure
                        easyWs?.textChannel?.send(Failure(exception = t))
                    }
                }
                else{
                    println("onFailure: $t $response ${t.cause}")
                    GlobalScope.launch { easyWs!!.textChannel.send(Failure(exception = t)) }
                   // it.resumeWithException(t)
                }
            }


            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                println("onClosing: $code $reason")
                webSocket.close(1000, "Bye!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)

                println("onMessage: $text")
                GlobalScope.launch { easyWs!!.textChannel.send(Success(text)) }
            }

            /*override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)

                // println("<--[B] $bytes")
            }*/


            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                println("onClosed: $code $reason")
                easyWs!!.textChannel.close()
            }

        })
    }
    }


@RequiresApi(Build.VERSION_CODES.M)
fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

