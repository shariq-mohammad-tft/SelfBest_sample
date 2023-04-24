package com.tft.selfbest.ui.fragments.overview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.databinding.FragmentOverviewBinding
import com.tft.selfbest.models.ActivityTimelineResponse
import com.tft.selfbest.models.GetGoHourResponse
import com.tft.selfbest.models.InputData
import com.tft.selfbest.models.StartTime
import com.tft.selfbest.models.overview.CourseDetail
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.ChatActivity
import com.tft.selfbest.ui.adapter.ActivityTimelineAdapter
import com.tft.selfbest.ui.adapter.TaskListAdapter
import com.tft.selfbest.ui.dialog.AccessibilityOnDialog
import com.tft.selfbest.ui.fragments.detailPage.SuggestedAppsViewModel
import com.tft.selfbest.ui.fragments.getGoHour.GetGoHour
import com.tft.selfbest.ui.fragments.getGoHour.GetGoHourViewModel
import com.tft.selfbest.ui.fragments.inputProgress.InputProgress
import com.tft.selfbest.ui.fragments.inputProgress.InputProgressViewModel
import com.tft.selfbest.ui.fragments.water.WaterFragment
import com.tft.selfbest.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class OverviewFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var preferences: SelfBestPreference

    val viewModel by viewModels<SuggestedAppsViewModel>()
    private val overviewViewModel by viewModels<OverviewViewModel>()
    private val gghViewViewModel by viewModels<GetGoHourViewModel>()
    lateinit var binding: FragmentOverviewBinding
    private lateinit var getGoHourResponse: GetGoHourResponse
    private var isExpandTaskSummary: Boolean = false
    var access : Boolean = false
    private val ipViewModel by viewModels<InputProgressViewModel>()
//    private var suggestedInstalledApps: ArrayList<InstalledAppInfoUtil.Companion.InfoObject> =
//        ArrayList()
    private var courseList: ArrayList<CourseDetail> = ArrayList()
//    private lateinit var selectedAppPackageName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        access = checkAccessibilityPermission()
        if(!access){
            binding.getStartBtn.setBackgroundResource(R.drawable.get_start_disable_bg)
            binding.getStartBtn.setTextColor(Color.parseColor("#3e3e3e"))
        }else{
            binding.getStartBtn.setBackgroundResource(R.drawable.get_started_btn_bg)
            binding.getStartBtn.setTextColor(Color.parseColor("#f8f8f8"))
        }
        // viewModel.getSuggestedApps()
//        binding.installedAppList.layoutManager = GridLayoutManager(context, 3)
        binding.taskSummaryList.layoutManager = LinearLayoutManager(context)
        //val activity = activity as HomeActivity

        gghViewViewModel.activityLogObserver.observe(viewLifecycleOwner){
            if (it is NetworkResponse.Success) {
                Log.e("ActivityLog: ", " Success")
                if (it.data!!.activities == null || it.data.activities.isEmpty()) {
                    ipViewModel.getInput(InputData(preferences.getGetHourId, listOf(), 1))
                } else if (it.data.activities.isNotEmpty()) {
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, InputProgress())
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }
            }
        }

        ipViewModel.ratinObserver.observe(viewLifecycleOwner) {
            val transaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, OverviewFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
            //Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
        }

        overviewViewModel.getGoHour()
        overviewViewModel.getGoHourObserver.observe(viewLifecycleOwner){
            if (it is NetworkResponse.Success) {
                getGoHourResponse = it.data!!
                Log.e("Timer:", "${getGoHourResponse.isActive}")
                if (getGoHourResponse.isActive || getGoHourResponse.isPaused) {
                    val transaction =
                        requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, GetGoHour())
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }else if(getGoHourResponse.getStarted && getGoHourResponse.ended){
                    Log.e("GetgoHour", "ended")
                    gghViewViewModel.getActivity()
                }else if(getGoHourResponse.getStarted){
                    val transaction =
                        requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, WaterFragment())
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }
            }
        }

        gghViewViewModel.getTimeline()
        gghViewViewModel.activityTimelineObserver.observe(viewLifecycleOwner){
            if (it is NetworkResponse.Success && it.data != null) {
                //binding.noActivityTimelineData.visibility = View.VISIBLE
                Log.e("Timeline", "Empty")
                if (it.data.isNotEmpty()) {
                    Log.e("Timeline", "Not Empty")
                    binding.noActivityTimelineData.visibility = View.GONE
                    binding.recentEvent.visibility = View.VISIBLE
                    binding.recentEvent.layoutManager = LinearLayoutManager(context)
                    binding.recentEvent.adapter = ActivityTimelineAdapter(
                        it.data
                    )
                }
            }
        }
