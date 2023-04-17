package com.example.chat_feature.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chat_feature.screens.chat.*
import com.example.chat_feature.screens.chat.components.PhotoPreview

private const val TAG = "AppNavHost"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_ROOM,
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {

        composable(
            route = AppScreen.ChatSelection.route
        ) {
            ChatSelectionScreen(navController)
        }


        composable(
//            route = "$ROUTE_CHAT?senderId={senderId}&receiverId={receiverId}",
            route = AppScreen.ChatBot.route, arguments = listOf(
                navArgument("senderId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("receiverId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
            )
        ) {
            val senderId = it.arguments?.getString("senderId")
            val receiverId = it.arguments?.getString("receiverId")

            ChatScreen(
                navController = navController, senderId = senderId!!, receiverId = receiverId!!
            )
        }

        composable(
//            route = "$ROUTE_E XPERT_CHAT?senderId={senderId}&receiverId={receiverId}",
            route = AppScreen.ExpertChat.route, arguments = listOf(
                navArgument("senderId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("receiverId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("query_id") {
                    type = NavType.StringType
                    defaultValue = ""
                },

                )
        ) {
            val senderId = it.arguments?.getString("senderId")
            val receiverId = it.arguments?.getString("receiverId")

            val queryId = it.arguments?.getString("query_id")
            Log.d("senderrrId", "${queryId}+ ${receiverId}")


            ExpertChatScreen(
                navController = navController,
                senderId = senderId!!,
                receiverId = receiverId!!,
                queryId = queryId!!
            )
        }

        /*---------------------------------- Load Rooms ---------------------------------*/

        composable(
            route = AppScreen.LoadRooms.route
        ) {
            RoomsList(navController)
        }


        composable(route = AppScreen.ExpertChathistoryViaLoadRoom.route,
            arguments = listOf(navArgument("senderId") {
                type = NavType.StringType
                defaultValue = ""
            }, navArgument("query_id") {
                type = NavType.StringType
                defaultValue = ""
            },
                navArgument("receiverId") {
                    type = NavType.StringType
                    defaultValue = ""
                }

            )) {
            val senderId = it.arguments?.getString("senderId")
            val queryId = it.arguments?.getString("query_id")
            val receiverId = it.arguments?.getString("receiverId")
            Log.d("RecssId", "${queryId}+ ${receiverId}")

            ExpertChatScreen(
                navController = navController,
                senderId = senderId!!,
                queryId = queryId!!,
                receiverId = receiverId!!
            )
        }



        composable(
            route = AppScreen.PhotoUpload.route, arguments = listOf(
                navArgument(SENDER_ID_ARGUMENT) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            )
        ) {
            val senderId = it.arguments?.getString(SENDER_ID_ARGUMENT)
            PhotoUploadScreen(
                navController = navController, senderId = senderId!!

            )
        }

        composable(
            route = AppScreen.PhotoPreview.route,
            arguments = listOf(
                navArgument(IMAGE_URI_ARGUMENT) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            )
        ) { navBackStackEntry ->


            val imageUris = navBackStackEntry.arguments?.getString(IMAGE_URI_ARGUMENT)

            Log.d(TAG, imageUris.toString())
            PhotoPreview(
                imageUri = imageUris,
                navController = navController,
            )

        }

    }
}