package com.example.chat_feature.navigation

import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.chat_feature.R
import retrofit2.http.QueryName

//const val ROUTE_CHAT = "chat_screen?senderId={senderId},receiverId={receiverId}"
//const val ROUTE_CHAT = "chat_screen/{senderId}/{receiverId}"
const val ROUTE_CHAT = "chat_screen"
const val ROUTE_EXPERT_CHAT = "expert_chat_screen"
const val ROUTE_ROOM = "rooms"
const val ROUTE_ROOM_CLOSED = "rooms"
const val CHAT_SELECTION_SCREEN = "chat_selection_screen"
const val ROUTE_UPLOAD_PHOTO = "photo_upload"
const val ROUTE_PHOTO_PREVIEW = "photo_preview"
const val SIMPLIFY_CHATBOT = "simplify_chatbot"

const val SENDER_ID_ARGUMENT = "senderId"
const val RECEIVER_ID_ARGUMENT = "receiverId"
const val QUERY_ID_ARGUMENT = "query_id"
const val IMAGE_URI_ARGUMENT = "image_uri"
const val QUERY_NAME_ARGUMENT = "query_name"

sealed class AppScreen(
    @StringRes val title: Int, @DrawableRes val icon: Int? = null, val route: String,
) {
    object ChatSelection : AppScreen(
        title = R.string.chat_selection, route = ROUTE_ROOM
    )

    object ChatBot : AppScreen(
        title = R.string.chat_bot,
        route = "$ROUTE_CHAT/{$SENDER_ID_ARGUMENT}/{$RECEIVER_ID_ARGUMENT}"
    ) {
        fun buildRoute(senderId: String, receiverId: String) =
            route.replace(oldValue = "{$SENDER_ID_ARGUMENT}", newValue = senderId)
                .replace(oldValue = "{$RECEIVER_ID_ARGUMENT}", newValue = receiverId)
    }

    object ExpertChat : AppScreen(
        title = R.string.expert_chat,
        route = "$ROUTE_EXPERT_CHAT/{$SENDER_ID_ARGUMENT}/{$RECEIVER_ID_ARGUMENT}/{$QUERY_ID_ARGUMENT}/{$QUERY_NAME_ARGUMENT}"
    ) {
        fun buildRoute(senderId: String, receiverId: String, queryId: String, queryName: String) =
            route.replace(oldValue = "{$SENDER_ID_ARGUMENT}", newValue = senderId)
                .replace(oldValue = "{$RECEIVER_ID_ARGUMENT}", newValue = receiverId)
                .replace(oldValue = "{$QUERY_ID_ARGUMENT}", newValue = queryId)
                .replace(oldValue = "{$QUERY_NAME_ARGUMENT}", newValue = queryName)



        //TODO add queryID
    }

    object ExpertChathistoryViaLoadRoom : AppScreen(
        title = R.string.expert_chat,
        route = "$ROUTE_ROOM/{$SENDER_ID_ARGUMENT}/{$QUERY_ID_ARGUMENT}/{$RECEIVER_ID_ARGUMENT}/{$QUERY_NAME_ARGUMENT}"
    ) {
        fun buildRoute(senderId: String, queryId: String,receiverId: String, queryName: String) =
            route.replace(oldValue = "{$SENDER_ID_ARGUMENT}", newValue = senderId)
                .replace(oldValue = "{$RECEIVER_ID_ARGUMENT}", newValue = receiverId)
                .replace(oldValue = "{$QUERY_ID_ARGUMENT}", newValue = queryId)
                .replace(oldValue = "{$QUERY_NAME_ARGUMENT}", newValue = queryName)

        //TODO add queryID
    }

    object ExpertChathistoryViaLoadRoomForClosedQuery : AppScreen(
        title = R.string.expert_chat_Closed_Room,
        route = "$ROUTE_ROOM_CLOSED/{$SENDER_ID_ARGUMENT}/{$QUERY_ID_ARGUMENT}/{$QUERY_NAME_ARGUMENT}"
    ) {
        fun buildRoute(senderId: String, queryId: String, queryName: String) =
            route.replace(oldValue = "{$SENDER_ID_ARGUMENT}", newValue = senderId)
               // .replace(oldValue = "{$RECEIVER_ID_ARGUMENT}", newValue = receiverId)
                .replace(oldValue = "{$QUERY_ID_ARGUMENT}", newValue = queryId)
                .replace(oldValue = "{$QUERY_NAME_ARGUMENT}", newValue = queryName)


        //TODO add queryID
    }

    /*object LoadRooms : AppScreen(
        title = R.string.room_list, route = "$ROUTE_ROOM/{$SENDER_ID_ARGUMENT}"
    ) {
        fun buildRoute(senderId: String) =
            route.replace(oldValue = "{$SENDER_ID_ARGUMENT}", newValue = senderId)
    }*/
    object LoadRooms : AppScreen(
        title = R.string.room_list, route = "$ROUTE_ROOM"
    )

    object PhotoUpload : AppScreen(
        title = R.string.upload_photo, route = "$ROUTE_UPLOAD_PHOTO"
    ) {
        fun buildRoute(senderId: String) =
            route.replace(oldValue = "{$SENDER_ID_ARGUMENT}", newValue = senderId)

    }

    object PhotoPreview : AppScreen(
        title = R.string.photo_preview, route = "$ROUTE_PHOTO_PREVIEW?data={$IMAGE_URI_ARGUMENT}"
    ) {
        fun buildRoute(imageUri: String): String {
            val path = route.replace(oldValue = "{$IMAGE_URI_ARGUMENT}", newValue = imageUri)
            Log.d("path", path)
            return path
        }
    }

    object SimplifyChatbot : AppScreen(
        title = R.string.room_list, route = SIMPLIFY_CHATBOT
    )

}
