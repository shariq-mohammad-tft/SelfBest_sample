package com.example.chat_feature.screens.chat

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.chat_feature.data.*
import com.example.chat_feature.navigation.AppScreen
import com.example.chat_feature.navigation.ROUTE_ROOM
import com.example.chat_feature.screens.chat.components.*
import com.example.chat_feature.utils.*
import com.example.chat_feature.view_models.ChatViewModel
import com.example.chat_feature.view_models.ExpertChatViewModel
import com.example.chat_feature.view_models.ExpertListViewModel
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "ChatScreen"

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class, ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    navController: NavController,
    senderId: String,
    receiverId: String,
    viewModel: ChatViewModel = hiltViewModel(),
    expertListViewModel: ExpertListViewModel = hiltViewModel(),
    expertViewModel: ExpertChatViewModel = hiltViewModel()
) {

    val messages = viewModel.messageList
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userId = viewModel.userId

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        Log.d(TAG, "ChatScreen: $uri")

        uri?.let {
            Log.d(TAG, "ChatScreen: FILE PATH - ${context.getFileFromUri(uri)?.absolutePath}")
            val file = context.getFileFromUri(uri)
            viewModel.updateUri(file?.absolutePath)
        }

    }



    Log.d(
        TAG, "ChatScreen: SENDER - $userId and RECEIVER - $receiverId "
    )

    var botMessageCount by rememberSaveable { mutableStateOf(0) }
    var unseenMessageCount by rememberSaveable { mutableStateOf(0) }


    /*LaunchedEffect(Unit) {
        viewModel.connectSocket(socketUrl = Constants.SELF_BEST_SOCKET_URL.createSocketUrl(userId))
    }*/

    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                viewModel.seenBotMessage()

            }
            Lifecycle.Event.ON_RESUME -> viewModel.connectSocket(
                Constants.SELF_BEST_SOCKET_URL.createSocketUrl(
                    userId
                )
            )
            Lifecycle.Event.ON_STOP -> {
                viewModel.closeConnection()
                viewModel.seenBotMessage()
                expertListViewModel.botMessageCount = 0
                expertListViewModel.unseenMessageCount = 0
                Log.d("onStopCalled", expertListViewModel.unseenMessageCount.toString())
            }
            else -> Unit
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Selfbest Bot",
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.h6

                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(
                    0xFFF8F8F8
                )
            ), navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                    )
                }
            }, modifier = Modifier.shadow(
                10.dp,
                shape = RoundedCornerShape(0.dp).copy(
                    bottomStart = CornerSize(15.dp), bottomEnd = CornerSize(15.dp)
                ),

                )
        )
    }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f), state = listState
            ) {

                scope.launch {
                    // todo
                    // scroll rules
                    // automatically scroll down when new message arrives but,
                    // when user manually scroll up then prevent scroll down (so detect scroll up)
                    if (messages.size > 6 && !listState.isScrollInProgress) {
                        listState.scrollToItem(index = messages.lastIndex)
                    }
                }
                items(messages) { message ->

                    when (message) {
                        is Resource.Failure -> {
                            Log.i(TAG, "ChatScreen: $message")
                            if (message.errorCode == 500) {
                                //ErrorMessage("Server is down please try after sometime")
                                CardErrorMessage(message = "Server is down please try after sometime <br>or try to type 'Reset' in chat box")
                            }
                        }

                        is Resource.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is Resource.Success -> {
                            // todo
                            // scroll rules
                            // automatically scroll down when new message arrives but,
                            // when user manually scroll up then prevent scroll down (so detect scroll up)
                            /* LaunchedEffect(messages.size > 6 && !listState.isScrollInProgress) {
                                 listState.scrollToItem(index = messages.lastIndex)
                             }*/

                            if (!message.value.channelId.isNullOrEmpty()) {
                                LaunchedEffect(Unit) {
                                    context.toast("Connected")

                                    // todo debug and check whether back press works

                                    navController.navigate(
                                        AppScreen.ExpertChat.buildRoute(
                                            senderId = userId,
                                            receiverId = message.value.channelId,
                                            queryId = message.value.queryId!!,
                                        )
                                    ) {
                                        Log.d(
                                            "queryIdd",
                                            message.value.queryId + userId + message.value.channelId
                                        )
                                        popUpTo(ROUTE_ROOM) {
                                            inclusive = false
                                        }
                                    }.also { viewModel.closeConnection() }
                                }


                            } else {
                                MessageCard(
                                    message = message.value,
                                    senderId = userId,
                                    navController = navController
                                ) { messageRequest ->
                                    viewModel.sendMessageToServer(messageRequest)
                                }
                            }


                        }
                        else -> {}
                    }

                }
            }

            if (viewModel.imageUri != null) {

                //viewModel.uploadImage(file)
                ChatBoxEditTextWithImage(message = viewModel.message, onChange = {
                    viewModel.updateMessage(it)
                }, imageUri = viewModel.imageUri, onSend = {

                    val messageObj = ImgShareOnBotRequest(
                        file = File(viewModel.imageUri),
                        event_type = "message",
                        sender_id = userId,
                        event_message = viewModel.message.normalText()
                    )
                    Log.d("messageObjForBot", messageObj.toString())

                    viewModel.sendMessageToServer(messageObj)
                    viewModel.updateMessage("")
                    viewModel.updateUri(null)

                    scope.launch {
                        if (messages.isNotEmpty()) listState.scrollToItem(index = messages.lastIndex)
                    }

                }, onImageIconClicked = {
                    launcher.launch("image/*")
                },

                    onImageOpen = {
                        scope.launch {

                            viewModel.imageUri?.let {
                                navController.navigate(
                                    AppScreen.PhotoPreview.buildRoute(
                                        imageUri = it
                                    )
                                )
                            }
                        }


                    }, onImageClose = {
                        viewModel.updateUri(null)
                    })
            } else {
                ChatBoxEditText(
                    onImageIconClicked = {
                        Log.d("imageSelected", "")
                        launcher.launch("image/*")
                    },
                    message = viewModel.message,

                    isImgButtonEnable = viewModel.isImageBoxEnabled,
                    onChange = { viewModel.updateMessage(it) },
                    onSend = {
                        if (it.normalText().isEmpty()) {
                            context.toast("Please Enter Message!")
                            return@ChatBoxEditText
                        }

                        val messageObj = buildPlainMessage(message = it, userId)

                        viewModel.sendMessageToServer(messageObj)
                        viewModel.updateMessage("")

                        scope.launch {
                            if (messages.isNotEmpty()) listState.scrollToItem(index = messages.lastIndex)
                        }

                    },
                )
            }


        }
    }
}


