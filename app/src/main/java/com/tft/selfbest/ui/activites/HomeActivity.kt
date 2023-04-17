package com.tft.selfbest.ui.activites

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.chat_feature.interfaces.HomeActivityCaller
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.databinding.ActivityMainBinding
import com.tft.selfbest.models.suggestedApps.AppDetail
import com.tft.selfbest.ui.fragments.activityLog.ActivityLogFragment
import com.tft.selfbest.ui.fragments.overview.OverviewFragment
import com.tft.selfbest.ui.fragments.profile.ProfileViewModel
import com.tft.selfbest.ui.fragments.settings.SettingFragment
import com.tft.selfbest.utils.InstalledAppInfoUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), View.OnClickListener,HomeActivityCaller{
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var installedApps: ArrayList<InstalledAppInfoUtil.Companion.InfoObject>
    var suggestedInstalledApps: ArrayList<AppDetail> = ArrayList()
    lateinit var binding: ActivityMainBinding
    val viewModel by viewModels<ProfileViewModel>()

    @Inject
    lateinit var preferences: SelfBestPreference

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            //can send notifications
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(
                        "Firebase Message",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
//                viewModel.sendRegistrationToken(token)

                // Log and toast
                Log.e("Firebase Message", token)
            })
        } else {
            //app will not show notifications
            Toast.makeText(this, "Notifications are blocked!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideKeyboard()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!preferences.isFirstTime) {
            preferences.setFirstTime(true)
            askForNotificationPermission()
        }
        binding.progress.visibility = View.GONE
        binding.bottomNavigation.itemIconTintList = null
        bottomNavigation = findViewById(R.id.bottom_navigation)
        binding.headerTitle.text = ""
        //installedApps = InstalledAppInfoUtil.getInstallApps(this)
        loadFragment(OverviewFragment())
        viewModel.getProfileData(true)
        // val navController = findNavController(R.id.fragmentContainerView)
        binding.viewNotifications.setOnClickListener(this)
        binding.viewCalenderEvent.setOnClickListener(this)
        binding.profile.setOnClickListener(this)

        if(intent.hasExtra("FRAGMENT")){
            showActivityLogFragment()
        }
        bottomNavigation.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.overviewFragment -> {
                    loadFragment(OverviewFragment())
                    binding.headerTitle.text = ""
                    //Toast.makeText(this, "Overview Page", Toast.LENGTH_LONG).show()
                    return@setOnItemSelectedListener true
                }
//                R.id.myCourseFragment -> {
//                    loadFragment(MyCourseFragment())
//                    binding.headerTitle.text = "My Course"
//                     Toast.makeText(this, "myCourseFragment Page", Toast.LENGTH_LONG).show()
//                    return@setOnItemSelectedListener true
//                }
                R.id.statisticsFragment -> {
                    loadFragment(ActivityLogFragment(mutableListOf(), "Mobile", "daily", "", "", true))
                    binding.headerTitle.text = ""
                    // Toast.makeText(this, "statisticsFragment Page", Toast.LENGTH_LONG).show()
                    return@setOnItemSelectedListener true
                }
//                R.id.eventsFragment -> {
//                    loadFragment(MyCalendarFragment())
//                    binding.headerTitle.text = ""
//                    // Toast.makeText(this, "eventsFragment Page", Toast.LENGTH_LONG).show()
//                    return@setOnItemSelectedListener true
//                }
                R.id.eventsFragment -> {
                    val intentOpenDetailPage = Intent(this, ChatActivity::class.java)
                    ContextCompat.startActivity(this, intentOpenDetailPage, null)
                }
                R.id.settingFragment -> {
                    loadFragment(SettingFragment())
                    binding.headerTitle.text = ""
                    // Toast.makeText(this, "settingFragment Page", Toast.LENGTH_LONG).show()
                    return@setOnItemSelectedListener true
                }


            }
            false
        }
    }

    private fun showActivityLogFragment() {
        val id = intent.getIntExtra("FRAGMENT", 0)
        if(id == 1){
            val values = intent.getStringArrayExtra("DETAILS")
            val firstTime = intent.getBooleanExtra("FIRST_TIME", true)
            loadFragment(ActivityLogFragment(mutableListOf(), values!![0], values[1], values[2], values[3], firstTime))
        }
    }

    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.e("Entered", "02")
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                //can send notifications
                Log.e("Firebase Notifications", "onCreate: PERMISSION GRANTED")
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.e(
                            "Firebase Message",
                            "Fetching FCM registration token failed",
                            task.exception
                        )
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result
                    viewModel.sendRegistrationToken(token)
                    // Log and toast
                    Log.e("Firebase Message", token)
                })
            }
            else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                //ask user to choose 'yes' or 'no' for notifications
                Log.e("Else if", "Firebase")
                Snackbar.make(
                    binding.drawerLayout,
                    "Notification blocked",
                    Snackbar.LENGTH_LONG
                ).setAction("Settings") {
                    // Responds to click on the action
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
            }
            else {
                // The registered ActivityResultCallback gets the result of this request
                //ask for permission
                Log.e("Else", "Firebase")
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
        else{
            Log.e("Not Entered", "02")
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(
                        "Firebase Message",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                viewModel.sendRegistrationToken(token)

                // Log and toast

                Log.e("Firebase Message", token)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progress.visibility = View.GONE
        /*if (!isAccessibilityServiceEnabled(this, SelfBestAccessibilityService::class.java)) {
            redirectToSettings()
        }*/
        checkAccessibilityPermission()
        if(preferences.getProfilePicture != "")
            Glide.with(applicationContext).load(preferences.getProfilePicture).into(binding.profile)
        else
            binding.profile.setImageResource(R.drawable.user_icon)
    }

    // method to check is the user has permitted the accessibility permission
    // if not then prompt user to the system's Settings activity
    private fun checkAccessibilityPermission(): Boolean {
        var accessEnabled = 0
        try {
            accessEnabled =
                Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return accessEnabled != 0
        //return if (accessEnabled == 0) {
            // if not construct intent to request permission
//            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            // request permission via start activity for result
            //startActivity(intent)
        //    false
        //} else {
          //  true
        //}
    }

    private fun isAccessibilityServiceEnabled(
        context: Context,
        service: Class<out AccessibilityService?>,
    ): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (enabledService in enabledServices) {
            val enabledServiceInfo = enabledService.resolveInfo.serviceInfo
            if (enabledServiceInfo.packageName.equals(context.packageName) && enabledServiceInfo.name.equals(
                    service.name
                )
            ) return true
        }
        return false
    }

    private fun redirectToSettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // request permission via start activity for result
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.tft.selfbest.R.menu.toolbar_actions, menu)
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(com.tft.selfbest.R.id.fragmentContainerView, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.view_calender_event -> {
                openDetailPage("Upcoming Events", "calendar")
            }
            R.id.view_notifications -> {
                openDetailPage("Notifications", "notifications")
            }
            R.id.profile -> {
                openDetailPage("Profile", "profile")
            }
        }
    }

    private fun openDetailPage(headerTitle: String, detailType: String) {
        binding.progress.visibility = View.VISIBLE
        val intentOpenDetailPage = Intent(this, DetailActivity::class.java)
        intentOpenDetailPage.putExtra("header_title", headerTitle)
        intentOpenDetailPage.putExtra("detailType", detailType)
        ContextCompat.startActivity(this, intentOpenDetailPage, null)
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

//    fun hideForFullScreen(){
//        binding.myToolbar.visibility = View.GONE
//        binding.bottomNavigation.visibility = View.GONE
//    }
//
//    fun showForFullScreen(){
//        binding.myToolbar.visibility = View.VISIBLE
//        binding.bottomNavigation.visibility = View.VISIBLE
//    }

    override fun callHomeActivity() {
        val intent=Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}