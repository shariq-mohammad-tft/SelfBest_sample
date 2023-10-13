package com.example.chat_feature.network.web_socket

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException

import io.socket.client.Ack
import io.socket.engineio.client.transports.Polling
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException

class EasySocketIo {
    private var socket: Socket? = null

    /*fun connect(socketUrl: String, onError: (String) -> Unit) {
         try {
             val options = IO.Options()
//             options.transports = arrayOf(io.socket.engineio.client.transports.WebSocket.NAME)
             options.reconnection = true //reconnection
             options.forceNew = true
             socket = IO.socket(socketUrl, options)

             socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                 val errorMessage = "Socket connection error: ${args?.getOrNull(0)}"
                 onError(errorMessage)
             }
         } catch (e: URISyntaxException) {
             e.printStackTrace()
         }

         socket?.connect()
     }

     fun disconnect() {
         socket?.disconnect()
     }

     fun isConnected(): Boolean {
         return socket?.connected() ?: false
     }

     fun sendEvent(eventName: String, data: Any?) {
         Log.e("Socket send", isConnected().toString())
         if(isConnected()) {
             socket?.emit(eventName, data)
         }
     }

     fun onEvent(eventName: String, listener: (JSONObject) -> Unit) {
         socket?.on(eventName, object : Emitter.Listener {
             override fun call(vararg args: Any?) {
                 Log.e("Socket Receive", eventName.toString())
                 if(eventName.equals("bot_reply")) {
                     val data = args[0] as JSONObject
                     listener(data)
                 }else{
                     Log.e("Simplify Socket", args[0].toString())
                 }
             }
         })
     }*/

    private val connectMutex = Mutex()

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun connect(socketUrl: String): Result<Unit> = withContext(Dispatchers.IO) {
        Log.e("Simplify Socket", "outside mutex")
        connectMutex.withLock {
            Log.e("Simplify Socket", "inside mutex")

            try {
//                 val options = IO.Options()
//                options.transports = arrayOf(Polling.NAME)

                socket = IO.socket(socketUrl)
                Log.e("Simplify Socket", "Connecting")
                var isConnected = false
                val connectResult = suspendCancellableCoroutine<Boolean> { continuation ->
                    val connectListener = Emitter.Listener {
                        if (!continuation.isCancelled && !continuation.isCompleted) {
                            isConnected = true
                            continuation.resume(true,null)
                        }
                    }

                    val connectErrorListener = Emitter.Listener { args ->
                        if (!continuation.isCancelled) {
                            isConnected = false
                            if (continuation.isActive) {
                                continuation.resume(false, null)
                            }
                        }
                    }

                    try {
                        socket?.on(Socket.EVENT_CONNECT, connectListener)
                        socket?.on(Socket.EVENT_CONNECT_ERROR, connectErrorListener)

                        socket?.connect()

                        continuation.invokeOnCancellation {
                            socket?.off(Socket.EVENT_CONNECT, connectListener)
                            socket?.off(Socket.EVENT_CONNECT_ERROR, connectErrorListener)
                        }
                    } catch (e: Exception) {
                        continuation.resumeWith(Result.failure(e))
                    }
                }

                if (connectResult && isConnected) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Socket connection failed"))
                }
            } catch (e: URISyntaxException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun disconnect() {
        socket?.off()
        socket?.disconnect()
    }

    fun isConnected(): Boolean {
        return socket?.connected() ?: false
    }

    /*     suspend fun sendEvent(eventName: String, data: Any?) = suspendCoroutine<Unit> { continuation ->
        try {
            socket?.emit(eventName, data, Ack { args ->
                val exception = args.getOrNull(0) as? Exception
                if (exception != null) {
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(Unit)
                }
            })
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }*/
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun sendEvent(eventName: String, data: Any?) = suspendCancellableCoroutine<Unit> { continuation ->
        try {
            Log.e("Simplify socket sending", "$eventName ${isConnected()}" )
            socket?.emit(eventName, data, Ack { args ->
                val exception = args.getOrNull(0) as? Exception
                Log.e("Simplify Socket send", eventName)
                if (exception != null) {
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(Unit, null)
                }
            })
            Log.e("Simplify Socket send", eventName)
            continuation.resume(Unit, null)
        } catch (e: Exception) {
            Log.e("Simplify So Exception", e.toString())
            continuation.resumeWithException(e)
        }

        continuation.invokeOnCancellation {
            // Handle cancellation if needed
        }
    }

    /*suspend fun sendEvent(eventName: String, vararg data: Any?) = suspendCoroutine<Unit> { continuation ->
            socket?.emit(eventName, data) { args ->
                // Handle the acknowledgement if needed
                // For simplicity, we're not using it in this example
                continuation.resume(Unit)
            }
        }*/


    fun onEvent(eventName: String, listener: (JSONObject) -> Unit) {
        Log.e("Simplify Socket", eventName)
        socket?.on(eventName, object : Emitter.Listener {
            override fun call(vararg args: Any) {
                val data = args.getOrNull(0) as? JSONObject
                if (data != null) {
                    listener(data)
                }
            }
        })
    }

}