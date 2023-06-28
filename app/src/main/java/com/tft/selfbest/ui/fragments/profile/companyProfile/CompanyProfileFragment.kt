package com.tft.selfbest.ui.fragments.profile.companyProfile

import android.Manifest
import android.app.Activity
import android.app.Dialog
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
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.databinding.FragmentCompanyProfileBinding
import com.tft.selfbest.models.CollabToolsRequest
import com.tft.selfbest.models.CompanyProfileDetail
import com.tft.selfbest.models.SaveOrgDetails
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.DetailActivity
import com.tft.selfbest.ui.activites.Signup
import com.tft.selfbest.ui.login.LoginActivity
import com.tft.selfbest.utils.isInternetAvailable
import com.tft.selfbest.utils.showIconErrorOnly
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class CompanyProfileFragment : Fragment(), View.OnClickListener {
    @Inject
    lateinit var pref: SelfBestPreference
    lateinit var binding: FragmentCompanyProfileBinding
    var domains = listOf<String>()
    var orgSkills = listOf<String>()
    lateinit var companyDetails: CompanyProfileDetail
    val viewModel by viewModels<CompanyProfileViewModel>()
    var isConfigToolsView = false
    var selectedBot = "slack"
    var selectedCalendar = "google"
    var distractions = listOf<String>()
    var slackWorkSpaceId = ""
    lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var someActivityResultLauncherExcel: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompanyProfileBinding.inflate(layoutInflater, container, false)
        someActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    val imagePath = getFileFromUri(uri)
                    viewModel.uploadOrgProfilePhoto(imagePath)
                }
            }
        }
        someActivityResultLauncherExcel = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    val filePath = getFileFromUri(uri)
                    viewModel.uploadOrgSheet(filePath)
                }
            }
        }
        viewModel.getCompanyDetails()
        viewModel.companyProfileObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.progress.visibility = View.GONE
                companyDetails = it.data!!
                setData()
            }
        }
        viewModel.getCompanyDomains()
        viewModel.companyDomainsObserver.observe(viewLifecycleOwner) {
//            binding.domain.onItemSelectedListener = this
            if (it is NetworkResponse.Success) {
                domains = it.data!!
                val spinAdapter = ArrayAdapter(
                    binding.root.context,
                    R.layout.spinner_main_item_style,
                    domains
                )
                spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
                binding.domain.adapter = spinAdapter
            }
        }
        viewModel.getOrgSkills()
        viewModel.companyOrgSkillsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                orgSkills = it.data!!
            }
        }
        viewModel.addSkillObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                Toast.makeText(requireContext(), "Skill added successfully", Toast.LENGTH_SHORT)
                    .show()
                binding.skillAdded.text.clear()
            }
        }
        viewModel.saveCompanyProfileObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT)
                    .show()
                binding.skillAdded.text.clear()
            }
        }
        viewModel.collabToolsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                Toast.makeText(requireContext(), "Settings saved successfully", Toast.LENGTH_SHORT)
                    .show()
                binding.skillAdded.text.clear()
            }
        }
        viewModel.deleteAccountObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                Toast.makeText(
                    requireContext(),
                    "Account settings changed successfully",
                    Toast.LENGTH_SHORT
                ).show()
                if (it.data!!.type.equals("DeactivateAccount") or it.data.type.equals("DeleteAccount")) {
                    pref.clear()
                    val loginScreen = Intent(activity, LoginActivity::class.java)
                    loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    loginScreen.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    activity?.finish()
                    startActivity(loginScreen)
                }
            }
        }

        binding.skillAdded.setOnEditorActionListener(OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                if (binding.skillAdded.text.isNotEmpty()) {
                    val skill = binding.skillAdded.text.toString()
                    if (skill in orgSkills) {
                        Toast.makeText(
                            requireContext(),
                            "Skill already present",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.skillAdded.text.clear()
                    } else {
                        viewModel.addOrgSkill(skill)
                    }
                }
                return@OnEditorActionListener true
            }
            false
        })
        binding.bots.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.slack -> {
                    selectedBot = "slack"
                    val dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.dialog_layout)
                    dialog.window?.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    dialog.setCancelable(false)
                    val workspaceId = dialog.findViewById<EditText>(R.id.slack_workspace)
                    val submit = dialog.findViewById<TextView>(R.id.okay)
                    val cancel = dialog.findViewById<TextView>(R.id.cancel)
                    submit.setOnClickListener {
                        slackWorkSpaceId = workspaceId.text.toString()
                        dialog.dismiss()
                    }

                    cancel.setOnClickListener {
                        slackWorkSpaceId = ""
                        dialog.dismiss()
                    }
                    dialog.show()
                }

                R.id.google_chats -> {
                    selectedBot = "googleBots"
                }

                R.id.ms_teams -> {
                    selectedBot = "msTeams"
                }
            }
        }

        binding.calendars.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.google_cal -> {
                    selectedCalendar = "google"
                }

                R.id.microsoft_cal -> {
                    selectedCalendar = "microsoft"
                }
            }
        }
        binding.cancelProfile.setOnClickListener(this)
        binding.cancelCollab.setOnClickListener(this)
        binding.saveProfile.setOnClickListener(this)
        binding.saveCollab.setOnClickListener(this)
        binding.companyProfile.setOnClickListener(this)
        binding.configTools.setOnClickListener(this)
        binding.uploadDb.setOnClickListener(this)
        binding.userIcon.setOnClickListener(this)
        binding.downloadDb.setOnClickListener(this)
        binding.daty.setOnClickListener(this)
        binding.ddy.setOnClickListener(this)
        binding.dapy.setOnClickListener(this)
        return binding.root
    }

    private fun setData() {
        if (companyDetails.image != "")
            activity?.let {
                Glide.with(it.applicationContext).load(companyDetails.image).into(binding.userIcon)
            }
        binding.companyName.setText(companyDetails.name)
        binding.companyWebsite.setText(companyDetails.url)
        binding.companyWebsite.isEnabled = false
        binding.linkedin.setText(companyDetails.linkedIn)
        binding.noOfEmployees.setText(companyDetails.size.toString())
        binding.domain.setSelection(domains.indexOf(companyDetails.domain))
        binding.workDen.isChecked = companyDetails.workDen
        binding.solutionPt.isChecked = companyDetails.solutionPoint
        distractions = companyDetails.distractions!!
        selectedBot = companyDetails.botType
        selectedCalendar = companyDetails.calendar
        if (selectedBot.equals("slack")) {
            binding.slack.isChecked = true
        } else if (selectedBot.equals("googleBot")) {
            binding.googleChats.isChecked = true
        } else {
            binding.msTeams.isChecked = true
        }

        if (selectedCalendar.equals("microsoft")) {
            binding.microsoftCal.isChecked = true
        } else {
            binding.googleCal.isChecked = true
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.upload_db -> {
                openFileChooser()
            }

            R.id.cancel_profile -> {
                (activity as DetailActivity).finish()
            }

            R.id.cancel_collab -> {
                (activity as DetailActivity).finish()
            }

            R.id.save_profile -> {
                if (!requireContext().isInternetAvailable()) {
                    Toast.makeText(
                        requireContext(),
                        "You dont have connectivity",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (binding.linkedin.text.toString().isEmpty()) {
                        binding.linkedin.showIconErrorOnly()
                        return
                    } else if (binding.companyName.text.toString().isEmpty()) {
                        binding.companyName.showIconErrorOnly()
                        return
                    } else if (binding.noOfEmployees.text.toString().isEmpty()) {
                        binding.noOfEmployees.showIconErrorOnly()
                        return
                    }
                    viewModel.saveCompanyDetails(
                        SaveOrgDetails(
                            domains[binding.domain.selectedItemPosition],
                            binding.linkedin.text.toString(),
                            binding.companyName.text.toString(),
                            binding.noOfEmployees.text.toString().toLong(),
                            binding.companyWebsite.text.toString(),
                            distractions,
                            pref.getLoginData!!.email!!,
                            pref.getLoginData!!.id!!
                        )
                    )
                }
            }

            R.id.save_collab -> {
                if (!requireContext().isInternetAvailable()) {
                    Toast.makeText(
                        requireContext(),
                        "You dont have connectivity",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.saveCollabTools(
                        CollabToolsRequest(
                            selectedCalendar,
                            selectedBot,
                            "",
                            binding.solutionPt.isChecked,
                            binding.workDen.isChecked
                        )
                    )
                }
            }

            R.id.company_profile -> {
                if (!isConfigToolsView)
                    return
                isConfigToolsView = false
                binding.companyProfile.setBackgroundResource(R.drawable.left_organisation_round)
                //ContextCompat.getDrawable(requireContext(),R.drawable.left_organisation_round)
                binding.companyProfile.setTextColor(Color.WHITE)
                binding.configTools.setBackgroundColor(Color.TRANSPARENT)
                binding.configTools.setTextColor(Color.parseColor("#707070"))
                binding.personalDetail.visibility = View.VISIBLE
                binding.configToolsContainer.visibility = View.GONE
            }

            R.id.config_tools -> {
                if (isConfigToolsView)
                    return
                isConfigToolsView = true
                binding.configTools.setBackgroundResource(R.drawable.right_organisation_round)
                //ContextCompat.getDrawable(requireContext(),R.drawable.right_organisation_round)
                binding.configTools.setTextColor(Color.WHITE)
                binding.companyProfile.setBackgroundColor(Color.TRANSPARENT)
                binding.companyProfile.setTextColor(Color.parseColor("#707070"))
                binding.personalDetail.visibility = View.GONE
                binding.configToolsContainer.visibility = View.VISIBLE
            }

            R.id.user_icon -> {
                if (ContextCompat.checkSelfPermission(
                        binding.root.context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    Log.e("image", "1 if")
                    someActivityResultLauncher.launch(intent)

                } else {
                        Log.e("image", "2 else")
                        ActivityCompat.requestPermissions(
                            activity as DetailActivity, arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), 1
                        )
                    }
            }

            R.id.dapy -> {
                viewModel.deleteOrgAccount("DeleteAccount")
            }

            R.id.daty -> {
                viewModel.deleteOrgAccount("DeactivateAccount")
            }

            R.id.ddy -> {
                viewModel.deleteOrgAccount("DeleteData")
            }
        }
    }

    private fun openFileChooser() {
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/vnd.ms-excel"
            someActivityResultLauncherExcel.launch(intent)

        } else {
            Log.e("image", "2 else")
            ActivityCompat.requestPermissions(
                activity as DetailActivity, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        }
    }
}