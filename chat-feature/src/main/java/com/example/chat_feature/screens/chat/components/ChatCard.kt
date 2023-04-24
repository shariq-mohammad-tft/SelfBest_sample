package com.example.chat_feature.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.chat_feature.LinkifyText
import com.example.chat_feature.navigation.AppScreen
import com.example.chat_feature.screens.chat.cardShapeFor
import com.example.chat_feature.utils.extractTime
import java.sql.Timestamp


@Composable
fun CardSelfMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.End

    ) {
        Text(
            modifier = Modifier.padding(6.dp),
            text = "You",
            style = MaterialTheme.typography.caption,
            color = Color(0xFF1D71D4)
        )

        Row {
            Text(
                modifier = Modifier
                    .padding(end = 2.dp)
                    .align(Alignment.Bottom),
                text = "12:52 AM",
                fontSize = 10.sp,
                style = MaterialTheme.typography.caption,
                color = Color(0xFF707070)
            )
            Card(
                modifier = Modifier.widthIn(max = 340.dp), shape = RoundedCornerShape(12.dp).copy(
                    bottomEnd = CornerSize(0)
                ), backgroundColor = Color(0xFF1D71D4)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = if(message.contains("yes help", ignoreCase = true)) "Yes" else if(message.contains("Can't help", ignoreCase = true)) "No" else message,
                    style = MaterialTheme.typography.caption,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CardSelfMessagePreview() {
    CardSelfMessage("Cool")
}


@Composable
fun CardReceiverMessage(message: String, timestamp: String) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.Start

    ) {

        Text(
            modifier = Modifier.padding(2.dp),
            text = "Codey",
            style = MaterialTheme.typography.caption,
            color = Color(0xFF1D71D4)
        )

        Row {
            Card(
                modifier = Modifier.widthIn(max = 300.dp), shape = RoundedCornerShape(12.dp).copy(
                    bottomStart = CornerSize(0)
                ), backgroundColor = Color.LightGray.copy(alpha = 0.6f)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = message.replace("<br>", "\n\n"),
                    style = MaterialTheme.typography.caption,
                    color = Color(0xFF3E3E3E)
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .align(Alignment.Bottom),
                text = String().extractTime(timestamp)!!,
                fontSize = 10.sp,
                style = MaterialTheme.typography.caption,
                color = Color(0xFF707070)
            )
        }

    }
}

@Composable
fun CardlinksMessage(message: ArrayList<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.Start

    ) {

        Text(
            modifier = Modifier.padding(2.dp),
            text = "Codey",
            style = MaterialTheme.typography.caption,
            color = Color(0xFF1D71D4)
        )

        Row {
            Card(
                modifier = Modifier.widthIn(max = 300.dp), shape = RoundedCornerShape(12.dp).copy(
                    bottomStart = CornerSize(0)
                ), backgroundColor = Color.LightGray.copy(alpha = 0.6f)
            ) {
                LinkifyText(
                    modifier = Modifier.padding(8.dp),
                    text = message.joinToString(separator = "\n"),
                    style = MaterialTheme.typography.caption,
                    color = Color(0xFF3E3E3E),
                    clickable = true
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .align(Alignment.Bottom),
                text = "01:33 AM",
                fontSize = 10.sp,
                style = MaterialTheme.typography.caption,
                color = Color(0xFF707070)
            )
        }

    }
}

@Composable
@Preview(showBackground = true)
fun CardReceiverMessagePreview() {
    CardReceiverMessage("Cool","time")
}

@Composable
fun CardErrorMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.Start

    ) {

        Text(
            modifier = Modifier.padding(2.dp),
            text = "Error",
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.error,
        )


        Row {
            Card(
                modifier = Modifier.widthIn(max = 340.dp), shape = RoundedCornerShape(12.dp).copy(
                    bottomStart = CornerSize(0)
                ), backgroundColor = Color.LightGray.copy(alpha = 0.6f)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = message.replace("<br>", "\n"),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                )
            }

        }

    }
}


