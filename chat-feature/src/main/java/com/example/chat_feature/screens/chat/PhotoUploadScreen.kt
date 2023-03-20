package com.example.chat_feature.screens.chat

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chat_feature.navigation.AppScreen
import com.example.chat_feature.navigation.CHAT_SELECTION_SCREEN
import com.example.chat_feature.navigation.ROUTE_PHOTO_PREVIEW
import com.example.chat_feature.utils.Resource
import com.example.chat_feature.view_models.PhotoUploadViewModel
import java.io.File
import java.io.InputStream

@Composable
fun PhotoUploadScreen(
    navController: NavController,
    senderId: String,
    viewModel: PhotoUploadViewModel= hiltViewModel(),

    ) {
    var imageLink: String? = null

    var imageUri = remember { mutableStateOf<Uri?>(null) } // UPDATE
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri // UPDATE
    }

    viewModel.response.observeAsState().value?.let { resource ->
        when (resource) {
            is Resource.Loading -> {
                // Show loading UI
                CircularProgressIndicator()
            }
            is Resource.Failure -> {
                // Show error UI
            }
            is Resource.Success -> {

                imageLink = resource.value.imageLink
                /*if (!imageLink.isNullOrEmpty()) {
                LaunchedEffect(Unit) {
                    Log.d("imgss", imageLink.toString())

                        navController.navigate(
                            AppScreen.PhotoPreview.buildRoute(
                                senderId = senderId,
                                receiverId = imageLink.toString()!!,
                            )
                        ) {
                            popUpTo(ROUTE_PHOTO_PREVIEW) {
                                inclusive = false
                            }
                        }
                    }
                    // Use the imageLink to update the UI
                }*/
            }
        }

    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        //imagePath= getFileFromUri(uri = result!!)!!

        Button(onClick = { launcher.launch("*/*") }) {
            Text(text = "pick photo")
            if (imageUri.value!=null) {

                LaunchedEffect(Unit ) {


                    Log.d("imgss", imageUri.value.toString())

                    navController.navigate(
                        AppScreen.PhotoPreview.buildRoute(
                            //senderId = senderId,
                            imageUri = imageUri.value.toString(),
                        )
                    ) {
                        Log.d("RimgUri", imageUri.value.toString())
                        popUpTo(CHAT_SELECTION_SCREEN) {
                            inclusive = false
                        }
                    }

                } // Use the imageLink to update the UI
            }


        }
        Button(onClick = {
            imageUri?.let { uri ->
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri.value!!)
                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "my_image.jpg"
                )
                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                viewModel.uploadImage(file)
            }

        }
        ) {
            Text("Upload Image")
        }

        AsyncImage(
            model = imageUri.value.toString(),
            contentDescription = "Image from photo picker",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp, 200.dp)
                .clip(CircleShape)
        )

    }
}



@Composable
fun PhotoPre(img:String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "preview screen")
        AsyncImage(model = img,  contentDescription = "Image from photo picker",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp, 200.dp)
                .clip(CircleShape) )

    }
}
