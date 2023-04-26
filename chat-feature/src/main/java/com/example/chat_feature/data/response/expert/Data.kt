package com.example.chat_feature.data.response.expert


import android.text.Html
import androidx.core.text.HtmlCompat
import com.example.chat_feature.data.Message
import com.example.chat_feature.data.response.Button
import com.example.chat_feature.utils.Constants
import com.google.gson.annotations.SerializedName

data class Data(

    //todo this the socket response when user click on INSTANT EXPERT HELP(E29)
    //TODO when expert clicked on yes (Called only once)
    //todo This should be HTML Message
    @SerializedName("message")
    val message: String,

    @SerializedName("sentBy")
    val sentBy: String,

    @SerializedName("sentTo")
    val sentTo: String?,

    @SerializedName("type")
    val type: String,

    @SerializedName("buttons")
    val buttons: List<Button>? = mutableListOf(),

    @SerializedName("timestamp")
    val timestamp: String?,

    @SerializedName("channel_id")
    val channelId: String?,

    //todo add query id
    @SerializedName("query_id")
    val queryId: String?,

    @SerializedName("full_name")
    val fullName: String?,

    @SerializedName("query_name")
    val queryName: String?,

    ) {

    fun convertToMessage(): Message {
        return Message(
            channelId = channelId,
            message = message,
            buttons = buttons,
            isButtonEnabled = true,
            isDropDownEnabled = true,
            senderId = sentBy,
            receiverId = channelId ?: Constants.EXPERT_ID,
            queryId = queryId,
            name = fullName,
            queryName = queryName,
        )
    }
}

//Spanned result = HtmlCompat.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);;