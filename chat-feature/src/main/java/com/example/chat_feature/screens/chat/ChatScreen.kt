package com.example.chat_feature.screens.chat

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.chat_feature.data.InteractiveMessageRequest
import com.example.chat_feature.data.Message
import com.example.chat_feature.data.PlainMessageRequest
import com.example.chat_feature.data.experts.BotUnseenCountRequest
import com.example.chat_feature.data.experts.ExpertListRequest
import com.example.chat_feature.navigation.AppScreen
import com.example.chat_feature.navigation.CHAT_SELECTION_SCREEN
import com.example.chat_feature.navigation.ROUTE_ROOM
import com.example.chat_feature.screens.chat.components.*
import com.example.chat_feature.utils.Constants
import com.example.chat_feature.utils.Resource
import com.example.chat_feature.utils.createSocketUrl
import com.example.chat_feature.utils.normalText
import com.example.chat_feature.utils.toast
import com.example.chat_feature.view_models.ChatViewModel
import kotlinx.coroutines.launch

private const val TAG = "ChatScreen"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class)
@Composable
fun ChatScreen(
    navController: NavController,
    senderId: String,
    receiverId: String,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val messages = viewModel.messageList
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userId = viewModel.userId



    Log.d(
        TAG, "ChatScreen: SENDER - $userId and RECEIVER - $receiverId "
    )


    /*LaunchedEffect(Unit) {
        viewModel.connectSocket(socketUrl = Constants.SELF_BEST_SOCKET_URL.createSocketUrl(userId))
    }*/

    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE  -> {
                viewModel.seenBotMessage()
            }
            Lifecycle.Event.ON_RESUME  -> viewModel.connectSocket(Constants.SELF_BEST_SOCKET_URL.createSocketUrl(userId))
            Lifecycle.Event.ON_STOP  -> viewModel.closeConnection()
            else -> Unit
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Solution Point",
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.h6

                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(
                    0xFFF8F8F8
                )
            ), navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                    )
                }
            }, modifier = Modifier.shadow(
                10.dp,
                shape = RoundedCornerShape(0.dp).copy(
                    bottomStart = CornerSize(15.dp), bottomEnd = CornerSize(15.dp)
                ),

                )
        )
    }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f), state = listState
            ) {

                scope.launch {
                    // todo
                    // scroll rules
                    // automatically scroll down when new message arrives but,
                    // when user manually scroll up then prevent scroll down (so detect scroll up)
                    if (messages.size > 6 && !listState.isScrollInProgress) {
                        listState.scrollToItem(index = messages.lastIndex)
                    }
                }
                items(messages) { message ->

                    when (message) {
                        is Resource.Failure -> {
                            Log.i(TAG, "ChatScreen: $message")
                            if (message.errorCode == 500) {
                                //ErrorMessage("Server is down please try after sometime")
                                CardErrorMessage(message = "Server is down please try after sometime")
                            }
                        }

                        is Resource.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is Resource.Success -> {
                            // todo
                            // scroll rules
                            // automatically scroll down when new message arrives but,
                            // when user manually scroll up then prevent scroll down (so detect scroll up)
                            /* LaunchedEffect(messages.size > 6 && !listState.isScrollInProgress) {
                                 listState.scrollToItem(index = messages.lastIndex)
                             }*/

                            if (!message.value.channelId.isNullOrEmpty()) {
                                LaunchedEffect(Unit) {
                                    context.toast(message.value.channelId!!)

                                    // todo debug and check whether back press works

                                    navController.navigate(
                                        AppScreen.ExpertChat.buildRoute(
                                            senderId = userId,
                                            receiverId = message.value.channelId,
                                            queryId = message.value.queryId!!,
                                            )
                                    ) {
                                        Log.d(
                                            "queryIdd",
                                            message.value.queryId + userId + message.value.channelId
                                        )
                                        popUpTo(ROUTE_ROOM) {
                                            inclusive = false
                                        }
                                    }.also { viewModel.closeConnection() }
                                }


                            } else {
                                MessageCard(
                                    message = message.value, senderId = userId
                                ) { messageRequest ->
                                    viewModel.sendMessageToServer(messageRequest)
                                }
                            }


                        }
                    }

                }
            }

            ChatBoxEditTextForBot(
                message = viewModel.message,
                onChange = { viewModel.updateMessage(it) },
                onSend = {
                    if (it.normalText().isEmpty()) {
                        context.toast("Please Enter Message!")
                        return@ChatBoxEditTextForBot
                    }

                    val messageObj = buildPlainMessage(message = it, userId)
                    viewModel.sendMessageToServer(messageObj)
                    viewModel.updateMessage("")

                    scope.launch {
                        if (messages.isNotEmpty()) listState.scrollToItem(index = messages.lastIndex)
                    }

                },
            )
        }
    }


}

/*---------------------------------- Build Messages ----------------------------------------*/

private fun buildPlainMessage(message: String, userId: String): PlainMessageRequest {
    return PlainMessageRequest(
        senderId = userId,
        message = message,
    )
}

private fun buildInteractiveMessage(
    message: String, eventName: String, userId: String
): InteractiveMessageRequest {
    return InteractiveMessageRequest(
        senderId = userId, message = message, eventName = eventName
    )
}

/*--------------------------------------- Message Card ----------------------------------------*/

@Composable
fun MessageCard(
    message: Message? = null,
    senderId: String? = null,
    onSend: (messageRequest: InteractiveMessageRequest) -> Unit
) {

    message!!   // To Avoid preview conflict
    senderId!!   // To Avoid preview conflict

    var isEnabled by rememberSaveable { mutableStateOf(message.isButtonEnabled) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {

        if (!message.links.isNullOrEmpty()) {
            CardlinksMessage(message = message.links!!)
        }

        if (message.senderId == senderId) {
            if (message.eventMessage != null) {
                CardSelfMessage(message = message.eventMessage)
            } else {
                CardSelfMessage(message = message.message)
            }

        } else {
            if (message.message != "") {
                CardReceiverMessage(message = message.message, timestamp =  message.timeStamp?:"2023-04-19 20:25:13.218301+00:00")

            } else {
                CardReceiverMessage(message = "Here is something I found", timestamp = message.timeStamp?:"2023-04-19 20:25:13.218301+00:00")
            }

        }

        if (!message.buttons.isNullOrEmpty()) {


            ButtonGridLayout(
                buttons = message.buttons, isEnabled = isEnabled
            ) { button ->
                isEnabled = false
                val messageObj = buildInteractiveMessage(
                    eventName = button.id, message = button.value, userId = senderId
                )

                onSend.invoke(messageObj)

            }

        }


    }
}

@Composable
fun cardShapeFor(isMine: Boolean = true): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        isMine -> roundedCorners.copy(bottomEnd = CornerSize(0))
        else -> roundedCorners.copy(bottomStart = CornerSize(0))
    }
}


@Composable
@Preview(showBackground = true)
fun MessageCardPreview() {
    MessageCard(
        message = Message(
            senderId = Constants.BOT_ID, receiverId = Constants.USER_ID, message = "Hello"
        )
    ) {}
}



