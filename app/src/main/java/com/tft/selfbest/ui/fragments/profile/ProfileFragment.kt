package com.tft.selfbest.ui.fragments.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.internal.LinkedTreeMap
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.databinding.FragmentProfileBinding
import com.tft.selfbest.models.ProfileChangesData
import com.tft.selfbest.models.ProfileData
import com.tft.selfbest.models.ProfileWorkingData
import com.tft.selfbest.models.calendarEvents.RecursiveDays
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.DetailActivity
import com.tft.selfbest.ui.activites.MyButton
import com.tft.selfbest.ui.activites.MyButtonListener
import com.tft.selfbest.ui.activites.MySwipeHelper
import com.tft.selfbest.ui.adapter.*
import com.tft.selfbest.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener,
    RecommendationsAdapter.SkillAddedListener, ProfileSkillsAdapter.ChangeRatingListener {
    @Inject
    lateinit var pref: SelfBestPreference
    lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()
    private lateinit var profileData: ProfileData
    private lateinit var personalityTypes: ArrayList<String>
    private var workingDaysTemp= arrayListOf<RecursiveDays>()
    lateinit var linkedIndialog: Dialog
    var deactivated = false
    private var workingDays= mutableListOf<String>()
    private var currentSkills = mutableListOf<String>()
    private val genderCategory = listOf("Male", "Female", "Others")
    private val dayNameInWeek =
        listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    private var rSkills = listOf(
        "python",
        "angular",
        "android",
        "adobe systems adobe acrobat",
        "c++",
        "go",
        "swift",
        "angular",
        "R",
        "confluence",
        "c#",
        "reactjs",
        "django",
        "java",
        "mongodb"
    )

    //    val personalities = arrayOf(
//        "Having deadlines",
//        "Having accountability partner",
//        "Rewards",
//        "Reminders",
//        "Reducing distractions",
//        "Looking at inspiring content",
//        "Working in isolation",
//        "Working around people",
//        "Just help getting started",
//        "Taking micro-breaks during work"
//    )
//    lateinit var customPersonlityList: List<String>
//    var FinalPersonality = arrayListOf<String>()
//    var personalityList = arrayListOf<Int>()
//    val personalityBoolean = BooleanArray(personalities.size)
    private var startHour = 0
    private var startMinute = 0
    private var endHour = 0
    private var endMinute = 0
    private lateinit var timePicker: TimePickerDialog
    private var profileSkills = LinkedTreeMap<String, Int>()
    private var pendingSkills = LinkedTreeMap<String, Int>()
    private var isIntegrationView = false
    lateinit var calendarType: String
    lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
//    var isSkillAvailableForSave = false
    private lateinit var allSkills: List<String>

    companion object {
        private const val IMAGE_PICK_CODE = 100
        private const val GOOGLE_REDIRECT_URL = "https://staging.self.best/user/profile"

        // working only in production
        private const val MICROSOFT_REDIRECT_URL = "https://self.best/user/profile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        binding.progress.visibility = View.GONE
        val swipe = object : MySwipeHelper(requireActivity(), binding.skillList, 200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(
                    MyButton(activity!!, "Delete", 30, Color.parseColor("#E20404"),
                        object : MyButtonListener {
                            override fun onClick(pos: Int) {
                                Log.e("Skills Position", "$pos ${currentSkills[pos]}")
                                (binding.skillList.adapter as ProfileSkillsAdapter).deleteSkill(
                                    currentSkills[pos]
                                )
                                currentSkills.removeAt(pos)
                            }

                        })
                )
            }

        }

        someActivityResultLauncher= registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    val imagePath = getFileFromUri(uri)
                    viewModel.updateProfilePhoto(imagePath)
                }
            }
        }

        viewModel.accountSettingObserver.observe(viewLifecycleOwner) {
            if(it is NetworkResponse.Success){
                Toast.makeText(context, "Data updated successfully", Toast.LENGTH_LONG).show()
                if(deactivated){
                    deactivated = false
                    pref.clear()
                    val loginScreen = Intent(activity, LoginActivity::class.java)
                    loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    loginScreen.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    activity?.finish()
                    startActivity(loginScreen)
                }
            }
        }
        viewModel.profileObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                profileData = it.data?.profileData!!
                profileSkills = profileData.skills as LinkedTreeMap<String, Int>
                for (skill in profileSkills) {
                    currentSkills.add(skill.key)
                }
                pendingSkills = profileData.pendingskills as LinkedTreeMap<String, Int>
                Log.e("Skill", pendingSkills.toString())
                //pref.setProfileData(it.data.profileData)
                //customPersonlityList = profileData.customPersonality
                setData()
            }
        }
        viewModel.rSkillsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success && it.data!!.isNotEmpty()) {
                rSkills = it.data
                binding.recommendedSkills.adapter = RecommendationsAdapter(
                    binding.root.context,
                    rSkills,
                    this
                )
            }
        }
        viewModel.personalityTypeObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                personalityTypes = it.data as ArrayList<String>
                Log.e("Personality List", "" + personalityTypes.size)
                val personalityAdapter = ArrayAdapter(
                    binding.root.context,
                    R.layout.spinner_main_item_style,
                    personalityTypes
                )
                //personalityTypes.add("Choose Personality Attribute")
                personalityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
                binding.personalityTypeSpinner.adapter = personalityAdapter
                binding.personalityTypeSpinner.setSelection(personalityTypes.indexOf(profileData.personalityType))
