package com.example.chat_feature.screens.chat.components


import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chat_feature.navigation.AppScreen


@Composable
fun ChatBoxEditTextWithImage(
    message: String,
    onChange: (message: String) -> Unit,
    onSend: (message: String) -> Unit,
    onImageIconClicked: () -> Unit = {},
    onImageOpen: () -> Unit = {},
    onImageClose: () -> Unit = {},
    imageUri: String? = null,
) {
    Row(modifier = Modifier.padding(8.dp)) {
        // Show button to select image
        Box(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Bottom)
        ) {
            Button(
                onClick = {
                    // launcher.launch("image/*")
                    onImageIconClicked.invoke()
                },
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = buttonColors(Color(0XFF1D71D4))
            ) {
                Icon(
                    Icons.Filled.AttachFile,
                    contentDescription = "content description",
                    tint = Color(0XFFFFFFFF)
                )
            }
        }


        Column(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, Color(0xFFC8C8C8), shape = RoundedCornerShape(7.dp))
        ) {


            TextField(
                value = message,
                onValueChange = { onChange(it) },
                //  modifier = Modifier
                //.padding(vertical = 4.dp),
                placeholder = {
                    Text(
                        text = "Type your message...",
                        color = Color(0XFF707070)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                keyboardActions = KeyboardActions { onSend(message) },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0XFF3E3E3E),
                    backgroundColor = Color(0XFFFFFFFF),
//                    unfocusedIndicatorColor = Color(0xFFC8C8C8),
                    focusedIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = false,
                maxLines = 3
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.White)

            ) {
                HangoutsStyleTextWithImages(
                    imageUri = imageUri,
                    onImageOpen = onImageOpen,
                    onImageClose = onImageClose
                )
            }

        }

        IconButton(
            onClick = {
                // Pass the selected image Uri along with the message
                onSend(message)

            }, modifier = Modifier.align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color(0XFF1D71D4)
            )
        }


    }

}


@Composable
@Preview(showBackground = true)
fun ChatBoxEditTextWithImagePreview() {
    ChatBoxEditTextWithImage(
        message = "Amazing World!",
        onChange = {},
        onSend = {},
    )
}