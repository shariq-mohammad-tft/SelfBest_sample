package com.tft.selfbest.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.databinding.FragmentActivityLogFiltersDialogBinding
import com.tft.selfbest.models.SelectedCategory
import com.tft.selfbest.ui.activites.HomeActivity
import com.tft.selfbest.ui.adapter.SelectCategoryAdapter
import com.tft.selfbest.ui.fragments.activityLog.ActivityLogFragment
import com.tft.selfbest.ui.fragments.activityLog.ForCategories
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class ActivityLogFiltersDialog(
    private val applyFilterListener: FilterListener,
    private val categories: List<SelectedCategory>,
) : Fragment(), View.OnClickListener, SelectCategoryAdapter.SelectionOfCategories {

    @Inject
    lateinit var preference: SelfBestPreference

    lateinit var binding: FragmentActivityLogFiltersDialogBinding
    private val platforms = listOf("Mobile", "Web", "Desktop")
    private val durations = listOf("daily", "weekly", "monthly", "custom")
    var selectedPlatform = "Mobile"
    var selectedDuration = "daily"
    var startDate = ""
    var endDate = ""
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    var custom_selected = false
    var selectedCategories = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as HomeActivity?)?.hideForFullScreen()
        binding = FragmentActivityLogFiltersDialogBinding.inflate(inflater)
        selectedPlatform = preference.selectedPlatform!!
        selectedDuration = preference.selectedDuration!!
        val sortedList = categories.sortedByDescending { it.duration }
        for(cat in sortedList.take(3)){
            selectedCategories.add(cat.category)
        }
        binding.givenCategories.layoutManager = LinearLayoutManager(binding.root.context)
        binding.givenCategories.adapter = SelectCategoryAdapter(
            binding.root.context,
            sortedList,
            this
        )
        setClickListeners()

        val spinAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_main_item_style,
            platforms
        )
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        binding.platform.adapter = spinAdapter
        val index = spinAdapter.getPosition(selectedPlatform)
        Log.e("Spinner", index.toString())
        binding.platform.setSelection(index)
        val spinAdapter1 = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_main_item_style,
            durations
        )
        spinAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_style)
        binding.duration.adapter = spinAdapter1
        val index1 = spinAdapter1.getPosition(selectedDuration)
        Log.e("Spinner2", index1.toString())
        binding.duration.setSelection(index1)

        binding.platform.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val selectedItem = binding.platform.selectedItem as String
                    selectedPlatform = selectedItem
                }
            }

        binding.duration.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val selectedItem = binding.duration.selectedItem as String
                    selectedDuration = selectedItem
                    if (selectedItem.equals("custom")) {
                        custom_selected = true
                        val dialog = Dialog(binding.root.context)
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setCancelable(false)
                        dialog.setContentView(R.layout.custom_date_dialog_layout)
                        val startDateContainer = dialog.findViewById(R.id.start_date) as TextView
                        val endDateContainer = dialog.findViewById(R.id.end_date) as TextView
                        val okBtn = dialog.findViewById(R.id.ok) as TextView
                        val cancelBtn = dialog.findViewById(R.id.cancel) as TextView
                        startDateContainer.setOnClickListener {
                            val c = Calendar.getInstance()

                            val year = c.get(Calendar.YEAR)
                            val month = c.get(Calendar.MONTH)
                            val day = c.get(Calendar.DAY_OF_MONTH)

                            val datePickerDialog =
                                DatePickerDialog(
                                    binding.root.context,
                                    { view, year, monthOfYear, dayOfMonth ->
                                        startDate = "${year}-${DecimalFormat("00").format(monthOfYear+1)}-${DecimalFormat("00").format(dayOfMonth)}"
                                        startDateContainer.text = startDate
                                    },
                                    year, month, day
                                )
                            datePickerDialog.show()
                        }
                        endDateContainer.setOnClickListener {
                            val c = Calendar.getInstance()

                            val year = c.get(Calendar.YEAR)
                            val month = c.get(Calendar.MONTH)
                            val day = c.get(Calendar.DAY_OF_MONTH)

                            val datePickerDialog =
                                DatePickerDialog(
                                    binding.root.context,
                                    { view, year, monthOfYear, dayOfMonth ->
                                        endDate = "${year}-${DecimalFormat("00").format(monthOfYear+1)}-${DecimalFormat("00").format(dayOfMonth)}"
                                        endDateContainer.setText(endDate)
                                    },
                                    year, month, day
                                )
                            datePickerDialog.show()
                        }
                        okBtn.setOnClickListener{
                            dialog.dismiss()
                        }
                        cancelBtn.setOnClickListener{
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }
            }

        return binding.root
    }

    private fun setClickListeners() {
        binding.arrow.setOnClickListener(this)
        binding.applyFilter.setOnClickListener(this)
        binding.reset.setOnClickListener(this)
        binding.selectCategory.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as HomeActivity?)?.showForFullScreen()
    }


//    override fun getTheme(): Int {
//        return android.R.style.Theme_Black_NoTitleBar_Fullscreen
//    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.arrow -> {
                Log.e("Filters", "Not Applied")
                requireActivity().supportFragmentManager.popBackStack()

            }
            R.id.apply_filter -> {
                Log.e("Filters", selectedPlatform + " " + selectedDuration)
                preference.setFilters(selectedDuration, selectedPlatform)
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
                val transaction = fragmentManager.beginTransaction()
                if (custom_selected) {
                    val bundle = Bundle()
                    bundle.putSerializable("Categories", ArrayList(selectedCategories))
                    val ALFragment = ActivityLogFragment(selectedPlatform!!, selectedDuration!!, startDate, endDate)
                    ALFragment.arguments = bundle
                    transaction.replace(
                        R.id.fragmentContainerView,
                        ALFragment
                        //ActivityLogFragment(getSelectedCategories(), selectedPlatform, selectedDuration, startDate, endDate, false)
                    )
                }
//                    applyFilterListener.filterData(
//                        selectedPlatform,
//                        selectedDuration,
//                        startDate,
//                        endDate
//                    )
                else{
                    val bundle = Bundle()
                    bundle.putSerializable("Categories", ArrayList(selectedCategories))
                    val ALFragment = ActivityLogFragment(selectedPlatform!!, selectedDuration!!, "", "")
                    ALFragment.arguments = bundle
                    transaction.replace(
                        R.id.fragmentContainerView,
                        ALFragment
                        //ActivityLogFragment(getSelectedCategories(), selectedPlatform, selectedDuration, startDate, endDate, false)
                    )
                }
                //transaction.addToBackStack(null)
                transaction.commit()
                //requireActivity().supportFragmentManager.popBackStack()
            }

            R.id.reset -> {
                binding.duration.setSelection(0)
                binding.platform.setSelection(0)
                selectedPlatform = "Mobile"
                selectedDuration = "daily"
                startDate = ""
                endDate = ""
            }

            R.id.select_category -> {
                if (binding.categories.isVisible) {
                    binding.categories.visibility = View.GONE
                } else {
                    binding.categories.visibility = View.VISIBLE
                }
            }
        }
    }


    interface FilterListener {
        fun filterData(platform: String, duration: String)
        fun filterData(
            platform: String,
            duration: String,
            startDate: String,
            endDate: String
        )
    }

    override fun selectCategory(category: SelectedCategory) {
        selectedCategories.add(category.category)
    }

    override fun deSelectCategory(category: SelectedCategory) {
        selectedCategories.remove(category.category)
    }
}