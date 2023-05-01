package com.example.chat_feature.screens.chat.components

import android.widget.HorizontalScrollView
import android.widget.ScrollView
import android.widget.Scroller
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/*@Composable
fun DropdownExample(
    options:List<String>,
    selectedOption: (title: String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var Option by remember { mutableStateOf(options[0]) }





    Column {
        // Text(text = "Selected option: $selectedOption")
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 10.dp)
                .wrapContentHeight()
        ) {
            TextButton(
                
                onClick = { expanded = true }
            ){
                Text(text = "Choose skill")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.8f)
                    .padding(horizontal = 10.dp)
                    .wrapContentHeight()
            ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.height(300.dp)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                Option = option
                                expanded = false

                                selectedOption.invoke(option)
                            },
                        ) {
                            Text(option)
                        }
                    }
                }
            }
        }
    }
}*/

/*------------------this is working perfect----------------------*/
/*@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DropdownExample(
    options: List<String>,
    isDropDownEnabled:Boolean=true,
    selectedOption: (title: String) -> Unit
) {
    val pageSize = 50 // Set the number of items to load per page
    var currentPage = 0
    var allOptions = remember { mutableStateOf(options) }
    var displayedOptions by remember { mutableStateOf(allOptions.value.take(pageSize)) }
    var expanded by remember { mutableStateOf(false) }
    var scrollState = rememberScrollState()
    var isSelected by rememberSaveable { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 10.dp)
                .wrapContentHeight()
        ) {
            TextButton(
                modifier = Modifier
                    .padding(1.dp)
                    .defaultMinSize(minWidth = 90.dp),
                onClick = {
                    expanded=true
                    isSelected=true

                },
                shape = RoundedCornerShape(19),
                colors =
                ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black,
                    backgroundColor = Color.LightGray.copy(alpha = 0.6f),
                    disabledContentColor = Color.Black.copy(alpha = 0.8f),
                ),
                enabled = isDropDownEnabled
            ) {
                Text(text = "Choose skill", modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.caption)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.8f)
                    .padding(horizontal = 10.dp)
                    .fillMaxHeight()
                    .imeNestedScroll()
            ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.height(300.dp)
                ) {

                    Column(modifier = Modifier.fillMaxHeight()) {

                        displayedOptions.forEach { option ->
                            DropdownMenuItem(
                                onClick = {
                                    val selectedOptions = option
                                    expanded = false
                                    selectedOption.invoke(option)
                                },
                                *//*  modifier = Modifier.scrollable(
                                      orientation = Orientation.Vertical,
                                      state= rememberScrollState(0),
                                      enabled = true,
                                      reverseDirection = true,
                                  )*//*
                            ) {
                                Text(option,style = MaterialTheme.typography.caption,
                                    color = Color(0xFF3E3E3E))
                            }
                        }
                    }


                    val remainingOptions = allOptions.value.drop(displayedOptions.size)
                    if (remainingOptions.isNotEmpty()) {
                        Divider()
                        DropdownMenuItem(
                            onClick = {

                                currentPage++
                                val startIndex = currentPage * pageSize
                                *//* val endIndex = startIndex + pageSize*//*
                                val endIndex = minOf(startIndex + pageSize, allOptions.value.size)
                                displayedOptions = allOptions.value.slice(startIndex until endIndex)
                                CoroutineScope(Dispatchers.Default).launch {
                                    scrollState.scrollTo(0)
                                }
                                ScrollState(0)
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .background(color = Color(0xFF1D71D4))
                        ) {
                            Text(text = "Load more options",style = MaterialTheme.typography.caption,
                                color = Color(0xFFFFFFFF))
                        }
                    }
                    LaunchedEffect(Unit) {
                        // Scroll to top after recomposing with new options
                        scrollState.animateScrollTo(0)
                    }
                    Box(
                        modifier = Modifier.width(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                       // PageIndicator(currentPage, allOptions.value.size, Modifier.padding(8.dp))
                    }
                }
            }
        }
    }

}*/


