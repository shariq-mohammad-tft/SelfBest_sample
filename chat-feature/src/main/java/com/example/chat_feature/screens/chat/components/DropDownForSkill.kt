package com.example.chat_feature.screens.chat.components

import android.widget.ScrollView
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


val optionsss = listOf(
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
  /*  "eclinicalworks ehr software",
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
    "Mobile testing"*/
)


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
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DropdownExample(
    options: List<String>,
    selectedOption: (title: String) -> Unit
) {
    val pageSize = 50 // Set the number of items to load per page
    var currentPage = 0
    var allOptions = remember { mutableStateOf(options) }
    var displayedOptions by remember { mutableStateOf(allOptions.value.take(pageSize)) }
    var expanded by remember { mutableStateOf(false) }
    var scrollState = rememberScrollState()

    

    /*Column {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 10.dp)
                .wrapContentHeight()
        ) {
            TextButton(
                onClick = { expanded = true }
            ) {
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
                    displayedOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                val selectedOptions = option
                                expanded = false
                                selectedOption.invoke(option)
                            },
                        ) {
                            Text(option)
                        }
                    }

                    val remainingOptions = allOptions.value.drop(displayedOptions.size)
                    if (remainingOptions.isNotEmpty()) {
                        DropdownMenuItem(
                            onClick = {
                                currentPage++
                                val startIndex = currentPage * pageSize
                                val endIndex = startIndex + pageSize
                                displayedOptions = allOptions.value.slice(startIndex until endIndex)
                            },
                            modifier = Modifier.padding(8.dp).background(color = Color(0xFF1D71D4))
                        ) {
                            Text(text = "Load more options",
                            color = Color(0xFFFFFFFF)
                            )
                        }
                    }
                }
            }
        }*/
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
                    // Disable all buttons + mark current as selected
                    expanded=true

                },

                shape = RoundedCornerShape(19),

                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black,
                        backgroundColor = Color.LightGray.copy(alpha = 0.6f),
                        disabledContentColor = Color.Black.copy(alpha = 0.8f),
                    )
            ) {
                Text(text = "Choose skill", modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.caption)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.8f)
                    .padding(horizontal = 10.dp)
                    .fillMaxHeight().imeNestedScroll()
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
                              /*  modifier = Modifier.scrollable(
                                    orientation = Orientation.Vertical,
                                    state= rememberScrollState(0),
                                    enabled = true,
                                    reverseDirection = true,


                                )*/
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
                               /* val endIndex = startIndex + pageSize*/
                                val endIndex = minOf(startIndex + pageSize, allOptions.value.size)
                                displayedOptions = allOptions.value.slice(startIndex until endIndex)
                                CoroutineScope(Dispatchers.Default).launch {
                                    scrollState.scrollTo(0)
                                }
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
                        PageIndicator(currentPage, allOptions.value.size, Modifier.padding(8.dp))
                    }
                }
            }
        }
    }

}
@Composable
fun PageIndicator(currentPage: Int, pageCount: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) {
                Color.Black
            } else {
                Color.Gray
            }
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(8.dp)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}









