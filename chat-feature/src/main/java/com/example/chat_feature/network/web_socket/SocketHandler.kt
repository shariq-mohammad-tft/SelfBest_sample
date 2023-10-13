package com.example.chat_feature.network.web_socket


object SocketHandler {
    private var easySocketIO: EasySocketIo? = null

    fun getSocketInstance(): EasySocketIo {
        if (easySocketIO == null) {
            easySocketIO = EasySocketIo() // Or initialize your EasySocketIo instance here
        }
        return easySocketIO!!
    }
}