/*---------------------------code is working fine for dialog box case--------------------*/
/*@Composable
fun DialogForSkill(
    options: List<String>,
    isDropDownEnabled:Boolean=true,
    selectedOption: (title: String) -> Unit,
) {
    val pageSize = 50 // Set the number of items to load per page
    var currentPage = 0
    var allOptions = remember { mutableStateOf(options) }
    var displayedOptions by remember { mutableStateOf(allOptions.value.take(pageSize)) }
    var showDialog by remember { mutableStateOf(false) }
    var isSelected by rememberSaveable { mutableStateOf(false) }

    Column {
        Box ( modifier = Modifier
            .width(200.dp)
            .padding(horizontal = 10.dp)
            .wrapContentHeight()){
            TextButton(
                onClick = { showDialog = true
                          isSelected=true},
                shape = RoundedCornerShape(19),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.LightGray.copy(alpha = 0.6f),
                    contentColor = Color.Black,
                    disabledContentColor = Color.Black.copy(alpha = 0.8f),
                    disabledBackgroundColor =Color.LightGray.copy(alpha = 0.6f)
                ),
                enabled = isDropDownEnabled
            ) {
                Text(
                    text = "Choose skill",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(2.dp),
                )
            }
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = {showDialog=false},
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.width(280.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Choose skill",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyColumn {
                            items(displayedOptions) { option ->
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                        .clickable(
                                            onClick = {
                                                val selectedOptions = option
                                                selectedOption.invoke(option)
                                                showDialog = false
                                            }
                                        )
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.caption,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        ),
                                        color = Color.Black
                                    )
                                    Divider(
                                        color = Color(0xFFE2E2E2),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(15.dp, 0.dp, 15.dp, 0.dp)
                                    )
                                }
                            }

                            val remainingOptions = allOptions.value.drop(displayedOptions.size)
                            if (remainingOptions.isNotEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .clickable(
                                                onClick = {
                                                    currentPage++
                                                    val startIndex = currentPage * pageSize
                                                    val endIndex = startIndex + pageSize
                                                    displayedOptions =
                                                        allOptions.value.slice(startIndex until endIndex)
                                                }
                                            )
                                    ) {
                                        Text(
                                            text = "Load more options",
                                            style = MaterialTheme.typography.caption,
                                            color = Color(0xFF1D71D4)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}*/

/*@Composable
fun DialogExample(
    options: List<String>,
    selectedOption: (title: String) -> Unit,
) {
    val pageSize = 50 // Set the number of items to load per page
    var currentPage by remember { mutableStateOf(0) }
    var allOptions by remember { mutableStateOf(options) }
    var displayedOptions by remember { mutableStateOf(allOptions.take(pageSize)) }
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .padding(1.dp)
                .defaultMinSize(minWidth = 90.dp),
            shape = RoundedCornerShape(19),
            colors =
            ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black,
                backgroundColor = Color.LightGray.copy(alpha = 0.6f),
                disabledContentColor = Color.Black.copy(alpha = 0.8f),
            ),

        ) {
            Text(
                text = "Choose skill",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(2.dp),
            )
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.width(280.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Choose skill",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        val lazyListState = rememberLazyListState()

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp)
                                ,
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            itemsIndexed(displayedOptions) { index, option ->
                                Box(
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                selectedOption.invoke(option)
                                                showDialog = false
                                            }
                                        )
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.caption,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                        ),
                                        color = Color.Black
                                    )
                                }
                                if (index == displayedOptions.lastIndex && allOptions.size > displayedOptions.size) {
                                    this@LazyColumn.item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable(
                                                    onClick = {
                                                        currentPage++
                                                        val startIndex = currentPage * pageSize
                                                        val endIndex = startIndex + pageSize
                                                        displayedOptions =
                                                            allOptions.slice(startIndex until endIndex)

                                                    }
                                                )
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Load more options",
                                                style = MaterialTheme.typography.caption,
                                                color = Color(0xFF1D71D4),
                                            )
                                        }
                                    }
                                }
                            }
                        }


                    }
                }
            }
        }
    }
}*/

/*---------------------shows complete list skills on single page------------------------*/
/*@Composable
fun DialogForSkill(
    options: List<String>,
    isDropDownEnabled:Boolean=true,
    selectedOption: (title: String) -> Unit,
) {
    var allOptions = remember { mutableStateOf(options) }
    var displayedOptions by remember { mutableStateOf(allOptions.value) }
    var showDialog by remember { mutableStateOf(false) }
    var isSelected by rememberSaveable { mutableStateOf(false) }

    Column {
        Box ( modifier = Modifier
            .width(200.dp)
            .padding(horizontal = 10.dp)
            .wrapContentHeight()){
            TextButton(
                onClick = { showDialog = true
                    isSelected=true},
                shape = RoundedCornerShape(19),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF1D71D4),
                    contentColor = Color.White,
                    disabledContentColor = Color.Black.copy(alpha = 0.8f),
                    disabledBackgroundColor =Color.LightGray.copy(alpha = 0.6f)
                ),
                enabled = isDropDownEnabled
            ) {
                Text(
                    text = "Choose skill",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(2.dp),
                )
            }
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = {showDialog=false},
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.width(280.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Choose skill",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = Color.Black
                        )

                        LazyColumn {
                            items(displayedOptions) { option ->
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                        .clickable(
                                            onClick = {
                                                val selectedOptions = option
                                                selectedOption.invoke(option)
                                                showDialog = false
                                            }
                                        )
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.subtitle2,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        ),
                                        //color = Color.Black
                                    )
                                    Divider(
                                        color = Color(0xFFE2E2E2),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(15.dp, 0.dp, 15.dp, 0.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}*/

