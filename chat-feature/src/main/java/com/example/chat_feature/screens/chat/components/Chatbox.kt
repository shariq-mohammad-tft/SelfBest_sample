package com.example.chat_feature.screens.chat.components


import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun ChatBoxEditText(
    message: String,
    onChange: (message: String) -> Unit,
    onSend: (message: String) -> Unit,
    onImageIconClicked: () -> Unit = {},
) {


    Column() {


        Row {
            // Show button to select image
            Box(modifier = Modifier.padding(8.dp)) {
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



            OutlinedTextField(
                value = message,
                onValueChange = { onChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp, 0.dp, 15.dp, 5.dp),
                placeholder = { Text(text = "Type your message...", color = Color(0XFF707070)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions { onSend(message) },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0XFF3E3E3E),
                    backgroundColor = Color(0XFFFFFFFF),
                    unfocusedIndicatorColor = Color(0xFFC8C8C8),
                    focusedIndicatorColor = Color(0xFFC8C8C8),
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        // Pass the selected image Uri along with the message
                        onSend(message)

                    }) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color(0XFF1D71D4)
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = false
            )


        }

    }
}


/*@Composable
@Preview(showBackground = true)
fun ChatBoxEditTextPreview() {
ChatBoxEditText(message = "Amazing World!", onChange = {}, onSend = {}, imageUri = "")
//PhotoPreview()
}*/