package com.tft.selfbest.ui.fragments.signup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentSignupBinding
import com.tft.selfbest.models.SignUpDetail
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.*
import com.tft.selfbest.ui.adapter.RecommendationsAdapter
import com.tft.selfbest.ui.adapter.SignUpSkillListAdapter
import com.tft.selfbest.ui.fragments.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SignupFragment : Fragment(), View.OnClickListener,
    SignUpSkillListAdapter.ChangeRatingListener,
    RecommendationsAdapter.SkillAddedListener {

    lateinit var binding: FragmentSignupBinding
    val viewModel by viewModels<ProfileViewModel>()
    private var profileSkills = LinkedTreeMap<String, Int>()
    private var resumeSkills = LinkedTreeMap<String, Int>()
    private lateinit var allSkills: List<String>
    private var currentSkills = mutableListOf<String>()
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

    var rSkills = listOf(
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
    lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        //private const val RESUME_PICKER = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        someActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    val path = getFileFromUri(uri)
                    viewModel.getResumeSkills(path)
                    binding.uploadResume.text = uri.path!!
                }
            }
        }

        binding.skillList.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.skillList.adapter =
            SignUpSkillListAdapter(
                binding.root.context,
                profileSkills,
                this
            )

        binding.recommendedSkills.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recommendedSkills.adapter =
            RecommendationsAdapter(binding.root.context, rSkills, this)
        viewModel.getProfile("Window")
        viewModel.getJobs()
        viewModel.getSkills()
        viewModel.pSkillsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                allSkills = it.data as ArrayList<String>
                Log.e("Skills List", "$allSkills")

                val adapter = ArrayAdapter(
                    binding.root.context,
                    android.R.layout.simple_list_item_1,
                    allSkills
                )
                binding.skill.setAdapter(adapter)
                binding.skill.threshold = 1
            }
        }
        viewModel.signupObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.profile.visibility = View.GONE
                binding.completed.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(context, HomeActivity::class.java))
                    activity?.finish()
                }, 5000)
            } else if (it is NetworkResponse.Error) {
                Toast.makeText(activity, it.msg, Toast.LENGTH_SHORT).show()
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
        viewModel.profileObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                resumeSkills = it.data?.profileData?.skills as LinkedTreeMap<String, Int>
                for (key in resumeSkills.keys) {
                    profileSkills[key] = 3
                    currentSkills.add(key)
                }
                if (resumeSkills.isNotEmpty()) {
                    binding.skillListContainer.visibility = View.VISIBLE
                }
                binding.skillList.adapter =
                    SignUpSkillListAdapter(
                        binding.root.context,
                        profileSkills,
                        this
                    )
            }
        }
        viewModel.resumeUploadLiveDataObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                viewModel.getProfile("Windows")
            }
        }

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
                                (binding.skillList.adapter as SignUpSkillListAdapter).deleteSkill(
                                    currentSkills[pos]
                                )
                                currentSkills.removeAt(pos)
                            }

                        })
                )
            }

        }
        binding.skill.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.cancelEvent.setOnClickListener(this)
        binding.uploadResume.setOnClickListener(this)
        binding.skill.onItemClickListener = OnItemClickListener { arg0, _, position, _ ->
            val selectedItem = arg0.getItemAtPosition(position) as String
            binding.skillListContainer.visibility = View.VISIBLE
            currentSkills.add(selectedItem)
            profileSkills[selectedItem] = 1
            (binding.skillList.adapter as SignUpSkillListAdapter).addSkill(
                binding.skill.text.toString(),
                1
            )
            binding.skill.text.clear()
            viewModel.getRecommendation(selectedItem)
            Toast.makeText(context, "Skill added successfully", Toast.LENGTH_SHORT).show()
        }