//        viewModel.suggestedAppsObserver.observe(viewLifecycleOwner) {
//            if (it is NetworkResponse.Success) {
//                suggestedInstalledApps =
//                    InstalledAppInfoUtil.getSuggestedInstalledApps(it.data as ArrayList<AppDetail>,
//                        activity.installedApps)
//                binding.installedAppList.adapter =
//                    InstalledAppsOverviewAdapter(suggestedInstalledApps,
//                        binding.root.context,
//                        true,
//                        this,
//                        false)
//
//            }
//        }
        overviewViewModel.getOverViewLevel()
        overviewViewModel.overViewLevelObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.progress.visibility = View.GONE
                binding.progressBar.progress = it.data?.currentProgress!!.roundToInt()
                binding.progressValue.text = "${it.data.currentProgress.roundToInt()}%"
                val currentLevel = it.data.level
                binding.level.text = "Level $currentLevel"
            }else if(it is NetworkResponse.Error){
                binding.progress.visibility = View.GONE
                Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show()
                preferences.clear()
                val loginScreen = Intent(activity, LoginActivity::class.java)
                loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(loginScreen)
                activity?.finish()
            }
        }
        overviewViewModel.getCourses()
        overviewViewModel.overViewCourseObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                if (it.data?.allCourses != null)
                    courseList = it.data.allCourses as ArrayList<CourseDetail>
                binding.taskSummaryList.adapter =
                    TaskListAdapter(courseList)
            }
        }

        overviewViewModel.getStartedObserver.observe(viewLifecycleOwner) {
            if(it is NetworkResponse.Success) {
                val transaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, WaterFragment())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }
//        binding.installedAppList.adapter = InstalledAppsOverviewAdapter(
//            suggestedInstalledApps,
//            binding.root.context, true, this, false
//        )
        binding.taskSummaryList.adapter =
            TaskListAdapter(courseList)
        //binding.startInstalledAppBtn.isEnabled = false
        binding.dropDownArrow.setOnClickListener(this)
        binding.getStartBtn.setOnClickListener(this)
        binding.getUnstuck.setOnClickListener(this)
        //binding.shareContainer.setOnClickListener(this)
        //binding.startInstalledAppBtn.setOnClickListener(this)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when (view?.id) {
//            R.id.share_container -> {
//                val intentOpenDetailPage = Intent(context, DetailActivity::class.java)
//                intentOpenDetailPage.putExtra("header_title", "Share with manager")
//                intentOpenDetailPage.putExtra("detailType", "share")
//                ContextCompat.startActivity(binding.root.context, intentOpenDetailPage, null)
//                Toast.makeText(context, "To do something", Toast.LENGTH_LONG).show()
//            }
            R.id.drop_down_arrow -> {
                isExpandTaskSummary = !isExpandTaskSummary
                binding.dropDownArrow.setImageResource(if (isExpandTaskSummary) R.drawable.arror_drop_up else R.drawable.arrow_drop_down)
                binding.taskSummaryList.visibility = if (isExpandTaskSummary)
                    View.VISIBLE else View.GONE
            }
            R.id.get_start_btn -> {
                Log.e("OverView : ", " Started")
//                this::selectedAppPackageName.isInitialized ?: return
                if(!access){
                    AccessibilityOnDialog().show(
                                parentFragmentManager,
                                "AccessibilityOnDialog"
                            )
                }else {
                    overviewViewModel.getStarted(StartTime(Instant.now().toString()))
                }
//                val launchIntent: Intent? =
//                    binding.root.context.packageManager.getLaunchIntentForPackage(
//                        selectedAppPackageName)
//                if (launchIntent != null)
//                    ContextCompat.startActivity(activity as HomeActivity, launchIntent, null)
            }

            R.id.get_unstuck -> {
                val intentOpenDetailPage = Intent(activity, ChatActivity::class.java)
                ContextCompat.startActivity(requireActivity(), intentOpenDetailPage, null)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        access = checkAccessibilityPermission()
        if(!access){
            binding.getStartBtn.setBackgroundResource(R.drawable.get_start_disable_bg)
            binding.getStartBtn.setTextColor(Color.parseColor("#3e3e3e"))
        }else{
            binding.getStartBtn.setBackgroundResource(R.drawable.get_started_btn_bg)
            binding.getStartBtn.setTextColor(Color.parseColor("#f8f8f8"))
        }
//        binding.startInstalledAppBtn.isEnabled = false
//        binding.startInstalledAppBtn.setBackgroundResource(R.drawable.get_go_hour_start_btn)
//        binding.startInstalledAppBtn.setTextColor(ContextCompat.getColor(binding.root.context,
//            R.color.start_app_text_color))
//        viewModel.getSuggestedApps()
    }

    private fun checkAccessibilityPermission(): Boolean {
        var accessEnabled = 0
        try {
            accessEnabled = Settings.Secure.getInt(activity?.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return accessEnabled == 1
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

//    override fun selectedAppListener(selectedAppPName: String) {
//        binding.startInstalledAppBtn.isEnabled = true
//        binding.startInstalledAppBtn.background = resources.getDrawable(R.drawable.add_list_bg)
//        binding.startInstalledAppBtn.setTextColor(resources.getColor(R.color.white))
//        selectedAppPackageName = selectedAppPName
//    }
}