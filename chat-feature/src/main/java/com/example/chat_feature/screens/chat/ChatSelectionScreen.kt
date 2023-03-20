package com.example.chat_feature.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chat_feature.navigation.AppScreen
import com.example.chat_feature.navigation.ROUTE_CHAT
import com.example.chat_feature.navigation.ROUTE_EXPERT_CHAT
import com.example.chat_feature.screens.chat.components.AnimatedBox
import com.example.chat_feature.utils.Constants
import com.example.chat_feature.view_models.ExpertChatViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatSelectionScreen(navController: NavController,
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = {

            navController.navigate(
                AppScreen.ChatBot.buildRoute(
                    senderId = Constants.USER_ID,
                    receiverId = Constants.EXPERT_ID
                )
            )

        }) {
            Text(text = "Go with Asker ID - ${Constants.USER_ID}")
        }

        Button(onClick = {
            navController.navigate(
                AppScreen.ChatBot.buildRoute(
                    senderId = Constants.EXPERT_ID,
                    receiverId = Constants.USER_ID
                )
            )
        }) {
            Text(text = "Go with Expert ID - ${Constants.EXPERT_ID}")
        }

        Button(onClick = {
            navController.navigate(
                AppScreen.ExpertChat.buildRoute(
                    senderId = Constants.EXPERT_ID,
                    receiverId = Constants.USER_ID,
                    queryId = Constants.query


                )
            )

        }) {
            Text(text = "Hey Expert, Chat with User AT - ${Constants.USER_ID}")
        }
//TODO here i have replaced ExpertChat from ChatBot for testing purpose
        Button(onClick = {
            navController.navigate(
                AppScreen.ExpertChat.buildRoute(
                    senderId = Constants.USER_ID,
                    receiverId = Constants.EXPERT_ID,
                    queryId = Constants.query
                )
            )

        }) {
            Text(text = "Hey User, Chat with Expert AT - ${Constants.EXPERT_ID}")
        }


        /*-------------------- Room List ----------------------*/

        /*Button(onClick = {

            navController.navigate(
                AppScreen.LoadRooms.buildRoute(
                    senderId = Constants.USER_ID,
                )
            )

        }) {
            Text(text = "Load Rooms for ID - ${Constants.USER_ID}")
        }

        Button(onClick = {
            navController.navigate(
                AppScreen.LoadRooms.buildRoute(
                    senderId = Constants.EXPERT_ID,
                )
            )
        }) {
            Text(text = "Load Rooms for ID - ${Constants.EXPERT_ID}")
        }*/

        /*-------------------- Photo upload ----------------------*/

        Button(onClick = { navController.navigate(
            AppScreen.PhotoUpload.buildRoute(
                senderId = Constants.USER_ID
            )
        ) }) {
            Text(text = "Photo Upload room id - ${Constants.USER_ID}")
        }

        Box(modifier = Modifier.size(50.dp)) {
            AnimatedBox()
        }
    }

}


@Composable
@Preview(showBackground = true)
fun ChatSelectionScreenPreview() {
    ChatSelectionScreen(rememberNavController())
}

