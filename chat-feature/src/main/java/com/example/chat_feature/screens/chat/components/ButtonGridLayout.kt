package com.example.chat_feature.screens.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chat_feature.data.response.Button
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun ButtonGridLayout(
    buttons: List<Button>,
    isEnabled: Boolean = true,
    onClick: (button: Button) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.8f)
            .padding(horizontal = 10.dp)
            .wrapContentHeight()
    ) {
        buttons.forEach { button ->
            var isSelected by rememberSaveable { mutableStateOf(false) }


            TextButton(
                modifier = Modifier
                    .padding(1.dp)
                    .defaultMinSize(minWidth = 90.dp),
                onClick = {
                    // Disable all buttons + mark current as selected
                    onClick.invoke(button)
                    isSelected = true

                },
                enabled = isEnabled,
                shape = RoundedCornerShape(19),
                //border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                /*colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFFFFFFF),
                    backgroundColor = Color(0xFF1D71D4)
                )*/
//                colors = if ((isSelected && !isEnabled) || isEnabled) {
                colors = if (isSelected) {
                    ButtonDefaults.buttonColors(
                        contentColor = Color(0xFFC8C8C8),
                        backgroundColor = Color(0xFF1D71D4),
                        disabledContentColor = Color(0xFFC8C8C8),
                        disabledBackgroundColor = Color(0xFF1D71D4),
                    )

                }else if(isEnabled){
                    ButtonDefaults.buttonColors(
                        backgroundColor = Color.LightGray.copy(alpha = 0.6f),
                    )
                }

                else {
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black,
                        backgroundColor = Color.LightGray.copy(alpha = 0.6f),
                        disabledContentColor = Color.Black.copy(alpha = 0.8f),
                    )
                }

            ) {
                Text(
                    text = if(button.value.contains("yes help", ignoreCase = true)) "Yes" else if(button.value.contains("Can't help", ignoreCase = true)) "No" else button.value,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }

}


@Composable
@Preview(showBackground = true)
fun ButtonGridLayoutPreview() {
    ButtonGridLayout(
        buttons = listOf(
            Button("1001", "Yes", "Yes", "Instant Expert Help"),
            Button("1001", "Yes", "Yes", "Online Resources"),
            Button("1001", "Yes", "Yes", "Exit Chat"),
            Button("1001", "Yes", "Yes", "Online Resources"),
        ),
        onClick = {}
    )
}