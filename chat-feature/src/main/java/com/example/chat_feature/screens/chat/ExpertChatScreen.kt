package com.example.chat_feature.screens.chat


import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chat_feature.data.experts.ChatBetweenUserAndExpertRequest
import com.example.chat_feature.data.response.expert_chat.ExpertChatRequest
import com.example.chat_feature.navigation.AppScreen
import com.example.chat_feature.screens.chat.components.*
import com.example.chat_feature.utils.*
import com.example.chat_feature.view_models.ExpertChatViewModel
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "ExpertChatScreen"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class)
@Composable
fun ExpertChatScreen(
    navController: NavController,
    senderId: String,
    receiverId: String,
    queryId: String,
    viewModel: ExpertChatViewModel = hiltViewModel(),
) {

    val messages = viewModel.messageList
    Log.d(TAG, messages.toString())
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        Log.d(TAG, "ExpertChatScreen: $uri")

        uri?.let {
            Log.d(TAG, "ExpertChatScreen: FILE PATH - ${context.getFileFromUri(uri)?.absolutePath}")
            val file = context.getFileFromUri(uri)
            viewModel.updateUri(file?.absolutePath)
        }

    }


    Log.d(
        TAG,
        "ExpertChatScreen: SENDER - $senderId and RECEIVER - $receiverId and QueryId -$queryId"
    )



    LaunchedEffect(Unit) {

        val fromPreview =
            navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>(
                "from_preview"
            ) ?: false
        Log.d(TAG, "ExpertChatScreen: BACKSTACK VALUE -- $fromPreview")

        navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
            "from_preview"
        )

        if (!fromPreview) {
            Log.d(TAG, "ExpertChatScreen: LOADING CALLED.....")

            viewModel.loadChatBetweenUserAndExpert(
                ChatBetweenUserAndExpertRequest(
                    senderId = senderId,
                    //receiverId = receiverId,
                    queryId = queryId

                )
            )
            viewModel.connectSocket(
                socketUrl = Constants.SELF_BEST_SOCKET_URL.createSocketUrl(
                    senderId
                )
            )
        }
    }


    // todo Show Image Progress here....

    /*viewModel.imageUploadResponse.value.let {
        when (it) {
            is Resource.Success -> {
                imageLink = it.value.imageLink
            }
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Failure -> {
                // Show error UI
            }
        }
    }*/

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Solution Point",
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    fontWeight = FontWeight.SemiBold,

                    )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(
                    0xFFF8F8F8
                )
            ),
            navigationIcon = {
                IconButton(onClick = {navController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            modifier = Modifier.shadow(
                10.dp,
                shape = RoundedCornerShape(0.dp).copy(
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp)
                ),

                )
        )
    },content={
        Column(
            modifier = Modifier
                .fillMaxSize().padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = false,
                state = listState
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

                items(items = messages) { message ->

                    when (message) {

                        is Resource.Success -> {

                            val data = message.value

                            Log.d(TAG, "PROGRESS OF IMAGE ===  ${data.progress}")


                            /* if (data.sentBy == senderId) {
                                 Log.d("data.semtBy", data.sentBy)
                                 if (data.image != null) {
                                     PhotoSenderCard(imageLink = data.image, message=data.message!!,progress = (data.progress.toFloat() / 100))
                                     // todo add card self to display caption
                                 } else {
                                     CardSelfMessage(message = data.message!!)
                                 }
                             } else {
                                 if (data.image != null) {
                                     PhotoReceiverCard(imageLink = data.image, message=data.message!!)
                                     // todo add card self to display caption
                                 } else {
                                     Log.d("data.semtBy", data.sentBy!!)
                                     CardReceiverMessage(message = data.message!!)
                                 }
                             }*/
                            if (data.message.isNullOrEmpty() && data.image == null) {
                                context.toast("please enter message")
                            }

                            if (data.sentBy == senderId) {
                                // Sender's message
                                if (data.image != null) {
                                    PhotoSenderCard(
                                        imageLink = data.image,
                                        message = data.message ?: "",
                                        progress = (data.progress.toFloat() / 100),
                                        navController
                                    )
                                } else {
                                    CardSelfMessage(
                                        message = data.message ?: "",
                                    )
                                }
                            } else {
                                // Receiver's message
                                if (data.image != null) {
                                    PhotoReceiverCard(
                                        imageLink = data.image,
                                        message = data.message ?: "",
                                        navController
                                    )
                                } else {
                                    CardReceiverMessage(
                                        message = data.message ?: "",
                                    )
                                }
                            }


                            // todo make it visible according to data.
                            /*if (data.progress != 100) {
                                LinearProgressIndicator(
                                    progress = (data.progress.toFloat() / 100),
                                )
                            }*/
                        }
                        is Resource.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is Resource.Failure -> {
                            Log.i(TAG, "ChatScreen: $message")
                            ErrorMessage(message.errorCode.toString())
                        }
                    }
                }
            }





            if (viewModel.imageUri != null) {
                ChatBoxEditTextWithImage(
                    message = viewModel.message,
                    onChange = {
                        viewModel.updateMessage(it)
                    },
                    imageUri = viewModel.imageUri,
                    onSend = {

                        val messageObj = buildMessage(
                            message = viewModel.message.normalText(),
                            senderId = senderId,
                            receiverId = receiverId,
                            queryId = queryId,
                            imageLink = null    // will be changed via copy in ViewModel
                        )


                        viewModel.imageUri?.let { filePath ->
                            viewModel.uploadImage(
                                image = File(filePath),
                                data = messageObj
                            )
                        }

                        scope.launch {
                            if (messages.isNotEmpty())
                                listState.scrollToItem(index = messages.lastIndex)
                        }

                    },
                    onImageIconClicked = {
                        launcher.launch("image/*")
                    },

                    onImageOpen = {
                        scope.launch {

                            viewModel.imageUri?.let {
                                navController.navigate(
                                    AppScreen.PhotoPreview.buildRoute(
                                        imageUri = it
                                    )
                                )
                            }
                        }


                    },
                    onImageClose = {
                        viewModel.updateUri(null)
                    }
                )
            } else {
                ChatBoxEditText(
                    onImageIconClicked = {
                        Log.d("imageSelected", "")
                        launcher.launch("image/*")
                    },
                    message = viewModel.message,
                    onChange = { viewModel.updateMessage(it) },
                    onSend = {
                        if (it.normalText().isEmpty()) {
                            context.toast("Please Enter Message!")
                            return@ChatBoxEditText
                        }
                        val messageObj = buildMessage(
                            message = it,
                            senderId = senderId,
                            receiverId = receiverId,
                            queryId = queryId,
                            imageLink = null
                        )
                        viewModel.sendMessage(messageObj)
                        viewModel.updateMessage("")

                        scope.launch {

                            if (messages.isNotEmpty())
                                listState.scrollToItem(index = messages.lastIndex)
                        }

                    },
                )
            }

        }
    })



}


private fun buildMessage(
    message: String,
    senderId: String,
    receiverId: String,
    queryId: String,
    imageLink: String?,
): ExpertChatRequest {
    return ExpertChatRequest(
        senderId = senderId,
        receiverId = receiverId,
        message = message,
        queryId = queryId,
        imageLink = imageLink
    )
}