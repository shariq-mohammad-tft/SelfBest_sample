package com.example.chat_feature.screens.chat.components.telegram


import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.chat_feature.data.experts.Expert
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
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
                text = (user.fullName).replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.SemiBold,
                fontSize = TextUnit(14f, TextUnitType.Sp),
                style = MaterialTheme.typography.button
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.queryText.replaceFirstChar { it.uppercase() },
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
            val formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val updateAt= LocalDate.parse(user.createdAt.substring(0,10),formatter)
            val displayFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val displayDate = updateAt.format(displayFormat)
            if (user.unSeenCount==0) {
                Text(
                    text = displayDate.toString(),
                    fontWeight = FontWeight.Normal,
                    // color = selfBestDefaultColor,
                    fontSize = TextUnit(12f, TextUnitType.Sp),
                    style = MaterialTheme.typography.body2

                )
            } else {
                Text(
                    text = " +${user.unSeenCount}",
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.background(Color(0xFF2FD765), shape = RoundedCornerShape(20)),
                    color = Color.White,
                    textAlign = TextAlign.Justify,
                    // color = selfBestDefaultColor,
                    fontSize = TextUnit(12f, TextUnitType.Sp)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            if (user.queryStatus) {
                Text(
                    text = "Closed",
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


@RequiresApi(Build.VERSION_CODES.O)
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