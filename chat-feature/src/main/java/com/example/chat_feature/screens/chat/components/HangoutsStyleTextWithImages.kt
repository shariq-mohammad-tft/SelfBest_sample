package com.example.chat_feature.screens.chat.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.chat_feature.R


@Composable
fun HangoutsStyleTextWithImages(
    imageUri: String? = null,
    onImageOpen: () -> Unit = {},
    onImageClose: () -> Unit = {},
) {


    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        imageUri?.let {
            item {
                MyIconBox(
                    imageUri = imageUri,
                    onImageOpen = { onImageOpen() },
                    onImageClose = { onImageClose() },
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyIconBox(
    imageUri: String?,
    onImageOpen: () -> Unit = {},
    onImageClose: () -> Unit = {},
) {
    val iconSize = 15.dp
    val offsetInPx = LocalDensity.current.run { (iconSize / 2).roundToPx() }

    Box(modifier = Modifier.padding((iconSize / 2))) {

        Card(
            shape = RoundedCornerShape(5.dp),
            onClick = onImageOpen,
        ) {

            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier.width(60.dp).height(100.dp),
                contentScale = ContentScale.Crop
            )
        }

        IconButton(
            onClick = onImageClose,
            modifier = Modifier
                .offset {
                    IntOffset(x = +offsetInPx, y = -offsetInPx)
                }
                .clip(CircleShape)
                .background(Color.White)
                .size(iconSize)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HangoutsStyleTextWithImagesPreview() {
    HangoutsStyleTextWithImages()
    MyIconBox(imageUri = null)
}