/*---------------------------------- Build Messages ----------------------------------------*/

private fun buildPlainMessage(message: String, userId: String): PlainMessageRequest {
    return PlainMessageRequest(
        senderId = userId,
        message = message,
    )
}

private fun buildInteractiveMessage(
    message: String, eventName: String, userId: String
): InteractiveMessageRequest {
    return InteractiveMessageRequest(
        senderId = userId, message = message, eventName = eventName
    )
}


/*--------------------------------------- Message Card ----------------------------------------*/

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageCard(
    navController: NavController,
    message: Message? = null,
    senderId: String? = null,
    viewModel: ChatViewModel = hiltViewModel(),
    onSend: (messageRequest: MessageRequest) -> Unit,


    ) {

    val options = listOf(
        "adobe systems adobe acrobat",
        "adobe systems adobe after effects",
        "adobe systems adobe creative cloud software",
        "adobe systems adobe illustrator",
        "adobe systems adobe indesign",
        "adobe systems adobe photoshop",
        "ajax",
        "alteryx software",
        "amazon dynamodb",
        "amazon elastic compute cloud ec2",
        "amazon redshift",
        "amazon simple storage service s3",
        "amazon web services aws cloudformation",
        "amazon web services aws software",
        "ansible software",
        "apache cassandra",
        "apache hadoop",
        "apache hive",
        "apache kafka",
        "apache maven",
        "apache spark",
        "apache subversion svn",
        "apache tomcat",
        "apple ios",
        "atlassian bamboo",
        "atlassian bitbucket",
        "atlassian confluence",
        "autodesk autocad",
        "autodesk autocad civil 3d",
        "autodesk revit",
        "bash",
        "bentley microstation",
        "bootstrap",
        "c",
        "c#",
        "c++",
        "chef",
        "cisco webex",
        "dassault systemes solidworks",
        "django",
        "docker",
        "drupal",
        "eclinicalworks ehr software",
        "elasticsearch",
        "epic systems",
        "esri arcgis software",
        "facebook",
        "figma",
        "gitlab",
        "go",
        "google angular",
        "google cloud software",
        "google docs",
        "google sheets",
        "google workspace software",
        "graphql",
        "hibernate orm",
        "hubspot software",
        "ibm db2",
        "ibm spss statistics",
        "ibm terraform",
        "ibm websphere",
        "informatica software",
        "intuit quickbooks",
        "jenkins ci",
        "jquery",
        "junit",
        "kronos workforce timekeeper",
        "kubernetes",
        "linkedin",
        "linux",
        "marketo marketing automation",
        "microsoft .net framework",
        "microsoft access",
        "microsoft active directory",
        "microsoft active server pages asp",
        "microsoft asp.net",
        "microsoft azure software",
        "microsoft dynamics",
        "microsoft excel",
        "microsoft office software",
        "microsoft outlook",
        "microsoft powerpoint",
        "microsoft powershell",
        "microsoft project",
        "microsoft sharepoint",
        "microsoft sql server reporting services ssrs",
        "microsoft team foundation server",
        "microsoft teams",
        "microsoft visio",
        "microsoft visual basic",
        "microsoft visual basic for applications vba",
        "microsoft windows server",
        "microsoft word",
        "microstrategy",
        "mongodb",
        "node.js",
        "nosql",
        "oracle database",
        "oracle java",
        "oracle java 2 platform enterprise edition j2ee",
        "oracle javaserver pages jsp",
        "oracle pl/sql",
        "oracle primavera enterprise project portfolio management",
        "oracle sql developer",
        "perl",
        "php",
        "puppet",
        "python",
        "qlik tech qlikview",
        "r",
        "red hat openshift",
        "redis",
        "ruby",
        "ruby on rails",
        "sap software",
        "sas",
        "scala",
        "selenium",
        "servicenow",
        "shell script",
        "slack",
        "splunk enterprise",
        "spring boot",
        "spring framework",
        "swift",
        "tableau",
        "teradata database",
        "the mathworks matlab",
        "transact-sql",
        "trimble sketchup pro",
        "typescript",
        "unix",
        "unix shell",
        "vue.js",
        "wordpress",
        "workday software",
        "yardi software",
        "zoom",
        ".net",
        "android",
        "angular",
        "ble bluetooth low energy",
        "browserstack",
        "codeigniter",
        "cross browser testing",
        "css cascading style sheets",
        "cypress",
        "deep learning",
        "devops",
        "eclipse ide",
        "express.js",
        "fastapi",
        "ghcdatascience",
        "ghcgeneral",
        "ghchardware",
        "ghcproduct",
        "ghcproduct",
        "ghcsoftware",
        "git/github",
        "gtmetrix",
        "html hypertext markup language",
        "http hypertext transfer protocol",
        "influxdb",
        "intellij idea",
        "iot internet of things",
        "javascript",
        "jira atlassian",
        "jmeter",
        "json javascript object notation",
        "knockout.js",
        "kubernetes",
        "loadrunner",
        "lora",
        "mac",
        "mantishub",
        "manual testing/android/ios/web",
        "ms sql",
        "netsparker/invicti",
        "new relic",
        "nlp natural language processing",
        "nltk natural language toolkit",
        "objective-c",
        "oops",
        "oracle jdbc",
        "phpstorm",
        "pivotal tracker",
        "postgres",
        "postman/api testing",
        "puppeteer",
        "pytest",
        "qtest",
        "qtp quicktest professional",
        "react native",
        "reactjs",
        "rest api",
        "restclient",
        "restful",
        "rpa robotic process automation",
        "sauce labs",
        "scipy",
        "soapui",
        "sql structured query language",
        "sqlite",
        "testrail",
        "tfs team foundation server",
        "uat user acceptance testing",
        "visual studio",
        "wave tool",
        "webstorm",
        "windows",
        "wordpress",
        "x-ray",
        "xcode ide",
        "xml extensible markup language",
        "gitlab",
        "python",
        "amazon redshift",
        "Nagios",
        "cypress",
        "swagger",
        "pivotal tracker",
        "adobe systems adobe photoshop",
        "jquery",
        "PyCharm",
        "Talend API Tester",
        "Azure Sphere",
        "CPQ Salesforce",
        "webstorm",
        "intellij idea",
        "Fastlane",
        "Fog Computing",
        "microsoft sql server reporting services ssrs",
        "Amazon Web Services AWS",
        "Search Engine Marketing (SEM)",
        "gRPC",
        "nlp natural language processing",
        "System Center Operations Manager (SCOM)",
        "Trello",
        "Agile methodology",
        "typescript",
        "Google Analytics",
        "jira atlassian",
        "Agile Methodology",
        "Spring Framework",
        "rpa robotic process automation",
        "flask",
        "Monday.com",
        "SQL",
        "Recruitment",
        "embedded",
        "TestNG",
        "xcode ide",
        "Adobe Target",
        "azure iot hub",
        "Kotlin",
        "angular",
        "jmeter",
        "LangChain",
        "Wifi",
        "Adobe XD",
        "LoraWAN",
        "arduino",
        "Web",
        "css cascading style sheets",
        "Appium",
        "QGIS",
        "ms sql",
        "Manual Testing",
        "sauce labs",
        "soapui",
        "oops",
        "Test Rail",
        "Search Engine Optimization (SEO)",
        "API Automation",
        "prometheus",
        "thingsboard",
        "Automation Testing in ROR with BDD",
        "SharePoint",
        "Maven",
        "Selenium (Mobile)",
        "Exploratory Data Analysis (EDA)",
        "CorelDRAW",
        "Zapier",
        "Flask",
        "REST",
        "postgres",
        "trello",
        "lokinsrjenrmkene",
        "nodered",
        "mantishub",
        "cross browser testing",
        "netsparker/invicti",
        "Playwright",
        "core java",
        "Manual testing",
        "azure iot central",
        "mongodb",
        "VMware",
        "apache cassandra",
        "windows",
        "Maestro",
        "Keras",
        "apache maven",
        "Time series forecasting",
        "Grafana",
        "express.js",
        ".NET MVC",
        "Appian",
        "node.js",
        "Flutter",
        "SQlite3",
        "Manual QA",
        "Figma",
        "android",
        "Nginx",
        "BLE",
        "UART",
        "Metasploit",
        "html hypertext markup language",
        "amazon web services aws software",
        "raspberry pi",
        "bentley microstation",
        "sqlite",
        ".net",
        "WCF",
        "Java",
        "linux",
        "reactjs",
        "Redis",
        "Tkinter",
        "Cisco Certified Network Associate(CCNA)",
        "AWS Glue",
        "Tailwind CSS",
        "Manual Testing ",
        "STAAD Pro",
        "selfbest",
        "Celery ",
        "x-ray",
        "flutter",
        "MVC (Model-View-Controller)",
        "Mobile app testing",
        "tfs team foundation server",
        "Embedded",
        "replaced",
        "amazon web services aws cloudformation",
        "Postman",
        "atlassian confluence",
        "grafana",
        "Selenium",
        "Semgrep",
        "Sublime Text",
        "browserstack",
        "ansible software",
        "visual studio",
        "apple ios",
        "wave tool",
        "Azure IoT Central",
        "unix",
        "aws",
        "backend development",
        "google cloud software",
        "Arduino",
        "restclient",
        "puppet",
        "Bugzilla",
        "sql structured query language",
        "react native",
        "Wordpress",
        "esp32",
        "postman/api testing",
        "OWASP",
        "Penetration test",
        "rest api",
        "bootstrap",
        "express",
        "Salesforce",
        "django",
        "Oracle Cloud",
        "MySQL",
        "testrail",
        "javascript",
        "Device Testing",
        "MariaDB",
        "Windows Presentation Foundation",
        "puppeteer",
        "Power BI",
        "Automation in Ruby on Rails withBDD",
        "Nest.js",
        "Yii",
        "chai",
        "microsoft azure software",
        "Web API",
        "redis",
        "http hypertext transfer protocol",
        "Wireshark",
        "fastapi",
        "objective-c",
        "google angular",
        "ruby on rails",
        "microsoft dynamics",
        "docker",
        "OpenAI",
        "Edge Computing",
        "Typeform",
        "API Tetsing",
        "Human Resources (HR)",
        "microsoft asp.net",
        "Socket.IO",
        "Nessus",
        "Cypress",
        "c",
        "jenkins ci",
        "example",
        "Internet of Things",
        "Alexa",
        "rest",
        "oracle database",
        "Load Testing",
        "adobe systems adobe illustrator",
        "Fiddler",
        "Blockchain Technology",
        "Machine Learning",
        "API Testing",
        "kubernetes",
        "Charles Proxy",
        "OutSystems",
        "Firebase",
        "eclipse ide",
        "Travis CI",
        "git/github",
        "Laravel",
        "microsoft office software",
        "CSS",
        "Raspberry Pi",
        "selenium",
        "Jenkins",
        "frontend development",
        "Swagger",
        "phpstorm",
        "mac",
        "ibm terraform",
        "JMeter",
        "Automation Testing",
        "oracle pl/sql",
        "Unity 2D",
        "Quicksight",
        "Adobe Premiere Pro",
        "Asana",
        "Cucumber",
        "Gherkin",
        "MQTT",
        "BlazeMeter",
        "oracle java",
        "manual testing/android/ios/web",
        "Mobile testing"
    )
    var filteredList by remember { mutableStateOf(options) }
    if (message?.wrongskill.isNullOrEmpty().not()) {
        val wSkill = message?.wrongskill
        filteredList = options.filter { it.contains(wSkill!!, ignoreCase = true) }
    }


    message!!   // To Avoid preview conflict
    senderId!!   // To Avoid preview conflict

    var isEnabled by rememberSaveable { mutableStateOf(message.isButtonEnabled) }
    var isImgButtonEnabled by rememberSaveable { mutableStateOf(message.isImgButtonEnabled) }
    var isDropDownEnabled by rememberSaveable { mutableStateOf(message.isDropDownEnabled) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {

        if (!message.links.isNullOrEmpty()) {
            CardlinksMessage(message = message.links!!)
        }


        if (message.senderId == senderId) {
            if ((message.eventMessage != null) && (message.file == null)) {
                CardSelfMessage(
                    message = message.eventMessage,
                    timestamp = message.timeStamp ?: getCurrentTime()
                )
            } else if (message.file != null) {
                if (message.eventMessage != null) {
                    PhotoSenderCardForBot(
                        imageLink = message.file,
                        message = message.eventMessage!!,
                        navController = navController
                    )
                } else {
                    PhotoSenderCardForBot(
                        imageLink = message.file,
                        message = message.message,
                        navController = navController
                    )
                }
                Log.d("PhotoSenderCardForBots", message.file)

                //PhotoCardForBot(imageLink = message.file)
            } else {
                CardSelfMessage(
                    message = message.message, timestamp = message.timeStamp ?: getCurrentTime()
                )
            }

        } else {
            if ((message.message != "")) {
                CardReceiverMessage(
                    message = message.message,
                    timestamp = message.timeStamp ?: getCurrentTime()
                )
            }
            else {
                CardReceiverMessage(
                    message = "Here is something I found",
                    timestamp = message.timeStamp ?: getCurrentTime()
                )
            }

        }


        if (message.message == "Please select a valid skill from this dropdown") {
            DialogForSkill(options, isDropDownEnabled) {
                Log.d("selectedOptionTitle", "$it")
                val messagess = buildPlainMessage(message = it.toString(), userId = senderId)
                onSend.invoke(messagess)
                isDropDownEnabled = false
            }
        }


        if (!message.buttons.isNullOrEmpty()) {


            ButtonGridLayout(
                buttons = message.buttons, isEnabled = isEnabled
            ) { button ->
                isEnabled = false
                val messageObj = buildInteractiveMessage(
                    eventName = button.id, message = button.value, userId = senderId
                )
                onSend.invoke(messageObj)

            }

        }


    }
}

@Composable
fun cardShapeFor(isMine: Boolean = true): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        isMine -> roundedCorners.copy(bottomEnd = CornerSize(0))
        else -> roundedCorners.copy(bottomStart = CornerSize(0))
    }
}


@Composable
@Preview(showBackground = true)
fun MessageCardPreview() {
    /* MessageCard(
         message = Message(
             senderId = Constants.BOT_ID, receiverId = Constants.USER_ID, message = "Hello",
         )
     ) {}*/
}





