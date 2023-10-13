@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chat_feature.screens.chat

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chat_feature.data.PlainMessageRequest
import com.example.chat_feature.data.socketIoRequest
import com.example.chat_feature.screens.chat.components.CardReceiverMessage
import com.example.chat_feature.screens.chat.components.CardSelfMessage
import com.example.chat_feature.screens.chat.components.ChatBoxEditText
import com.example.chat_feature.screens.chat.components.ComposableLifecycle
import com.example.chat_feature.screens.chat.components.SimplifyAutoCompleteChatBox
import com.example.chat_feature.screens.chat.components.SimplifyPathChatBox
import com.example.chat_feature.utils.*
import com.example.chat_feature.view_models.SimplifyPathViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimplifyPathChatScreen(
    navController: NavController,
    viewModel: SimplifyPathViewModel = hiltViewModel()
) {

    val listState = rememberLazyListState()
    val messages = viewModel.chatMessages
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userId = viewModel.userId
    val suggestions = viewModel.suggestions

    ComposableLifecycle { _, event ->
        when (event) {

            Lifecycle.Event.ON_RESUME -> viewModel.connetSocketIO(
                Constants.SIMPLIFYPATH_SOCKET_IO_URL
            )

            Lifecycle.Event.ON_CREATE -> viewModel.connetSocketIO(Constants.SIMPLIFYPATH_SOCKET_IO_URL)

            Lifecycle.Event.ON_PAUSE -> viewModel.disconnectSocket()

            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        viewModel.connetSocketIO(Constants.SIMPLIFYPATH_SOCKET_IO_URL)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    if (viewModel.isSocketConnected()) "connected" else "SimplifyPath Bot",
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
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    } else {
                        (context as? Activity)?.finish()
                    }
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
    },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyColumn(modifier = Modifier.weight(1f), state = listState)
                {
                    scope.launch {
                        // todo
                        // scroll rules
                        // automatically scroll down when new message arrives but,
                        // when user manually scroll up then prevent scroll down (so detect scroll up)
                        if (messages.size > 6 && !listState.isScrollInProgress) {
                            listState.scrollToItem(index = messages.lastIndex)
                        }
                    }
                    items(items = messages) { message ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (message.sentby.equals("user")) {
                                CardSelfMessage(
                                    message = message.message,
                                    timestamp = message.timestamp
//                                timestamp = /*data.timestamp?:*/ getCurrentTime()
                                )
                            } else {
                                Log.e("Simplify Card reciever", message.message)
                                CardReceiverMessage(
                                    message = message.message,
                                    timestamp = message.timestamp,
                                    botName = "Simplify path bot"
                                )
                            }
                        }

                    }
                }
                for(value in suggestions){
                    Log.e("Suggestion", value.text)
                }
                Log.e("Suggestions List", suggestions.toString())
                SimplifyAutoCompleteChatBox(
                    modifier = Modifier.size(40.dp),
                    message = viewModel.message,
                    onChange = {
                        viewModel.getQuerySuggestions(it)
                        viewModel.updateMessage(it)
                    },
                    suggestions = suggestions,
                    onSend = {
                        if (it.normalText().isEmpty()) {
                            context.toast("Please Enter Message!")
                            return@SimplifyAutoCompleteChatBox
                        }
                        val messageObj = buildMessage(message = it, userId, organisationId = 1)
                        viewModel.sendMessage(messageObj)
                        viewModel.updateMessage("")
                        viewModel.getQuerySuggestions("")
                        scope.launch {
                            if (messages.isNotEmpty()) listState.scrollToItem(index = messages.lastIndex)
                        }
                    },
                    onItemClick = {
                        if (it.text.normalText().isEmpty()) {
                            context.toast("Please Enter Message!")
                            return@SimplifyAutoCompleteChatBox
                        }
                        val messageObj = buildMessage(message = it.text, userId, organisationId = 1)
                        viewModel.sendMessage(messageObj)
                        viewModel.updateMessage("")
                        viewModel.getQuerySuggestions("")
                        scope.launch {
                            if (messages.isNotEmpty()) listState.scrollToItem(index = messages.lastIndex)
                        }
                    },
                )
               /* SimplifyPathChatBox(
                    message = viewModel.message,
                    onChange =  { viewModel.updateMessage(it) },
                    onSend = {
                        if (it.normalText().isEmpty()) {
                            context.toast("Please Enter Message!")
                            return@SimplifyPathChatBox
                        }
                        val messageObj = buildMessage(message = it, userId, organisationId = 1)
                        viewModel.sendMessage(messageObj)
                        viewModel.updateMessage("")
                        scope.launch {
                            if (messages.isNotEmpty()) listState.scrollToItem(index = messages.lastIndex)
                        }
                    }
                )*/
            }
        })
}

private fun buildMessage(message: String, userId: String, organisationId: Int): socketIoRequest {
    return socketIoRequest(
        userId = userId,
        message = message,
        organisationId = organisationId
    )
}