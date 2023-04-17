package com.tft.selfbest.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.R
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


@AndroidEntryPoint
class ActivityLogFiltersDialog() : AppCompatActivity(), View.OnClickListener, ForCategories {
    lateinit var binding: FragmentActivityLogFiltersDialogBinding
    private val platforms = listOf("Mobile", "Web", "Desktop")
    private val durations = listOf("daily", "weekly", "monthly", "yearly", "custom")
    var selectedPlatform = "Mobile"
    var selectedDuration = "daily"
    var startDate = ""
    var endDate = ""
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    var custom_selected = false

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        // Inflate the layout for this fragment
        binding = FragmentActivityLogFiltersDialogBinding.inflate(layoutInflater)
        val sortedList = categories.sortedByDescending { it.duration }
        binding.givenCategories.layoutManager = LinearLayoutManager(binding.root.context)
        binding.givenCategories.adapter = SelectCategoryAdapter(
            binding.root.context,
            sortedList, getSelectedCategories()
        )
        setClickListeners()

        val spinAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_main_item_style,
            platforms
        )
        Log.e("Spinner", "Settled")
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        binding.platform.adapter = spinAdapter
        val spinAdapter1 = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_main_item_style,
            durations
        )
        spinAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_style)
        Log.e("Spinner2", "Settled")
        binding.duration.adapter = spinAdapter1

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
 }

    private fun setClickListeners() {
        binding.arrow.setOnClickListener(this)
        binding.applyFilter.setOnClickListener(this)
        binding.reset.setOnClickListener(this)
        binding.selectCategory.setOnClickListener(this)
    }


//    override fun getTheme(): Int {
//        return android.R.style.Theme_Black_NoTitleBar_Fullscreen
//    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.arrow -> {
                finish()
            }
            R.id.apply_filter -> {
                Log.e("Filters", selectedPlatform + " " + selectedDuration)
                if (custom_selected){
                    val intent = Intent(this,HomeActivity::class.java)
                    intent.putExtra("FRAGMENT", 1)
                    intent.putExtra("DETAILS", arrayOf(selectedPlatform, selectedDuration, startDate, endDate))
                    intent.putExtra("FIRST_TIME", false)
                    startActivity(intent)
                    finish()
                }
//                    transaction.replace(
//                        R.id.fragmentContainerView,
//                        ActivityLogFragment(getSelectedCategories(), selectedPlatform, selectedDuration, startDate, endDate, false)
//                    )
//                    applyFilterListener.filterData(
//                        selectedPlatform,
//                        selectedDuration,
//                        startDate,
//                        endDate
//                    )
                else{
                    val intent = Intent(this,HomeActivity::class.java)
                    intent.putExtra("Fragment", 1)
                    intent.putExtra("Details", arrayOf(getSelectedCategories(), selectedPlatform, selectedDuration, "", "", false))
                }
//                    transaction.replace(
//                        R.id.fragmentContainerView,
//                        ActivityLogFragment(getSelectedCategories(), selectedPlatform, selectedDuration, "", "", false)
//                    )
                //transaction.addToBackStack(null)

            }

            R.id.reset -> {
                binding.duration.setSelection(0)
                binding.platform.setSelection(0)
                applyFilterListener.filterData("Mobile", "daily", "", "")
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
        fun filterData(
            platform: String,
            duration: String,
            startDate: String,
            endDate: String
        )
    }
}