/*---------------------shows complete list skills on single page with 50 enteries at a time------------------------*/
@Composable
fun DialogForSkill(
    options: List<String>,
    isDropDownEnabled: Boolean = true,
    selectedOption: (title: String) -> Unit,
) {
    val pageSize = 50 // Set the number of items to load per page
    var currentPage = 0
    var allOptions = remember { mutableStateOf(options) }
    var displayedOptions by remember { mutableStateOf(allOptions.value.take(pageSize)) }
    var showDialog by remember { mutableStateOf(false) }
    var isSelected by rememberSaveable { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 10.dp)
                .wrapContentHeight()
        ) {
            TextButton(
                onClick = {
                    showDialog = true
                    isSelected = true
                },
                shape = RoundedCornerShape(19),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF1D71D4),
                    contentColor = Color.White,
                    disabledContentColor = Color.Black.copy(alpha = 0.8f),
                    disabledBackgroundColor = Color.LightGray.copy(alpha = 0.6f)
                ),
                enabled = isDropDownEnabled
            ) {
                Text(
                    text = "Choose skill",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(2.dp),
                )
            }
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.width(280.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 450.dp)
                            //.padding(8.dp)
                    ) {
                        Text(
                            text = "Choose skill",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                //.align(Alignment.CenterHorizontally)
                                .background(color = Color(0xFF1D71D4))
                                .fillMaxWidth().height(30.dp).padding(0.dp,4.dp,0.dp,0.dp),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold

                        )

                        LazyColumn {
                            items(displayedOptions) { option ->
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 0.dp)
                                        .clickable(
                                            onClick = {
                                                val selectedOptions = option
                                                selectedOption.invoke(option)
                                                showDialog = false
                                            }
                                        )
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.caption,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        ),
                                        color = Color.DarkGray
                                    )

                                }
                                Divider(
                                    color = Color(0xFFE2E2E2),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(7.dp, 0.dp, 7.dp, 0.dp)
                                )
                            }

                            val remainingOptions = allOptions.value.drop(displayedOptions.size)
                            if (remainingOptions.isNotEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .clickable(
                                                onClick = {
                                                    currentPage++
                                                    val startIndex = currentPage * pageSize
                                                    val endIndex = minOf(
                                                        startIndex + pageSize,
                                                        allOptions.value.size
                                                    )
                                                    displayedOptions += allOptions.value.slice(
                                                        startIndex until endIndex
                                                    )
                                                }
                                            )
                                    ) {
                                        Text(
                                            text = "More options",
                                            style = MaterialTheme.typography.caption,
                                            color = Color(0xFF1D71D4),
                                            modifier = Modifier.padding(8.dp, 10.dp, 0.dp, 0.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
/*
@Composable
fun DialogForSkill(
    options: List<String>,
    isDropDownEnabled: Boolean = true,
    selectedOption: (title: String) -> Unit,
) {
    val pageSize = 50 // Set the number of items to load per page
    val searchQuery = remember { mutableStateOf("") }
    val filteredOptions = remember {
        derivedStateOf {
            options.filter { it.contains(searchQuery.value, ignoreCase = true) }
        }
    }
    var showDialog by remember { mutableStateOf(false) }
    var isSelected by rememberSaveable { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 10.dp)
                .wrapContentHeight()
        ) {
            TextButton(
                onClick = {
                    showDialog = true
                    isSelected = true
                },
                shape = RoundedCornerShape(19),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF1D71D4),
                    contentColor = Color.White,
                    disabledContentColor = Color.Black.copy(alpha = 0.8f),
                    disabledBackgroundColor = Color.LightGray.copy(alpha = 0.6f)
                ),
                enabled = isDropDownEnabled
            ) {
                Text(
                    text = if (searchQuery.value.isNotEmpty()) searchQuery.value else "Choose skill",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(2.dp),
                )
            }
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.width(280.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 450.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery.value,
                            onValueChange = { searchQuery.value = it },
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            placeholder = { Text("Search") }
                        )
                        Divider(color = Color.Gray.copy(alpha = 0.2f))
                        LazyColumn {
                            items(filteredOptions.value) { option ->
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 0.dp)
                                        .clickable(
                                            onClick = {
                                                selectedOption.invoke(option)
                                                showDialog = false
                                            }
                                        )
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.caption,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        ),
                                        color = Color.DarkGray
                                    )
                                }
                                Divider(
                                    color = Color(0xFFE2E2E2),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(7.dp, 0.dp, 7.dp, 0.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
*/
























