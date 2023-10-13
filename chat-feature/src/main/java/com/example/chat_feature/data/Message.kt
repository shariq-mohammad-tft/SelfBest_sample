package com.example.chat_feature.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.chat_feature.data.response.Button
import java.util.*
import kotlin.collections.ArrayList


// If sender ID == My ID then show message on right side
// If receiver ID == My ID then show message on left side

data class Message(
    val messageId: String = UUID.randomUUID().toString(),
    val senderId: String = ((1..2).random()).toString(),
    val receiverId: String,
    val channelId: String? = null,//TODO channelId is equal to reciever id
    val message: String,
    val eventMessage: String?=null,
    val queryId: String? = null,
    // val query_Id:String?=null,
    //TODO queryId means room id
    val buttons: List<Button>? = listOf(
        Button("1001", "Yes", "Yes", "Yes"),
        Button("1002", "No", "No", "No"),
    ),
    var isButtonEnabled: Boolean = false,
    var isImgButtonEnabled: Boolean = false,
    var isDropDownEnabled: Boolean = false,
    val queryName: String? = null,
    val name: String? = null,
    var links:ArrayList<String>?= arrayListOf(),
    val timeStamp:String?=null,
    var wrongskill:String?=null,
    val file:String?=null,




    )


