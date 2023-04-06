package com.example.chat_feature.screens.chat.components.telegram

import androidx.annotation.ColorInt
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import coil.compose.AsyncImage
import com.example.chat_feature.data.experts.Expert
import kotlin.math.absoluteValue

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ChatItem(user: Expert? = null, onClick: (user: Expert) -> Unit = {}) {
    user!!
    val iconSize = 14.dp
    val offsetInPx = LocalDensity.current.run { (iconSize / 2).roundToPx() }
    Row(
        Modifier
            .padding(15.dp, 10.dp, 15.dp, 10.dp)
            .clickable { onClick(user) }, verticalAlignment = Alignment.CenterVertically
    ) {

        Box {
            val color = remember(user.receiverName) {
                Color(user.receiverName.toHslColor())
            }
            var initials = (user.receiverName.split("\\s".toRegex())[0].take(1)).uppercase()
            if (user.receiverName.split("\\s".toRegex()).size > 1)
                initials += (user.receiverName.split("\\s".toRegex())[1].take(1)).uppercase()
//            Canvas(modifier = Modifier
//                    .clip(CircleShape)
//                    .size(50.dp)) {
//                drawCircle(SolidColor(color))
//            }

            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = color,
                            radius = this.size.maxDimension
                        )
                    },
                textAlign = TextAlign.Center,
                text = initials,
                color = Color.White,
                fontSize = TextUnit(16f, TextUnitType.Sp)
            )
//            AsyncImage(
//                //model = "https://st2.depositphotos.com/1036149/10097/i/600/depositphotos_100972090-stock-photo-fun-cartoon-superhero.jpg",
//
//                model="https://selfbest-chatbot-image.s3.amazonaws.com/images/e5f0247e-1a14-4d64-9efc-295789e2868e",
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .size(50.dp),
//                //contentScale = ContentScale.Crop,
//                contentDescription = "Image from photo picker",
//            )
            if (user.status) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = "",
                    tint = Color(0xFF2FD765),
                    modifier = Modifier
                        .offset {
                            IntOffset(x = +offsetInPx - 10, y = -offsetInPx + 10)
                        }
                        .clip(CircleShape)
                        .background(Color.White)
                        .size(iconSize)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Column(
            Modifier
                .padding(horizontal = 8.dp)
                .weight(7f)
        ) {
            Text(
                text = (user.fullName).capitalize(),
                fontWeight = FontWeight.SemiBold,
                fontSize = TextUnit(14f, TextUnitType.Sp),
                style = MaterialTheme.typography.button
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.queryText.capitalize(),
                fontStyle = FontStyle.Normal,
                fontSize = TextUnit(12f, TextUnitType.Sp),
                color = Color.Gray,
                style = MaterialTheme.typography.caption
            )
        }

        /*  Column(
              modifier = Modifier.weight(3f),
              horizontalAlignment = Alignment.End
          ) {

              val textColor: Color
              val fontWeight: FontWeight
              if (user.unSeenCount > 0) {
                  textColor = MaterialTheme.colors.secondary
                  fontWeight =  FontWeight.Bold
              } else {
                  textColor = MaterialTheme.colors.onBackground
                  fontWeight =  FontWeight.Light
              }

              Text(
                  text = "18/11/2022",
                  fontWeight = fontWeight,
                  fontSize = 12.sp,
                  color = textColor,
              )

              Spacer(modifier = Modifier.height(4.dp))
              if (user.unSeenCount > 0) {
                  Text(
                      text = user.unSeenCount.toString(),
                      color = Color.White,
                      fontSize = 11.sp,
                      modifier = Modifier
                          .background(
                              MaterialTheme.colors.secondary,
                              shape = CircleShape
                          )
                          .padding(8.dp)
                  )
              }
          }*/
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(5.dp, 0.dp, 0.dp, 0.dp)
        ) {
            if (user.unSeenCount == 0) {
                Text(
                    text = "08/02/2023",
                    fontWeight = FontWeight.Normal,
                    // color = selfBestDefaultColor,
                    fontSize = TextUnit(12f, TextUnitType.Sp),
                    style = MaterialTheme.typography.body2

                )
            } else {
                Text(
                    text = "+{${user.unSeenCount}}",
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.background(Color(0xFF2FD765)),
                    color = Color.White,
                    textAlign = TextAlign.Justify,
                    // color = selfBestDefaultColor,
                    fontSize = TextUnit(12f, TextUnitType.Sp)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            if (user.queryStatus) {
                Text(
                    text = "Done",
                    fontWeight = FontWeight.Normal,
                    // color = selfBestDefaultColor,
                    fontSize = TextUnit(12f, TextUnitType.Sp),
                    fontStyle = FontStyle.Italic,

                    )
            } else {
                Text(
                    text = "Inprogress",
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Justify,
                    // color = selfBestDefaultColor,
                    fontSize = TextUnit(12f, TextUnitType.Sp),
                    fontStyle = FontStyle.Italic,
                )
            }

        }
    }
}


@Preview
@Composable
fun ChatItemPreview() {
    ChatItem()
}

@ColorInt
fun String.toHslColor(saturation: Float = 0.5f, lightness: Float = 0.4f): Int {
    val hue = fold(0) { acc, char -> char.code + acc * 37 } % 360
    return ColorUtils.HSLToColor(floatArrayOf(hue.absoluteValue.toFloat(), saturation, lightness))
}