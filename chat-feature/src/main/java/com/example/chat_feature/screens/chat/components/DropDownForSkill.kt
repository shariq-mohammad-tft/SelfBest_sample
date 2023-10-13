package com.example.chat_feature.screens.chat.components

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chat_feature.screens.chat.SearchAppBar


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
/*@Composable
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
}*/




















@Composable
fun DialogForSkill(
    options: List<String>,
    isDropDownEnabled: Boolean,
    selectedOptions: (titles: List<String>) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf(emptyList<String>()) }
    val pageSize = 10 // Set the number of items to load per page
    var currentPage by remember { mutableStateOf(0) }
    var displayedOptions by remember {
        mutableStateOf(
            options.take(pageSize)
        )
    }
    val searchQuery = remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }


    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf("") }
    if (showDialog) {

        AlertDialog(
            onDismissRequest = {
                showDialog = false
                searchQuery.value = ""
                displayedOptions = options.take(pageSize)
            },
            shape = RoundedCornerShape(5),
            title = {
                Box (contentAlignment = Alignment.Center){
                    Text(
                        text = "",
                        color = Color(0xFF1D71D4),
                        textAlign = TextAlign.Center)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp),
                    //.verticalScroll(rememberLazyListState())
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 1.dp,
                                    color = colorResource(id = if (txtFieldError.value.isEmpty()) R.color.darker_gray else R.color.darker_gray)
                                ),
                                shape = RoundedCornerShape(5)
                            )
                            .defaultMinSize(minHeight = 40.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),

                        placeholder = { Text(text = "Search Skills", color = Color.Gray) },
                        value = searchQuery.value,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        onValueChange = {
                            searchQuery.value = it
                            currentPage = 0
                            displayedOptions = options
                                .filter { option ->
                                    option.contains(
                                        searchQuery.value,
                                        ignoreCase = true
                                    )
                                }
                                .take(pageSize)
                            isSearchActive = it.isNotEmpty()
                        },
                        maxLines = 1,
                        singleLine = true
                    )

                    LazyColumn {
                        items(displayedOptions) { option ->
                            val isSelected = selectedItems.contains(option)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        selectedItems = if (isSelected) {
                                            selectedItems - option
                                        } else {
                                            selectedItems + option
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF1D71D4),
                                        uncheckedColor = Color(0xFF1D71D4),
                                        checkmarkColor = Color.White
                                    )

                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = option)
                            }
                        }
                        if (options.size > displayedOptions.size) {
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
                                                    options.size
                                                )
                                                displayedOptions += options.subList(
                                                    startIndex,
                                                    endIndex
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
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedOptions(selectedItems)
                        showDialog = false
                    },
                    shape = RoundedCornerShape(19),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF1D71D4),
                        contentColor = Color.White,
                        disabledContentColor = Color.Black.copy(alpha = 0.8f),
                        disabledBackgroundColor = Color.LightGray.copy(alpha = 0.6f)
                    ),
                    enabled = !selectedItems.isEmpty()
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    },
                    shape = RoundedCornerShape(19),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF1D71D4),
                        contentColor = Color.White,
                        disabledContentColor = Color.Black.copy(alpha = 0.8f),
                        disabledBackgroundColor = Color.LightGray.copy(alpha = 0.6f)
                    ),
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    Column {
        if (isDropDownEnabled) {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .padding(horizontal = 10.dp)
                    .wrapContentHeight()
            ) {
                TextButton(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(19),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF1D71D4),
                        contentColor = Color.White,
                        disabledContentColor = Color.Black.copy(alpha = 0.8f),
                        disabledBackgroundColor = Color.LightGray.copy(alpha = 0.6f)
                    ),
                ) {
                    Text(
                        text = "Choose skill",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(2.dp),
                    )
                }
            }
        }
    }
}