//                binding.personalityTypeSpinner.onItemSelectedListener =
//                    object : AdapterView.OnItemSelectedListener {
//                        override fun onNothingSelected(parent: AdapterView<*>?) {}
//                        override fun onItemSelected(
//                            parent: AdapterView<*>?,
//                            view: View?,
//                            position: Int,
//                            id: Long
//                        ) {
//                            if (binding.personalityTypeSpinner.selectedItem.toString() == "Choose Personality Attribute") {
//                                val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
//                                builder.setCancelable(false)
//
//                                builder.setMultiChoiceItems(personalities, personalityBoolean,
//                                    OnMultiChoiceClickListener { _, i, b ->
//                                        if (b) {
//                                            personalityList.add(i)
//                                            personalityList.sort()
//                                        } else {
//                                            personalityList.remove(Integer.valueOf(i))
//                                        }
//                                    })
//
//                                builder.setPositiveButton(
//                                    "OK"
//                                ) { _, _ ->
//                                    val stringBuilder = StringBuilder()
//                                    for (j in 0 until personalityList.size) {
//                                        FinalPersonality.add(personalities[personalityList[j]])
//                                        stringBuilder.append(personalities[personalityList[j]])
//                                        if (j != personalityList.size - 1) {
//                                            stringBuilder.append(", ")
//                                        }
//                                    }
//                                    binding.personalityTypeSpinner.setSelection(
//                                        personalityTypes.indexOf(
//                                            profileData.personalityType
//                                        )
//                                    )
//                                }
//
//                                builder.setNegativeButton(
//                                    "Cancel"
//                                ) { dialogInterface, _ ->
//                                    dialogInterface.dismiss()
//                                    binding.personalityTypeSpinner.setSelection(
//                                        personalityTypes.indexOf(
//                                            profileData.personalityType
//                                        )
//                                    )
//                                }
//                                builder.show()
//                            }
//                        }
//                    }

            }
        }
        viewModel.profileImageObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                Toast.makeText(context, "Image updated successfully", Toast.LENGTH_SHORT).show()
                Glide.with(binding.root.context).load(URL(it.data?.imageUrl)).into(binding.userIcon)
            }
        }
        viewModel.skillsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                allSkills = it.data as ArrayList<String>
                val adapter = ArrayAdapter(
                    binding.root.context,
                    android.R.layout.simple_list_item_1,
                    allSkills
                )
                binding.skillSearch.setAdapter(adapter)
                binding.skillSearch.threshold = 1
            }

        }
        viewModel.profileChangesDoneMessageObserver.observe(viewLifecycleOwner) {
            Log.e("Profile", " Updated")
            Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
        }
//        if (pref.getProfileData == null) {
        viewModel.getProfileData(false)
