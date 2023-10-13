package com.tft.selfbest.ui.fragments.overview

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.internal.LinkedTreeMap
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentShareWithManagerBinding
import com.tft.selfbest.models.ManagerEmails
import com.tft.selfbest.models.ObservationDetail
import com.tft.selfbest.models.ObservationsResponse
import com.tft.selfbest.models.SelectedTimeSheetData
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.ActivitiesPerformedListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShareWithManagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ShareWithManagerFragment : Fragment(), View.OnClickListener {
    lateinit var pieChart: PieChart
    lateinit var binding: FragmentShareWithManagerBinding
    val viewModel by viewModels<ShareWithManagerViewModel>()
    val startDateCal = Calendar.getInstance()
    val endDateCal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    lateinit var managersData: List<ManagerEmails>
    lateinit var startDate: String
    lateinit var endDate: String
    lateinit var observationsData: ObservationsResponse
    lateinit var emailList: List<String>
    lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShareWithManagerBinding.inflate(layoutInflater)
        pieChart = binding.pieChart
        startDate = dateFormat.format(startDateCal.time)
        endDate = dateFormat.format(endDateCal.time)
        binding.startDate.text = startDate
        binding.endDate.text = endDate
        setUpPieChart()
        setAllClickListener()
        viewModel.getObservationsData(startDate, endDate)
        viewModel.showMessageObserver.observe(viewLifecycleOwner) {
            Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
        }
        viewModel.observationsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                observationsData = it.data!!
                emailList = observationsData.managers
                val adapter = ArrayAdapter(
                    binding.root.context,
                    android.R.layout.simple_list_item_1,
                    emailList
                )
                binding.mailContainer.setAdapter(adapter)
                binding.mailContainer.threshold = 1
                setUIData(observationsData)
                loadPieChartData(observationsData.categories)
                setAdapter(observationsData.observations)
            }
        }
        return binding.root
    }

    private fun setUpPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(8f)
        pieChart.setDrawEntryLabels(false)
        pieChart.setEntryLabelColor(R.color.black)
        pieChart.description.isEnabled = false
        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.yEntrySpace = 4f
        legend.formToTextSpace = 4f
        legend.setDrawInside(false)
        legend.textSize = 8.0f
        legend.isEnabled = true

    }

    private fun loadPieChartData(category: Any) {
        if (category.toString() == "{}")
            return
        val categoryMap = category as LinkedTreeMap<String, Float>
        val entries = ArrayList<PieEntry>()
        for (entry in categoryMap.entries) {
            entries.add(PieEntry(entry.value, entry.key))
        }
        /*   entries.add(PieEntry(0.70f, "DocumentationTime"))
        entries.add(PieEntry(0.10f, "LearningTime"))
        entries.add(PieEntry(0.10f, "NeutralTime"))
        entries.add(PieEntry(0.15f, "CodeTime"))*/

        val colors = ArrayList<Int>()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(7.2f)
        data.setValueTextColor(R.color.black)
        pieChart.data = data
        pieChart.invalidate()
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun setAdapter(observationList: List<ObservationDetail>) {
        if (observationList == null)
            return
        binding.performedList.layoutManager = LinearLayoutManager(binding.root.context)
        binding.performedList.adapter = ActivitiesPerformedListAdapter(
            binding.root.context,
            observationList
        )
    }

    private fun setAllClickListener() {
        binding.startDateContainer.setOnClickListener(this)
        binding.endDateContainer.setOnClickListener(this)
        binding.sendData.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.start_date_container -> {
                datePickerDialog = DatePickerDialog(
                    binding.root.context,
                    object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(
                            p0: DatePicker?, year: Int, monthOfYear: Int,
                            dayOfMonth: Int
                        ) {
                            startDateCal.set(Calendar.YEAR, year)
                            startDateCal.set(Calendar.MONTH, monthOfYear)
                            startDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            startDate = dateFormat.format(startDateCal.time)
                            binding.startDate.text = startDate
                            viewModel.getObservationsData(startDate, endDate)
                        }
                    },
                    startDateCal.get(Calendar.YEAR),
                    startDateCal.get(Calendar.MONTH),
                    startDateCal.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
            }
            R.id.end_date_container -> {
                datePickerDialog = DatePickerDialog(
                    binding.root.context,
                    object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(
                            p0: DatePicker?, year: Int, monthOfYear: Int,
                            dayOfMonth: Int
                        ) {
                            endDateCal.set(Calendar.YEAR, year)
                            endDateCal.set(Calendar.MONTH, monthOfYear)
                            endDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            endDate = dateFormat.format(endDateCal.time)
                            binding.endDate.text = endDate
                            viewModel.getObservationsData(startDate, endDate)
                        }
                    },
                    endDateCal.get(Calendar.YEAR),
                    endDateCal.get(Calendar.MONTH),
                    endDateCal.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
            }
            R.id.send_data -> {
                var managerMail = binding.mailContainer.text.toString()
                if (managerMail.isEmpty()) {
                    Toast.makeText(
                        binding.root.context,
                        "Please select email first",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                val selectedList =
                    (binding.performedList.adapter as ActivitiesPerformedListAdapter).getAllSelectedList()
                val selectedData = SelectedTimeSheetData(
                    endDate,
                    managerMail,
                    selectedList,
                    "",
                    startDate
                )
                viewModel.sendTimeSheetToManager(selectedData)
            }
        }
    }

    private fun setUIData(observationsResponse: ObservationsResponse) {
        binding.pieChartLevel.text = "Current level - ${observationsResponse.level}"
        binding.completedTaskLevel.text = "Current level - ${observationsResponse.level}"
        binding.noOfLearnTask.text = "Learnt ${observationsResponse.taskCompleted} number of task"
        binding.totalNoOfCompletedTask.text = observationsResponse.taskCompleted.toString()
        val focusHour = observationsResponse.categories
        if (observationsResponse.categories.toString() == "{}")
            return
        val categoryMap = observationsResponse.categories as LinkedTreeMap<String, Double>
        val focusTime =
            categoryMap["CodeTime"] ?: 0.0 + (categoryMap["DocumentationTime"]
                ?: 0.0) + (categoryMap["LearningTime"] ?: 0.0)
        binding.pieChartFocusHour.text =
            "${(focusTime / 3600).roundToInt()} hours of focus work done!"
    }
}