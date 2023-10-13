package com.example.chat_feature.screens.chat.components

import android.graphics.drawable.Icon
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

private const val TAG = "PhotoPreview"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class)
@Composable
fun PhotoPreview(
    imageUri: String? = null,
    navController: NavController,
) {

    /* TopAppBar(
         title = {
             Text(
                 text = "Preview",
                 textAlign = TextAlign.Center,
                 modifier = Modifier
                     .fillMaxWidth()
                     .wrapContentWidth(align= Alignment.CenterHorizontally),
             )
         },
         backgroundColor = Color(0xFFF8F8F8),
         modifier = Modifier.shadow(
             10.dp,
             shape = RoundedCornerShape(0.dp).copy(
                 bottomStart = CornerSize(15.dp),
                 bottomEnd = CornerSize(15.dp)
             ),
             //spotColor = Color(0xFFC8C8C8)
         ).fillMaxWidth(),

         navigationIcon = {
             IconButton(
                 onClick = {  },

             ) {
                 Icon(
                     imageVector = Icons.Default.ArrowBack,
                     contentDescription = "",
                 )
             }
         }

     )*/



    Log.d(TAG, "PhotoPreview: $imageUri")

    LaunchedEffect(Unit) {
        navController.previousBackStackEntry?.savedStateHandle?.set(
            "from_preview",
            true
        )

    }

    Scaffold(topBar = {CenterAlignedTopAppBar(
        title = { Text("Preview",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.h6,

            ) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFF8F8F8)),
        navigationIcon = {
            IconButton(onClick = {navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        modifier = Modifier.shadow(
            10.dp,
            shape = RoundedCornerShape(0.dp).copy(
                bottomStart = CornerSize(15.dp),
                bottomEnd = CornerSize(15.dp)
            ),
            //spotColor = Color(0xFFC8C8C8)
        )
    )

    }, content = {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(it)
        )
        {


            Box(modifier = Modifier.padding(1.dp)) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Image from photo picker",
                    modifier = Modifier
                        .fillMaxWidth()

                )
            }

        }
    })


}

@Composable
@Preview
fun previewPhoto() {
    PhotoPreview(navController = rememberNavController())
}