//        } else {
//            profileData = pref.getProfileData!!
//            profileSkills = profileData.skills as LinkedTreeMap<String, Int>
//            setData()
//        }
        viewModel.getAllSkills()
        viewModel.getPersonalityList()

        binding.skillSearch.onItemClickListener =
            AdapterView.OnItemClickListener { arg0, _, position, _ ->
                val selectedItem = arg0.getItemAtPosition(position) as String
                binding.skillList.visibility = View.VISIBLE
                profileSkills[selectedItem] = 1
                currentSkills.add(selectedItem)
                (binding.skillList.adapter as ProfileSkillsAdapter).addSkill(
                    binding.skillSearch.text.toString(),
                    1
                )
                binding.skillSearch.text.clear()
                viewModel.getRecommendation(selectedItem)
                Toast.makeText(context, "Skill added successfully", Toast.LENGTH_SHORT).show()
            }
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.userIcon.setOnClickListener(this)
        binding.personal.setOnClickListener(this)
        binding.integrations.setOnClickListener(this)
        binding.googleCalBtn.setOnClickListener(this)
        binding.microsoftCalBtn.setOnClickListener(this)
        binding.startTimeContainer.setOnClickListener(this)
        binding.endTimeContainer.setOnClickListener(this)
        binding.cancelProfile.setOnClickListener(this)
        binding.saveProfile.setOnClickListener(this)
//        binding.addSkillBtn.setOnClickListener(this)
        binding.skillSearch.setOnClickListener(this)
        binding.testLink.setOnClickListener(this)
        binding.daty.setOnClickListener(this)
        binding.ddy.setOnClickListener(this)
        binding.dapy.setOnClickListener(this)
