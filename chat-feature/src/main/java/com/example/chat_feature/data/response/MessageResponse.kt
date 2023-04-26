package com.example.chat_feature.data.response


import androidx.core.text.HtmlCompat
import com.example.chat_feature.data.Message
import com.example.chat_feature.utils.Constants
import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("buttons") val buttons: List<Button>,
    @SerializedName("message") val message: String,
    @SerializedName("sentBy") val sentBy: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("channel_id") val channelId: String?,
    //TODO add queryId
//    @SerializedName("queryId") val queryId: String?,

    @SerializedName("query_id") val queryId: String?,

    @SerializedName("full_name") val fullName: String?,
    @SerializedName("query_name") val queryName: String?,
    @SerializedName("links") var links:ArrayList<String>
) {
    fun convertToMessage() = Message(
        senderId = Constants.BOT_ID,
        receiverId = Constants.USER_ID, // todo this should be received from shared pref.
        message = message,
        buttons = buttons,
        isButtonEnabled = true,
        isDropDownEnabled = true,
        channelId = channelId,
        queryId = queryId,//TODO add this
        name = fullName,
        queryName = queryName,
        links = links
    )
}