/*@Composable
fun PhotoSenderCard(imageLink: String, message: String, progress:Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.End
    ) {

            Card(
                modifier = Modifier.widthIn(max = 340.dp), shape = RoundedCornerShape(12.dp).copy(
                    bottomEnd = CornerSize(0)
                ), backgroundColor = Color(0xFFF8F8F8)
            ) {

                Column {
                    AsyncImage(
                        model = imageLink,
                        contentDescription = "Image from photo picker",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp, 200.dp)
                            .padding(5.dp)
                    )
                    Text(text = message,
                        color=Color(0xFFF8F8F8),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(Color(0xFF1D71D4))
                            .width(200.dp),
                    )
                    if(progress !=100.0f){
                        LinearProgressIndicator(progress = progress,
                            modifier = Modifier.width(200.dp).height(5.dp),
                        color = Color.Red)
                    }

                }
            }



    }

}*/
@Composable
fun PhotoSenderCard(
    imageLink: String, message: String, progress: Float, navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier.widthIn(max = 220.dp),
            shape = RoundedCornerShape(12.dp).copy(bottomEnd = CornerSize(0)),
            backgroundColor = Color(0xFFF8F8F8)
        ) {
            Column {
                AsyncImage(model = imageLink,
                    contentDescription = "Image from photo picker",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp, 200.dp)
                        .clickable() {
                            navController.navigate(
                                AppScreen.PhotoPreview.buildRoute(
                                    imageUri = imageLink
                                )
                            )
                        })
                if (message.isNotEmpty()) {
                    Text(
                        text = message,
                        color = Color(0xFFF8F8F8),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(200.dp)
                            .background(Color(0xFF1D71D4))
                            .padding(8.dp),
                        fontSize = 10.sp,
                        style = MaterialTheme.typography.caption,
                    )
                }

                if (progress !in listOf(0f, 1f)) {
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .width(200.dp)
                            .height(8.dp),
                        color = MaterialTheme.colors.secondaryVariant
                    )
                }

            }
        }
    }
}

@Composable
@Preview
fun PhotoSenderCardPreview() {
    PhotoSenderCard(
        imageLink = "https://selfbest-chatbot-image.s3.amazonaws.com/images/edcf733e-b100-4f12-8b83-471358e4a980",
        "Cool Caption",
        100.0f,
        rememberNavController()
    )
}


@Composable
fun PhotoReceiverCard(imageLink: String, message: String, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Card(
            modifier = Modifier.widthIn(max = 220.dp), shape = RoundedCornerShape(12.dp).copy(
                bottomStart = CornerSize(0)
            ), backgroundColor = Color(0xFFF8F8F8)
        ) {

            Column {
                AsyncImage(model = imageLink,
                    contentDescription = "Image from photo picker",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp, 200.dp)
                        .clickable() {
                            navController.navigate(
                                AppScreen.PhotoPreview.buildRoute(
                                    imageUri = imageLink
                                )
                            )
                        })
                if (message.isNotEmpty()) {
                    Text(
                        text = message,
                        color = Color(0xFFF8F8F8),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(200.dp)
                            .background(MaterialTheme.colors.onBackground.copy(alpha = 0.7f))
                            .padding(8.dp),
                        fontSize = 10.sp,
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }


    }
}


@Composable
@Preview
fun PhotoReceiverCardPreview() {
    PhotoReceiverCard(
        imageLink = "https://selfbest-chatbot-image.s3.amazonaws.com/images/edcf733e-b100-4f12-8b83-471358e4a980",
        "Cool Caption",
        rememberNavController()
    )
}

@Composable
fun ImageProgress(progress: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier.size(200.dp, 200.dp), shape = RoundedCornerShape(12.dp).copy(
                bottomStart = CornerSize(0)
            ), backgroundColor = Color.Transparent
        ) {
            Text(
                text = progress.toString(),
                modifier = Modifier.align(Alignment.Start),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun ImageProgressPreview() {
    ImageProgress(progress = 50)

}