//        binding.deleteSkill.setOnClickListener(this)
    }


    private fun setData() {
        setBackendTime()
        //binding.userIcon.setImageURI(Uri.parse(profileData.firstName))
//        Log.e("Image : ",""+profileData.image)
        if (profileData.image != "")
            activity?.let { Glide.with(it.applicationContext).load(profileData.image).into(binding.userIcon) }
        val name = "${profileData.firstName} ${profileData.lastName}"
        binding.userName.text = name
        binding.userProfession.text = profileData.occupation
        binding.firstName.setText(profileData.firstName)
        binding.lastName.setText(profileData.lastName)
        binding.organisation.text = profileData.organisationName
        binding.jobPosition.setText(profileData.occupation)
        binding.experience.setText(profileData.noOfExperience.toString())
        binding.startTime.text = setTimeInLocalFormat(startHour, startMinute)
        binding.endTime.text = setTimeInLocalFormat(endHour, endMinute)
        binding.googleCalBtn.text =
            if (profileData.importGoogleCalendar!!) "Connected" else "Connect"
        binding.microsoftCalBtn.text =
            if (profileData.importOutlookCalendar!!) "Connected" else "Connect"
        binding.googleCalBtn.setTextColor(if(profileData.importGoogleCalendar!!) Color.parseColor("#1D71D4") else Color.WHITE)
        binding.microsoftCalBtn.setTextColor(if (profileData.importOutlookCalendar!!) Color.parseColor("#1D71D4") else Color.WHITE)
        binding.googleCalBtn.setBackgroundResource(if (profileData.importGoogleCalendar!!) R.drawable.cal_connected_bg else R.drawable.done_bg)
        binding.microsoftCalBtn.setBackgroundResource(if (profileData.importOutlookCalendar!!) R.drawable.cal_connected_bg else R.drawable.done_bg)
        binding.genderSpinner.onItemSelectedListener = this
        val spinAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_main_item_style,
            genderCategory
        )
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        binding.genderSpinner.adapter = spinAdapter
        binding.genderSpinner.setSelection(genderCategory.indexOf(profileData.gender))
        Log.e("Gender", "" + profileData.gender)
        if (profileSkills.isNotEmpty()) {
            binding.skillList.visibility = View.VISIBLE
        }
        binding.skillList.layoutManager = LinearLayoutManager(binding.root.context)
        binding.skillList.adapter =
            ProfileSkillsAdapter(
                binding.root.context,
                profileSkills,
                this
            )
        if (pendingSkills.isNotEmpty()) {
            binding.pendingSkillHeading.visibility = View.VISIBLE
            binding.pendingSkillList.visibility = View.VISIBLE
        }
        binding.pendingSkillList.layoutManager = LinearLayoutManager(binding.root.context)
        binding.pendingSkillList.adapter =
            PendingSkillAdapter(
                binding.root.context,
                pendingSkills
            )
        binding.workDays.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        workingDaysTemp = setWorkingDays(profileData.working)
        binding.workDays.adapter =
            RecursiveDaysAdapter(binding.root.context, workingDaysTemp)
        binding.recommendedSkills.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recommendedSkills.adapter =
            RecommendationsAdapter(binding.root.context, rSkills, this)

    }

    private fun setBackendTime() {
        val workingTime = profileData.working[0]
        startHour = workingTime.startHour + 5
        startMinute = workingTime.startMinute + 30
        endHour = workingTime.endHour + 5
        endMinute = workingTime.endMinute + 30
        if (startMinute >= 60) {
            startHour += 1
            startMinute -= 60
        }
        if (endMinute >= 60) {
            endHour += 1
            endMinute -= 60
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        profileData.gender = genderCategory[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // binding.genderSpinner.setSelection(genderCategory.indexOf("Others"))
    }

    private fun setWorkingDays(list: List<ProfileWorkingData>): ArrayList<RecursiveDays> {
        val recursiveDays = ArrayList<RecursiveDays>()
        for (day in dayNameInWeek) {
            var isSelected = false
            for (workingData in list) {
                if (day == workingData.dayName) {
                    isSelected = true
                    recursiveDays.add(RecursiveDays(day.substring(0, 1), day))
                    break
                }
            }
            if (!isSelected)
                recursiveDays.add(RecursiveDays(day.substring(0, 1), day, false))
        }
        return recursiveDays
    }

    private fun setTimeInLocalFormat(hours: Int, minutes: Int): String {
        val suffix = if (hours > 11) "PM" else "AM"
        val selectedHour =
            if (hours > 12) hours - 12 else if (hours == 0) 12 else hours
        return String.format(
            "%s : %s %s",
            if (selectedHour < 10) "0$selectedHour" else selectedHour.toString(),
            if (minutes < 10) "0$minutes" else minutes.toString(),
            suffix
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.user_icon -> {
                if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Environment.isExternalStorageManager()
                    } else {
                        ContextCompat.checkSelfPermission(
                            binding.root.context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(
                            binding.root.context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    }
                ) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    Log.e("image", "1 if")
                    someActivityResultLauncher.launch(intent)
//                    val intent = Intent(Intent.ACTION_PICK)
//                    intent.type = "image/*"
//                    Log.e("image", "1 if")
//                    someActivityResultLauncher.launch(intent)
//                    startActivityForResult(intent, IMAGE_PICK_CODE)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Log.e("image", "2 if")
                        val intent = Intent()
                        intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                        val uri =
                            Uri.fromParts("package", (activity as DetailActivity).packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } else {
                        Log.e("image", "2 else")
                        ActivityCompat.requestPermissions(
                            activity as DetailActivity, arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE
                            ), 1
                        )
                    }
                }
            }
            R.id.cancel_profile -> {
                (activity as DetailActivity).finish()
            }
            R.id.save_profile -> {
                val experience = binding.experience.text.toString().toInt()
                val firstName = binding.firstName.text.toString()
                val gender = genderCategory[binding.genderSpinner.selectedItemPosition]
                val lastName = binding.lastName.text.toString()
                //val occupation = binding.userProfession.text.toString()
                val occupation: String = if (binding.userProfession.text.toString() != "")
                    binding.userProfession.text.toString()
                else
                    "Software Engineer"
//                val personalityType =
//                    personalityTypes[binding.personalityTypeSpinner.selectedItemPosition]
                val startWorkingTime = timeInString(startHour, startMinute)
                val endWorkingTime = timeInString(endHour, endMinute)
                val skills: LinkedTreeMap<String, Int> = LinkedTreeMap()
                profileSkills.forEach {
                    skills[it.key] = it.value.toInt()
                }
                pendingSkills.forEach{
                    skills[it.key] = 1
                }
                for(day in workingDaysTemp){
                    if(day.isSelected)
                        workingDays.add(day.recursiveDate)
                }
                val profileChangesData = ProfileChangesData(
                    "",
                    "",
                    listOf(),
                    //FinalPersonality.distinct(),//custom personality
                    experience,
                    firstName,
                    gender,
                    listOf(),
                    false,
                    lastName,
                    "Asia/Calcutta",
                    occupation,
                    "",
                    false,
                    skills,
                    endWorkingTime,
                    startWorkingTime,
                    workingDays,
                    //listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Saturday"),
                    ""
                )
//                pref.setProfileData(
//                    ProfileData(
//                        pref.getProfileData?.contact,
//                        pref.getProfileData?.accountabilitypartner,
//                        pref.getProfileData?.createdAt,
//                        pref.getProfileData?.dob,
//                        pref.getProfileData?.email,
//                        pref.getProfileData?.existingGoals,
//                        pref.getProfileData?.extensionUsedAt,
//                        binding.firstName.text.toString(),
//                        gender,
//                        pref.getProfileData?.id,
//                        pref.getProfilePicture,
//                        pref.getProfileData?.importConnectionsLinkedIn,
//                        pref.getProfileData?.importGoogleCalendar,
//                        pref.getProfileData?.importOutlookCalendar,
//                        pref.getProfileData?.interests,
//                        pref.getProfileData?.isOrgAdmin,
//                        pref.getProfileData?.isadmin,
//                        pref.getProfileData?.isapproved,
//                        pref.getProfileData?.jiraId,
//                        binding.lastName.text.toString(),
//                        pref.getProfileData?.lastlogin,
//                        pref.getProfileData?.lastloginworkinghour,
//                        pref.getProfileData?.learning,
//                        pref.getProfileData?.linkedinProfileUrl,
//                        experience,
//                        occupation,
//                        pref.getProfileData?.password,
//                        personalityType,
//                        pref.getProfileData?.rejectedCount,
//                        pref.getProfileData?.role,
//                        pref.getProfileData?.skillenhanced,
//                        skills,
//                        pref.getProfileData?.timeZone,
//                        pref.getProfileData?.typicalProcrastinatorTrigger,
//                        pref.getProfileData?.updatedAt,
//                        pref.getProfileData?.working!!,
//                        pref.getProfileData?.organisationName!!,
//                        pref.getProfileData?.customPersonality!!
//                    )
//                )
                viewModel.saveProfileChangesData(profileChangesData)

            }
            R.id.skill_search -> {
                binding.skillSearch.requestFocus()
                if (binding.skillSearch.text.isEmpty()) {
                    Toast.makeText(
                        binding.root.context,
                        "Skill must be not null",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                binding.skillList.visibility = View.VISIBLE
                //currentSkills.add(binding.skill.text.toString())
                if (isPresentSkill(binding.skillSearch.text.toString())) {
                    currentSkills.add(binding.skillSearch.text.toString())
                    profileSkills[binding.skillSearch.text.toString()] = 1
                    (binding.skillList.adapter as ProfileSkillsAdapter).addSkill(
                        binding.skillSearch.text.toString(),
                        1
                    )
                    viewModel.getRecommendation(binding.skillSearch.text.toString())
                    Toast.makeText(context, "Skill added successfully", Toast.LENGTH_SHORT).show()
                }else{
                    pendingSkills[binding.skillSearch.text.toString()] = 1
                    (binding.pendingSkillList.adapter as PendingSkillAdapter).addSkill(
                        binding.skillSearch.text.toString(),
                        1
                    )
                }
                binding.skillSearch.text.clear()
//                isSkillAvailableForSave = !isSkillAvailableForSave
//                if (isSkillAvailableForSave) {
//                    binding.skill.requestFocus()
//                    binding.addSkillContainer.visibility = View.VISIBLE
//                    binding.skillSaveBtn.text = "Save"
//                } else {
//                    if (binding.skill.text.isEmpty()) {
//                        Toast.makeText(
//                            binding.root.context,
//                            "Skill must be not null",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        isSkillAvailableForSave = !isSkillAvailableForSave
//                        return
//                    }
//                    //  profileSkills[binding.skill.text.toString()] = 1.0
//                    (binding.skillList.adapter as ProfileSkillsAdapter).addSkill(
//                        binding.skill.text.toString(),
//                        1.0
//                    )
//                    viewModel.getRecommendation(binding.skill.text.toString())
//                    binding.addSkillContainer.visibility = View.GONE
//                    binding.skillSaveBtn.text = "Add new"
//                }

            }
//            R.id.delete_skill -> {
//                binding.addSkillContainer.visibility = View.GONE
//                binding.skill.text.clear()
//                isSkillAvailableForSave = false
//                binding.skillSaveBtn.text = "Add new"
//            }
            R.id.personal -> {
                if (!isIntegrationView)
                    return
                isIntegrationView = false
                binding.personalDetailText.text = "Edit your Personal Details"
                binding.personal.setBackgroundResource(R.drawable.left_organisation_round)
                    //ContextCompat.getDrawable(requireContext(),R.drawable.left_organisation_round)
                binding.personal.setTextColor(Color.WHITE)
                binding.integrations.setBackgroundColor(Color.TRANSPARENT)
                binding.integrations.setTextColor(Color.parseColor("#707070"))
                binding.personalDetail.visibility = View.VISIBLE
                binding.integrationsDetail.visibility = View.GONE
            }
            R.id.integrations -> {
                if (isIntegrationView)
                    return
                isIntegrationView = true
                binding.personalDetailText.text = "Calendar"
                binding.integrations.setBackgroundResource(R.drawable.right_organisation_round)
                //ContextCompat.getDrawable(requireContext(),R.drawable.right_organisation_round)
                binding.integrations.setTextColor(Color.WHITE)
                binding.personal.setBackgroundColor(Color.TRANSPARENT)
                binding.personal.setTextColor(Color.parseColor("#707070"))
                binding.personalDetail.visibility = View.GONE
                binding.integrationsDetail.visibility = View.VISIBLE
            }
            R.id.google_cal_btn -> {
                if (profileData.importGoogleCalendar!!)
                    viewModel.disconnectAllCalendar()
                else {
                    calendarType = "Google"
                    setCalendarPermissionWebViewDialog("https://accounts.google.com/o/oauth2/auth?approval_prompt=force&access_type=offline&client_id=446007361957-eajrsduofmu470dv2rsl0veol35b1bag.apps.googleusercontent.com&redirect_uri=https://staging.self.best/user/profile&response_type=code&scope=https://www.googleapis.com/auth/calendar&state=state-token&flowName=GeneralOAuthFlow")
                }
            }
            R.id.microsoft_cal_btn -> {
                if (profileData.importOutlookCalendar!!)
                    viewModel.disconnectAllCalendar()
                else {
                    calendarType = "Outlook"
                    setCalendarPermissionWebViewDialog("https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=f2d04265-c5b8-414c-83f6-1f38d8f139cb&response_type=code&redirect_uri=https://self.best/user/profile&response_mode=query&scope=openid%20offline_access%20https%3A%2F%2Fgraph.microsoft.com%2FCalendars.ReadWrite&state=12345")
                }
            }
            R.id.start_time_container -> {
                timePicker = TimePickerDialog(
                    binding.root.context,
                    { _, selectedHour, selectedMinute ->
                        startHour = selectedHour
                        startMinute = selectedMinute
                        binding.startTime.text = setTimeInLocalFormat(startHour, startMinute)
                    },
                    startHour,
                    startMinute,
                    false
                )
                timePicker.show()
            }
            R.id.end_time_container -> {
                timePicker = TimePickerDialog(
                    binding.root.context,
                    { _, selectedHour, selectedMinute ->
                        endHour = selectedHour
                        endMinute = selectedMinute
                        binding.endTime.text = setTimeInLocalFormat(endHour, endMinute)
                    },
                    endHour,
                    endMinute,
                    false
                )
                timePicker.setMessage("Select end time")
                timePicker.show()
            }
            R.id.test_link -> {
                val openURL = Intent(Intent.ACTION_VIEW)
                val testUrl = "https://www.16personalities.com/free-personality-test"
                openURL.data = Uri.parse(testUrl)
                startActivity(openURL)
            }

            R.id.daty -> {
                deactivated = true
                viewModel.accountSetting("DeactivateAccount")
            }
            R.id.dapy -> {
                deactivated = true
                viewModel.accountSetting("DeleteAccount")
            }
            R.id.ddy -> {
                viewModel.accountSetting("DeleteData")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            if (uri != null) {
                val imagePath = getFileFromUri(uri)
                viewModel.updateProfilePhoto(imagePath)
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        if (uri.path == null) {
            return null
        }
        var realPath = String()
        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (uri.path!!.contains("/document/image:")) {
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else {
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val column = "_data"
            val projection = arrayOf(column)
            val cursor = requireContext().contentResolver.query(
                databaseUri,
                projection,
                selection,
                selectionArgs,
                null
            )
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(column)
                    realPath = cursor.getString(columnIndex)
                }
                cursor.close()
            }
        } catch (e: Exception) {
            Log.i("GetFileUri Exception:", e.message ?: "")
        }
        val path = realPath.ifEmpty {
            when {
                uri.path!!.contains("/document/raw:") -> uri.path!!.replace(
                    "/document/raw:",
                    ""
                )
                uri.path!!.contains("/document/primary:") -> uri.path!!.replace(
                    "/document/primary:",
                    "/storage/emulated/0/"
                )
                else -> return null
            }
        }
        return File(path)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setCalendarPermissionWebViewDialog(
        linkedinAuthURLFull: String,
    ) {
        linkedIndialog = Dialog(binding.root.context)
        linkedIndialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        linkedIndialog.setCancelable(true)
        linkedIndialog.setContentView(R.layout.linkedin_popup_layout)
        val window: Window? = linkedIndialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val webView = WebView(binding.root.context)
        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = true
        webView.webViewClient = LinkedInWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.userAgentString = "Chrome/56.0.0.0 Mobile"
        webView.loadUrl(linkedinAuthURLFull)
        linkedIndialog.setContentView(webView)
        linkedIndialog.show()
    }

    @Suppress("OverridingDeprecatedMember")
    inner class LinkedInWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            view?.clearHistory()
            view?.clearCache(true)
            if (request?.url.toString()
                    .startsWith(if (calendarType == "Google") GOOGLE_REDIRECT_URL else MICROSOFT_REDIRECT_URL)
            ) {
                handleUrl(request?.url.toString(), view)
                return true
            }
            return false
        }

        // Check web view url for access token code or error
        private fun handleUrl(url: String, view: WebView?) {
            val uri = Uri.parse(url)
            if (url.contains("code")) {
                val code = uri.getQueryParameter("code") ?: ""
                linkedIndialog.dismiss()
/*
                val reDirectUrl =
                    if (calendarType == "Google") "/user/profile" else MICROSOFT_REDIRECT_URL
*/
                viewModel.connectCalendar(code, "/user/profile", calendarType)
            } else if (url.contains("error")) {
                val error = uri.getQueryParameter("error") ?: ""
                Log.e("Error: ", error)
                linkedIndialog.dismiss()
                Toast.makeText(binding.root.context, "Error: $error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun timeInString(hours: Int, minutes: Int): String {
        val time = if (minutes < 30) {
            "${hours.minus(6)}:${minutes.plus(30)}"
        } else {
            "${hours.minus(5)}:${minutes.minus(30)}"
        }
        return time
    }

    override fun skillAdded(skill: String) {
        (binding.skillList.adapter as ProfileSkillsAdapter).addSkill(
            skill,
            1
        )
        binding.skillList.visibility = View.VISIBLE
        viewModel.getRecommendation(skill)
    }

    override fun changeRating(level: Float, skill: String) {
        profileSkills[skill] = level.toInt()
        binding.skillList.adapter =
            ProfileSkillsAdapter(binding.root.context, profileSkills, this)
    }

    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        TODO("Not yet implemented")
    }

    private fun isPresentSkill(enteredSkill: String): Boolean{
        for(s in allSkills){
            if(s.equals(enteredSkill)){
                return true
            }
        }
        return false
    }

}
