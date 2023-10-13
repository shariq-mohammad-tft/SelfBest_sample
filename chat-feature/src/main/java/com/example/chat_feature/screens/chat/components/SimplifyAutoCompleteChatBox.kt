package com.example.chat_feature.screens.chat.components

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chat_feature.R
import com.example.chat_feature.data.SuggestionResponse

@Composable
fun <T> SimplifyAutoCompleteChatBox(
    modifier: Modifier,
    message: String,
    suggestions: List<T>,
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {},
    onSend: (message: String) -> Unit,
    onChange: (message: String) -> Unit
) {
    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    Column() {
        if (suggestions.isNotEmpty()) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .heightIn(max = TextFieldDefaults.MinHeight * 5)
                    .shadow(2.dp),
                contentPadding = PaddingValues(15.dp, 5.dp, 15.dp, 0.dp)
            ) {
                items(suggestions) { suggestion ->
                    Row(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                view.clearFocus()
                                onItemClick(suggestion)
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val image_id = when ((suggestion as SuggestionResponse).app) {
                                "keka" -> {
                                    R.drawable.keka
                                }

                                "googlecalendar" -> {
                                    R.drawable.googlecalendar
                                }

                                "dbf" -> {
                                    R.drawable.dbf
                                }

                                "jira" -> {
                                    R.drawable.jira
                                }

                                else -> {
                                    R.drawable.simplify_icon
                                }
                            }
                            Image(
                                painter = painterResource(id = image_id),
                                contentDescription = "App Icon",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .width(35.dp)
                                    .height(35.dp)
                                    .padding(5.dp, 5.dp, 5.dp, 5.dp)
                            )
                            Text(
                                (suggestion as SuggestionResponse).text,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )

                            if ((suggestion as SuggestionResponse).intent.equals("Informational"))
                                Text(
                                    "Information",
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                        }
                    }
                }
            }

        }

        QuerySearch(
            message = message,
            onSend = onSend,
            onChange = onChange
        )
    }
}

@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    message: String,
    onSend: (message: String) -> Unit,
    onChange: (message: String) -> Unit,
) {

    Row {

        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 5.dp, 10.dp, 5.dp),
            placeholder = { Text(text = "Type your message...", color = Color(0XFF707070)) },
            value = message,
            onValueChange = { onChange(it) },
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