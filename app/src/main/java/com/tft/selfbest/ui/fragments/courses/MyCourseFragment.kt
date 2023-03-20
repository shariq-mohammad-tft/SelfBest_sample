package com.tft.selfbest.ui.fragments.courses

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.databinding.FragmentMyCourseBinding
import com.tft.selfbest.models.mycourse.AddCourse
import com.tft.selfbest.models.mycourse.FilterSearchCourse
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.DetailActivity
import com.tft.selfbest.ui.activites.HomeActivity
import com.tft.selfbest.ui.adapter.EnrolledCourseAdapter
import com.tft.selfbest.ui.adapter.SuggestedCourseAdapter
import com.tft.selfbest.ui.dialog.CourseFilterDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MyCourseFragment : Fragment(), CourseFilterDialog.ApplyFilterListener,
    SuggestedCourseAdapter.AddCourseListener, EnrolledCourseAdapter.UploadCertificateListener {
    val viewModel by viewModels<MyCourseViewModel>()
    lateinit var binding: FragmentMyCourseBinding
    lateinit var filterSearchCourse: FilterSearchCourse
    lateinit var courseId: String

    companion object {
        private const val IMAGE_PICK_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyCourseBinding.inflate(inflater, container, false)
        viewModel.getEnrolledCourses()
        viewModel.getSuggestedCourses()
        binding.currentCourseList.layoutManager = LinearLayoutManager(context)
        binding.suggestedCourseList.layoutManager = LinearLayoutManager(context)
        binding.searchCourseList.layoutManager = LinearLayoutManager(context)
        searchBarListener()
        viewModel.enrolledCourseObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.currentCourseList.adapter =
                    it.data?.course?.let { enrolledCourseList ->
                        EnrolledCourseAdapter(
                            enrolledCourseList, this
                        )
                    }
            }
        }
        viewModel.suggestedCourseObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.suggestedCourseList.adapter = it.data?.course?.let { suggestedCourseList ->
                    SuggestedCourseAdapter(
                        suggestedCourseList, this
                    )
                }
            }
        }
        viewModel.searchResultCourseObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.progress.visibility = View.GONE
                binding.searchCourseContainer.visibility = View.VISIBLE
                binding.searchCourseList.adapter = it.data?.let { searchCourseList ->
                    SuggestedCourseAdapter(
                        searchCourseList, this
                    )
                }
            }
        }
        viewModel.addCourseObserver.observe(viewLifecycleOwner) {
            Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
        }
        val durationLength = ArrayList<String>()
        durationLength.add("any")
        val providers = ArrayList<String>()
        providers.add("Udemy")
        providers.add("Youtube")

        filterSearchCourse = FilterSearchCourse(
            "",
            durationLength,
            0,
            10000,
            providers
        )
        binding.filterContainer.setOnClickListener(View.OnClickListener {
            CourseFilterDialog(filterSearchCourse, this).show(
                (activity as HomeActivity).supportFragmentManager,
                "CourseFilterDialog"
            )
        })
        return binding.root
    }

    private fun searchBarListener() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    filterSearchCourse.Keyword = query.toString()
                    binding.progress.visibility = View.VISIBLE
                    viewModel.getSearchCourseResult(filterSearchCourse)
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isNullOrEmpty()) {
                    binding.searchCourseContainer.visibility = View.GONE
                    return false
                }
                return true
            }
        })
    }

    override fun filterData(filterSearchCourse: FilterSearchCourse) {
        this.filterSearchCourse = filterSearchCourse
        viewModel.getSearchCourseResult(filterSearchCourse)
    }

    override fun getAddedCourse(addCourse: AddCourse) {
        viewModel.addCourse(addCourse)
    }

    override fun uploadCourseCertificate(courseId: String) {
        this.courseId = courseId
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
            startActivityForResult(intent, IMAGE_PICK_CODE)
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
                    activity as DetailActivity, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    ), 1
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            if (uri != null) {
                val imagePath = getFileFromUri(uri)
                viewModel.uploadCertificate(imagePath, courseId)
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
        val path = if (realPath.isNotEmpty()) realPath else {
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
}