//        binding.finish.setOnClickListener(this)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.skill -> {
                binding.skill.requestFocus()
                if (binding.skill.text.isEmpty()) {
                    Toast.makeText(
                        binding.root.context,
                        "Skill must be not null",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                //binding.emptyList.visibility = View.GONE
                binding.skillListContainer.visibility = View.VISIBLE
                currentSkills.add(binding.skill.text.toString())
                profileSkills[binding.skill.text.toString()] = 1
                (binding.skillList.adapter as SignUpSkillListAdapter).addSkill(
                    binding.skill.text.toString(),
                    1
                )
                viewModel.getRecommendation(binding.skill.text.toString())
                binding.skill.text.clear()
                Toast.makeText(context, "Skill added successfully", Toast.LENGTH_SHORT).show()
            }
            R.id.cancel_event -> {
                activity?.finish()
                //activity?.onBackPressed()
//                activity?.onBackPressedDispatcher?.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
//                    override fun handleOnBackPressed() {
//                        // Back is pressed... Finishing the activity
//                        activity?.finish()
//                    }
//                })
            }
            R.id.upload_resume -> {
                openResumerChooser()
            }
            R.id.save -> {
                if (emailRegex.matches(binding.linkedin.text.toString())) {
                    Toast.makeText(activity, "Please add the valid LinkedIn URL", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                if (profileSkills.isEmpty()) {
                    Toast.makeText(activity, "Please add at least one skill", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                viewModel.saveData(
                    SignUpDetail(
                        //0,
                        binding.linkedin.text.toString(),
                        "Asia/Calcutta",
                        //"",
                        profileSkills,
                        //"",
                        //"",
                        //listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
                    )
                )
            }
        }
    }

    override fun changeRating(level: Float, skill: String) {
        profileSkills[skill] = level.toInt()
        binding.skillList.adapter =
            SignUpSkillListAdapter(binding.root.context, profileSkills, this)
        Log.e("Skill Level Changed ", "$level")
        Log.e("Skill Level Changed", "$profileSkills")
    }

    override fun skillAdded(skill: String) {
        (binding.skillList.adapter as SignUpSkillListAdapter).addSkill(
            skill,
            1
        )
        binding.skillListContainer.visibility = View.VISIBLE
        viewModel.getRecommendation(skill)
    }

    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        TODO("Not yet implemented")
    }

    private fun getFileFromUri(uri: Uri): File? {
        if (uri.path == null) {
            return null
        }
        Log.e("resume1", " ${uri.path}")
        var realPath = String()
        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (uri.path!!.contains("/document/image:")) {
            Log.e("Check", "1")
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else {
            Log.e("Check", "2")
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            Log.e("Check", "3")
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
                    Log.e("Check", "5")
                    val columnIndex = cursor.getColumnIndexOrThrow(column)
                    realPath = cursor.getString(columnIndex)
                }
                cursor.close()
            }
        } catch (e: Exception) {
            Log.e("Check", "4")
            Log.i("GetFileUri Exception:", e.message ?: "")
        }
        val path = realPath.ifEmpty {
            Log.e("Check", "6")
            when {
                uri.path!!.contains("/document/raw:") -> uri.path!!.replace(
                    "/document/raw:",
                    ""
                )
                uri.path!!.contains("/document/primary:") -> uri.path!!.replace(
                    "/document/primary:",
                    "/storage/emulated/0/"
                )
                //else -> return null
                else -> uri.path!!
            }
        }
        return File(path)
    }

//    private fun openResumerChooser(){
//        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
//        val requestCode = 123
//
//        if (ContextCompat.checkSelfPermission(activity as Signup, permission) == PackageManager.PERMISSION_GRANTED) {
//            // Permission has already been granted
//            // Do your file operations here
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "application/pdf"
//            someActivityResultLauncher.launch(intent)
//        } else {
//            // Permission has not been granted
//            ActivityCompat.requestPermissions(activity as Signup, arrayOf(permission), requestCode)
//        }
//    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun openResumerChooser() {
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
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            someActivityResultLauncher.launch(intent)
            //startActivityForResult(intent, RESUME_PICKER)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri =
                    Uri.fromParts("package", binding.root.context.packageName, null)
                intent.data = uri
                startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(
                    activity as Signup, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    ), 1
                )
            }
        }
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.setType("application/*")
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        startActivityForResult(intent, RESUME_PICKER)

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == RESUME_PICKER) {
//            val uri = data?.data
//            if (uri != null) {
//                val path = getFileFromUri(uri)
//                viewModel.getResumeSkills(path)
//                binding.uploadResume.text=uri.path!!
//            }
//        }
//    }


}