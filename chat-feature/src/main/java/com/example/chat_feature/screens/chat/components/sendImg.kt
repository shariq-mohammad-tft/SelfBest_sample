@file:OptIn(ExperimentalMaterialApi::class)


import android.graphics.fonts.FontFamily
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material.icons.twotone.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.chat_feature.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            ExpertList()
//            ExpertCardComposable()
            BotAndExpert()

        }
    }
}


data class User(
    val id: Int
)

val users = listOf(
    User(1),
    User(2),
    User(3),
    User(4),
    User(5),
    User(6),
    User(7),
    User(8),
    User(9),
    User(10),
    User(11),
    User(12),
    User(13),
    User(14),
    User(15),
    User(16),
    User(17),
    User(18),
    User(19),
    User(20),
)

@Composable
fun BotAndExpert() {
    Column(
        verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
    ) {
        BotCard()
        UserList()
    }
}

@Composable
fun UserList() {
    val context = LocalContext.current
//  Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
//    for(i in 1..10){
//      UserCard()
//    }
//  }
    LazyColumn {
        items(users) { user ->
            NewUserCard()
            Divider(color = Color(0xFFC8C8C8), thickness = 1.dp, modifier = Modifier.padding(15.dp,0.dp,15.dp,0.dp))
        }
    }
}

@Composable
fun BotCard() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(1.dp)
            .background(color = Color(0xFFF8F8F8))
            .clip(RoundedCornerShape(10.dp)), contentAlignment = Alignment.TopStart
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(8.dp))
                .padding(1.dp),
            onClick = { Toast.makeText(context, "clicked on this", Toast.LENGTH_LONG).show() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .padding(2.dp)
                    .background(color = Color(0xFFF8F8F8))
                    .clip(RoundedCornerShape(10.dp)),
                horizontalArrangement = Arrangement.SpaceAround


            ) {
                Card(
                    shape = CircleShape, border = BorderStroke(2.dp, color = Color(0xFF1d71D4)),
                    modifier = Modifier
                        .size(48.dp, 48.dp)
                        .padding(0.dp),
                    onClick = { Toast.makeText(context, "Profile Photo", Toast.LENGTH_LONG).show() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.iconrobot),
                        contentDescription = "Bot Picture Holder",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(240.dp, 50.dp),
                    verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Self.Best Bot",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1d71D4)
                    )
                    Text(
                        text = "This is the last Message",
                        color = Color(0xFF1d71D4),
                        fontStyle = FontStyle.Italic
                    )
                }
                Text(
                    text = "18/11/2022",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1d71D4),

                    )

            }
        }
    }
}


@Composable
fun UserCard() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
            .clip(RoundedCornerShape(10.dp)), contentAlignment = Alignment.TopStart
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(8.dp))
                .padding(2.dp),
            onClick = { Toast.makeText(context, "clicked on card", Toast.LENGTH_LONG).show() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceAround


            ) {
                Card(
                    shape = CircleShape, border = BorderStroke(2.dp, color = Color(0xFF1d71D4)),
                    modifier = Modifier
                        .size(48.dp, 48.dp)
                        .padding(0.dp),
                    onClick = { Toast.makeText(context, "Profile Photo", Toast.LENGTH_LONG).show() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = "Bot Picture Holder",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(240.dp, 50.dp),
                    verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Self.Best Bot",
                        fontWeight = FontWeight.Bold,
                        //   color = selfBestDefaultColor
                    )
                    Text(
                        text = "This is the last Message",
                        //color = selfBestDefaultColor,
                        fontStyle = FontStyle.Italic
                    )
                }
                Text(
                    text = "18/11/2022",
                    fontWeight = FontWeight.SemiBold,
                    // color = selfBestDefaultColor,

                )

            }
        }
    }
}

@Composable
fun NewBotCardDesign() {
    val context= LocalContext.current
    Row(modifier = Modifier.padding(15.dp)) {
        Card(shape = RoundedCornerShape(50), backgroundColor = Color(0xFF7630F2),
            modifier = Modifier
                .fillMaxWidth()
                .height(57.dp)
                .weight(1f),
            onClick = {Toast.makeText(context, "Clicked on Bot", Toast.LENGTH_LONG).show()}) {

            Box() {
                Card(shape = CircleShape, backgroundColor = Color(0xFFFFFFFF),
                    modifier = Modifier.size(57.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.robot),
                        contentDescription = "Bot Picture Holder",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(20.dp)
                            .height(35.dp)
                            .padding(5.dp, 0.dp, 0.dp, 0.dp),
                        colorFilter = ColorFilter.tint(color = Color(0xFF7630F2))
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(65.dp, 0.dp)
                    .size(240.dp, 50.dp),
                verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Selfbest Bot",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF),
                )
                Text(
                    text = "This is the last Message",
                    color = Color(0xFFFFFFFF),
                    fontStyle = FontStyle.Italic
                )
            }

        }

        Box(modifier = Modifier.padding(10.dp, 0.dp,5.dp,0.dp)){
            Card(shape = CircleShape, backgroundColor = Color(0xFFFFFFFF),
                modifier = Modifier.size(57.dp)) {
                IconButton(onClick = {
                    Toast.makeText(context, "Clicked on Search bar", Toast.LENGTH_LONG).show()} ) {
                    Icon(
                        Icons.TwoTone.Search,
                        contentDescription = "content description",
                        tint = Color(0XFFC8C8C8),
                        modifier = Modifier.padding(15.dp)
                    )

                }
            }
        }

    }

}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun NewUserCard(){
    Row(modifier = Modifier.padding(15.dp,10.dp, 15.dp, 10.dp)){
        Card(shape = CircleShape, backgroundColor = Color(0xFFF8F8F8), modifier = Modifier.size(45.dp)) {

        }
        Column(
            modifier = Modifier
                .padding(5.dp, 0.dp, 0.dp, 0.dp)
                .width(240.dp),
            verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
        ) {
            Text(
                text = "Python",
                fontWeight = FontWeight.Normal,
                //   color = selfBestDefaultColor
                fontSize = TextUnit(14f, TextUnitType.Sp)
            )
            Spacer(modifier = Modifier.size(7.dp))
            Text(
                text = "You : I have a query...",
                //color = selfBestDefaultColor,
                fontStyle = FontStyle.Normal,
                fontSize = TextUnit(12f, TextUnitType.Sp)
            )


        }

        Column ( modifier = Modifier
            .padding(5.dp, 0.dp, 0.dp, 0.dp)){
            Text(
                text = "18/11/2022",
                fontWeight = FontWeight.Normal,
                // color = selfBestDefaultColor,
                fontSize = TextUnit(12f, TextUnitType.Sp)

            )
        }


    }
}

@Composable
private fun MyIconBox() {

    val iconSize = 12.dp
    val offsetInPx = LocalDensity.current.run { (iconSize / 2).roundToPx() }

    Box(modifier = Modifier.padding((iconSize / 2))) {

        Card(shape = RoundedCornerShape(2.dp)) {
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = R.drawable.iconrobot),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }

        IconButton(
            onClick = {},
            modifier = Modifier
                .offset {
                    IntOffset(x = +offsetInPx, y = -offsetInPx)
                }
                .clip(CircleShape)
                .background(White)
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
fun DefaultPreview() {
    Surface(Modifier.fillMaxSize()) {
        Column {
            //BotCard()
//           NewBotCardDesign()
//           //NewUserCard()
//           UserList()
            MyIconBox()
        }
    }
}



