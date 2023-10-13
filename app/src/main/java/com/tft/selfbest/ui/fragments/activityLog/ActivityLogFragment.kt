package com.tft.selfbest.ui.fragments.activityLog

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.databinding.FragmentActivityLogBinding
import com.tft.selfbest.models.DefaultCategory
import com.tft.selfbest.models.QueryAnsweredResponse
import com.tft.selfbest.models.QueryResponse
import com.tft.selfbest.models.SelectedCategory
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.*
import com.tft.selfbest.ui.dialog.ActivityLogFiltersDialog
import com.tft.selfbest.ui.fragments.statistics.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ActivityLogFragment(
    var selectedPlatform: String,
    var selectedDuration: String,
    var startDate1: String,
    var endDate1: String,
) : Fragment(), View.OnClickListener,
    QueryResponseAdapter.ChangeStatusListener,
    AnsweredQueryAdapter.ChangeStatusListener,
    CategoryAdapter.PieChartListener {

    @Inject
    lateinit var pref: SelfBestPreference

    lateinit var binding: FragmentActivityLogBinding
    val viewModel by viewModels<StatisticsViewModel>()
    var categories: List<SelectedCategory> = listOf()
    var categoriesToDisplay: MutableList<SelectedCategory> = mutableListOf()
    lateinit var startDate: String
    var focusTime: Double = 0.0
    val startDateCal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    lateinit var adapter: CategoryAdapter
    var totalTime = 0.0
    lateinit var queries: List<QueryResponse>
    var queries1: MutableList<QueryResponse> = mutableListOf()

    lateinit var answeredQueries: List<QueryAnsweredResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentActivityLogBinding.inflate(layoutInflater)
        startDate = dateFormat.format(startDateCal.time)
//        setRadarChart()
        setClickListeners()
        binding.fabFilter.text = selectedPlatform
        binding.fabFilter.extend()
        setupChart()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.fabFilter.shrink()
        }, 4000)
        //sample data
//        categories = listOf(
//            SelectedCategory("Development", false, 1.43),
//            SelectedCategory("Documentation", true, 521.577),
//            SelectedCategory("Management & Planning", true, 78.9),
//            SelectedCategory("Designing", true, 940.0),
//            SelectedCategory("Marketing & Sales", false, 379.0),
//            SelectedCategory("Collaboration", false, 39.0)
//        )

        if (pref.showWorkDen) {
            binding.workDen.visibility = View.VISIBLE
        } else {
            binding.workDen.visibility = View.GONE
            binding.workDenContainer.visibility = View.GONE
            binding.solutionPointContainer.visibility = View.VISIBLE
            binding.solutionPoint.setTextColor(Color.parseColor("#FFFFFF"))
            binding.solutionPoint.setBackgroundResource(R.drawable.work_den_bg)
        }
        if (pref.showSolutionPt) {
            binding.solutionPoint.visibility = View.VISIBLE
        } else {
            binding.solutionPoint.visibility = View.GONE
        }
        if (selectedDuration.equals("custom")) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val start = inputFormat.parse(startDate1)
            val end = inputFormat.parse(endDate1)
            viewModel.getQuery(
                dateFormat.format(start!!),
                dateFormat.format(end!!),
                selectedDuration
            )
        } else {
            viewModel.getQuery(startDate, "", selectedDuration)
        }
        viewModel.queryObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.hoursSavedSp.text = it.data!!.hours_saved
                binding.askedQueries.layoutManager = LinearLayoutManager(context)
                queries1.clear()
                queries = it.data.query_data
                for (query in queries) {
                    if (query.question != "")
                        queries1.add(query)
                }
                if (queries1.isNotEmpty()) {
                    binding.askedQueries.visibility = View.VISIBLE
                    binding.noAskedQueries.visibility = View.GONE
                } else {
                    binding.askedQueries.visibility = View.GONE
                    binding.noAskedQueries.visibility = View.VISIBLE
                    binding.animationView.playAnimation()
                }

                binding.askedQueries.layoutManager = LinearLayoutManager(binding.root.context)
                binding.askedQueries.adapter = QueryResponseAdapter(
                    binding.root.context,
                    queries1,
                    this
                )
            }
        }

        if (selectedDuration.equals("custom")) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val start = inputFormat.parse(startDate1)
            val end = inputFormat.parse(endDate1)
            viewModel.getAnsweredQuery(
                "",
                dateFormat.format(start!!),
                dateFormat.format(end!!),
                selectedDuration
            )
        } else {
            viewModel.getAnsweredQuery("", startDate, "", selectedDuration)
        }
        viewModel.queryAnsweredObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                answeredQueries = it.data!!
                //if(answeredQueries.size > 10){
                //binding.loadMoreBtn.visibility = View.VISIBLE
                if (answeredQueries.isNotEmpty()) {
                    binding.answeredQueries.visibility = View.VISIBLE
                    binding.noAnsweredQueries.visibility = View.GONE
                } else {
                    binding.answeredQueries.visibility = View.GONE
                    binding.noAnsweredQueries.visibility = View.VISIBLE
                    binding.animationView1.playAnimation()
                }
                binding.answeredQueries.layoutManager = LinearLayoutManager(binding.root.context)
                binding.answeredQueries.adapter = AnsweredQueryAdapter(
                    binding.root.context,
                    answeredQueries,
                    this
                )
            }
        }
//        val adapter = activity?.let {
//            ViewPagerQueryAdapter(
//                it.supportFragmentManager,
//                lifecycle,
//                "",
//                "",
//                ""
//            )
//        }
//        binding.viewpager.adapter = adapter
//        binding.tabLayout.tabMode = TabLayout.MODE_FIXED
//
//        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
//            when (position) {
//                0 -> {
//                    tab.text = "Asked"
//                }
//                1 -> {
//                    tab.text = "Answered"
//                }
//                else -> {
//                    tab.text = "Asked"
//                }
//            }
//
//        }.attach()


        if (selectedDuration.equals("custom")) {
            viewModel.getLogs(
                selectedPlatform,
                selectedDuration,
                convertDate(startDate1),
                convertDate(endDate1)
            )
        } else
            viewModel.getLogs(selectedPlatform, selectedDuration)
        viewModel.activityLogsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success && it.data != null) {
                binding.progress.visibility = View.GONE

                binding.hoursSaved.text = getTimeInFormat(it.data.timeSaved)
                binding.distractedTime.text = getTimeInFormat(it.data.distractedTime)

                binding.breakTime.text = getTimeInFormat(it.data.defaultList.breakTime)
                binding.others.text = getTimeInFormat(it.data.defaultList.others)
                binding.distractionTime1.text = getTimeInFormat(it.data.defaultList.distraction)

                if (it.data.selectedCategories != null) {
                    categories = it.data.selectedCategories
                    Log.e("Categories", categories.toString())
                }

                focusTime = 0.0
                Log.e("Categories to display", categoriesToDisplay.toString())
                for (cat in categories) {
                    focusTime += cat.duration
                }
                setPieCharts(it.data.defaultList, categories)
                binding.focusedCategories.layoutManager = LinearLayoutManager(requireContext())
                adapter = CategoryAdapter(
                    binding.root.context,
                    categories,
                    this
                )
                binding.focusedCategories.adapter = adapter
                binding.timeSaved.text = getTimeInFormat(focusTime)

            } else if (it is NetworkResponse.Error) {
                binding.progress.visibility = View.GONE
                Toast.makeText(
                    context,
                    "Your connection is not stable or try to logout/login",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }

    private fun convertDate(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        inputFormat.timeZone = utcTimeZone
        val date = inputFormat.parse(dateStr)

        outputFormat.timeZone = utcTimeZone
        return outputFormat.format(date!!)
    }

    private fun setupChart() {
        binding.outerPieChart.description.isEnabled = false
//        binding.innerPieChart.description.isEnabled = false

        binding.outerPieChart.setCenterTextSize(10f)
//        binding.innerPieChart.setCenterTextSize(10f)

        binding.outerPieChart.holeRadius = 70f
//        binding.innerPieChart.holeRadius = 78f

        binding.outerPieChart.transparentCircleRadius = 50f
//        binding.innerPieChart.transparentCircleRadius = 50f

        binding.outerPieChart.legend.isEnabled = false
//        binding.innerPieChart.legend.isEnabled = false

        binding.outerPieChart.isRotationEnabled = false
        binding.outerPieChart.rotationAngle = 0f

//        binding.innerPieChart.isRotationEnabled = false
//        binding.innerPieChart.rotationAngle = 0f
    }

    private fun setPieCharts(
        defaultCategories: DefaultCategory,
        focusedCategories: List<SelectedCategory>
    ) {
        totalTime = 0.0
        val entries = ArrayList<PieEntry>()

        var focusedTime = 0.0
        var breakTime = 0.0

        entries.add(PieEntry(defaultCategories.others.toFloat(), "Others"))
        breakTime += defaultCategories.others
        entries.add(PieEntry(defaultCategories.distraction.toFloat(), "Distraction"))
        breakTime += defaultCategories.distraction
        entries.add(PieEntry(defaultCategories.breakTime.toFloat(), "Break"))
        breakTime += defaultCategories.breakTime

        for (i in focusedCategories) {
            entries.add(PieEntry(i.duration.toFloat(), i.category))
            focusedTime += i.duration
        }
//        if(focusTime == 0.0){
//            entries.add(PieEntry(1f, "No Data"))
//        }

        val dataSet = PieDataSet(entries, "Focused Categories")

        dataSet.setDrawIcons(false)

        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors = arrayListOf(
            Color.parseColor("#44445A"),
            Color.parseColor("#72727C"),
            Color.parseColor("#9B9BA0"),
            Color.parseColor("#7FC7BC"),
            Color.parseColor("#41AC9C"),
            Color.parseColor("#007BB6"),
            Color.parseColor("#0059C1"),
            Color.parseColor("#AA95DB"),
            Color.parseColor("#FFE0CF"),
            Color.parseColor("#FFC43D"),
            Color.parseColor("#00A2A4")
        )
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.TRANSPARENT)

//        val entries1 = ArrayList<PieEntry>()
//
//        entries1.add(PieEntry(defaultCategories.others.toFloat(), "Others"))
//        breakTime += defaultCategories.others
//        entries1.add(PieEntry(defaultCategories.distraction.toFloat(), "Distraction"))
//        breakTime += defaultCategories.distraction
//        entries1.add(PieEntry(defaultCategories.breakTime.toFloat(), "Break"))
//        breakTime += defaultCategories.breakTime
//
//        val dataSet1 = PieDataSet(entries1, "Default categories")
//
//        dataSet1.setDrawIcons(false)
//
//        dataSet1.iconsOffset = MPPointF(0f, 40f)
//        dataSet1.selectionShift = 5f
//
//        val colors1 = arrayListOf(
//            Color.parseColor("#44445A"),
//            Color.parseColor("#9B9BA0"),
//            Color.parseColor("#72727C"))
//        dataSet1.colors = colors1
//
//        val data1 = PieData(dataSet1)
//        data1.setValueFormatter(PercentFormatter())
//        data1.setValueTextSize(11f)
//        data1.setValueTextColor(Color.TRANSPARENT)

        binding.outerPieChart.data = data
//        binding.innerPieChart.data = data1

        binding.outerPieChart.highlightValues(null)
//        binding.innerPieChart.highlightValues(null)

//        binding.innerPieChart.setDrawEntryLabels(false)
        binding.outerPieChart.setDrawEntryLabels(false)


        if (focusedTime == 0.0 && breakTime == 0.0) {
            binding.categoryDuration.text = "No Data Available"
        } else {
            totalTime = focusedTime + breakTime
            binding.categoryName.text = "Total Time"
            binding.categoryDuration.text = getTimeInFormat(totalTime)
        }
//        binding.innerPieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
//            override fun onValueSelected(entry: Entry, highlight: Highlight) {
//                binding.outerPieChart.setTouchEnabled(false)
//                Log.e("Inner Pie Chart", entry.toString())
//                val value1 = entry.y
//                val label1 = (entry as PieEntry).label
////                val label = entry.
//
//                val total1 = binding.innerPieChart.data.yValueSum
//                val percentage1 = (value1 / total1) * 100
//
//                val valuePercentageString1 = String.format("%.2f%%", percentage1)
//
//                binding.categoryDuration.text = valuePercentageString1
//                binding.categoryName.text = label1
//            }
//
//            override fun onNothingSelected() {
//                binding.outerPieChart.setTouchEnabled(true)
//                binding.categoryDuration.text = ""
//                binding.categoryName.text = ""
//                binding.categoryName.text = "Total Time"
//                binding.categoryDuration.text = getTimeInFormat(totalTime)
//            }
//        })

        binding.outerPieChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry, highlight: Highlight) {
//                binding.innerPieChart.setTouchEnabled(false)
                Log.e("Outer Pie Chart", entry.toString())
                val value = entry.y
                val label = (entry as PieEntry).label
//                val label = entry.
                if (!label.equals("Others") && !label.equals("Distraction") && !label.equals("Break")) {
                    focusOnView()
                    val position: Int = adapter.getItemPosition(label)
                    Log.e("Activity Log Out", position.toString())
                    if (position >= 0 && position < adapter.itemCount) {
                        binding.focusedCategories.smoothScrollToPosition(position)
                        adapter.openInsightSection(position)
                        Log.e("Activity Log In", position.toString())
                    }
                }
                binding.categoryDuration.text = getTimeInFormat(value.toDouble())
                binding.categoryName.text = label
            }

            override fun onNothingSelected() {
//                binding.innerPieChart.setTouchEnabled(true)
                binding.categoryDuration.text = ""
                binding.categoryName.text = "Total Time"
                binding.categoryDuration.text = getTimeInFormat(totalTime)
                adapter.closeInsightSection(-1)
            }
        })

        binding.outerPieChart.invalidate()
//        binding.innerPieChart.invalidate()

    }

    private fun focusOnView() {
        binding.rootLayout.post { binding.rootLayout.scrollTo(0, binding.focusedCategories.bottom) }
    }

    private fun setClickListeners() {
        binding.workDen.setOnClickListener(this)
        binding.solutionPoint.setOnClickListener(this)
        binding.fabFilter.setOnClickListener(this)
        binding.asked.setOnClickListener(this)
        binding.answered.setOnClickListener(this)
    }

    /*private fun setRadarChart(list: ArrayList<RadarEntry>, labels: List<String>) {
        //binding.RadarChart.setBackgroundColor(Color.parseColor("#accbff"))
        binding.RadarChart.webLineWidth = 2f
        binding.RadarChart.webColor = Color.BLUE
        //binding.RadarChart.webLineWidthInner = 2f
        binding.RadarChart.webColorInner = Color.BLACK
        binding.RadarChart.webAlpha = 100
        val radarDataSet = RadarDataSet(list, "List")
//        radarDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        radarDataSet.color = Color.rgb(0, 255, 0)
        radarDataSet.fillColor = Color.rgb(0, 255, 0)
        radarDataSet.setDrawFilled(true)
        //radarDataSet.highLightColor = Color.rgb(0, 255, 0);
        radarDataSet.lineWidth = 2f
        radarDataSet.valueTextColor = Color.RED
        radarDataSet.valueTextSize = 14f
        radarDataSet.setDrawValues(false)
        radarDataSet.setDrawVerticalHighlightIndicator(true)
        //radarDataSet.setDrawCircles(true)

        val radarData = RadarData()
        radarData.addDataSet(radarDataSet)

//        val labels = listOf("Documenting", "Coding", "Break", "Distraction", "Other")
        val xAxis = binding.RadarChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.textSize = 12f

        binding.RadarChart.data = radarData
        //binding.RadarChart.getAxisLeft().setDrawLabels(false);
//        binding.RadarChart.highlightValue(Highlight(1f,0, 0))
        binding.RadarChart.yAxis.setDrawLabels(false);
        //binding.RadarChart.getXAxis().setDrawLabels(false);
        binding.RadarChart.description.text = ""
        binding.RadarChart.legend.isEnabled = false
        binding.RadarChart.animateY(2000)
    }
//    private fun setRadarChart(list: ArrayList<RadarEntry>, labels: List<String>) {
//        //binding.RadarChart.setBackgroundColor(Color.parseColor("#accbff"))
//        binding.RadarChart.webLineWidth = 2f
//        binding.RadarChart.webColor = Color.BLUE
//        //binding.RadarChart.webLineWidthInner = 2f
//        binding.RadarChart.webColorInner = Color.BLACK
//        binding.RadarChart.webAlpha = 100
//        val radarDataSet = RadarDataSet(list, "List")
////        radarDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
//        radarDataSet.color = Color.rgb(0, 255, 0)
//        radarDataSet.fillColor = Color.rgb(0, 255, 0)
//        radarDataSet.setDrawFilled(true)
//        radarDataSet.setDrawHighlightIndicators(true) // Enable highlight indicators
//        radarDataSet.highLightColor = Color.RED // Set highlight color
//        radarDataSet.setHighlightCircleFillColor(Color.RED) // Set highlight circle fill color
//        radarDataSet.setHighlightCircleStrokeColor(Color.BLACK) // Set highlight circle stroke color
//        radarDataSet.setHighlightCircleStrokeWidth(12f) // Set highlight circle stroke width
//        radarDataSet.lineWidth = 2f
//        radarDataSet.valueTextColor = Color.RED
//        radarDataSet.valueTextSize = 14f
//        radarDataSet.setDrawValues(false)
//        radarDataSet.setDrawVerticalHighlightIndicator(true)
//        //radarDataSet.setDrawCircles(true)
//
//        val radarData = RadarData()
//        radarData.addDataSet(radarDataSet)
//        val xAxis = binding.RadarChart.xAxis
//        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
//        xAxis.textSize = 12f
//
//        binding.RadarChart.data = radarData
//        binding.RadarChart.yAxis.setDrawLabels(false);
//        binding.RadarChart.description.text = ""
//        binding.RadarChart.legend.isEnabled = false
//        binding.RadarChart.animateY(2000)
//    }*/


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.work_den -> {
                binding.solutionPointContainer.visibility = View.GONE
                binding.workDenContainer.visibility = View.VISIBLE
                binding.workDen.setTextColor(Color.parseColor("#FFFFFF"))
                binding.solutionPoint.setTextColor(Color.parseColor("#C8C8C8"))
                binding.workDen.setBackgroundResource(R.drawable.work_den_bg)
                binding.solutionPoint.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding.fabFilter.visibility = View.VISIBLE
            }

            R.id.solution_point -> {
                binding.workDenContainer.visibility = View.GONE
                binding.solutionPointContainer.visibility = View.VISIBLE
                binding.solutionPoint.setTextColor(Color.parseColor("#FFFFFF"))
                binding.workDen.setTextColor(Color.parseColor("#C8C8C8"))
                binding.workDen.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding.solutionPoint.setBackgroundResource(R.drawable.work_den_bg)
            }

            R.id.asked -> {
                binding.asked.setTextColor(Color.parseColor("#3E3E3E"))
                binding.asked.setTypeface(null, Typeface.BOLD)
                binding.askedQueryBox.visibility = View.VISIBLE
                binding.answeredQueryBox.visibility = View.GONE

                binding.answered.setTextColor(Color.parseColor("#707070"))
                binding.answered.setTypeface(null, Typeface.NORMAL)

                binding.askedLine.setBackgroundColor(Color.parseColor("#1d71d4"))
                binding.answeredLine.setBackgroundColor(Color.TRANSPARENT)

            }

            R.id.answered -> {
                binding.answered.setTextColor(Color.parseColor("#3E3E3E"))
                binding.answered.setTypeface(null, Typeface.BOLD)
                binding.askedQueryBox.visibility = View.GONE
                binding.answeredQueryBox.visibility = View.VISIBLE

                binding.asked.setTextColor(Color.parseColor("#707070"))
                binding.asked.setTypeface(null, Typeface.NORMAL)

                binding.answeredLine.setBackgroundColor(Color.parseColor("#00A2A4"))
                binding.askedLine.setBackgroundColor(Color.TRANSPARENT)

            }

//            R.id.select_cat_container -> {
//                if (binding.categoryContainer.isVisible) {
//                    binding.categoryContainer.visibility = View.GONE
//                } else {
//                    binding.categoryContainer.visibility = View.VISIBLE
//                }
//            }

            R.id.fab_filter -> {
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(
                    R.id.fragmentContainerView,
                    ActivityLogFiltersDialog()
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }

    override fun changeStatus(id: Int, status: Int) {
        Log.e("Status ", "2")
        viewModel.updateStatus(id, status)
    }

    override fun updateRating(id: Int, rating: Int) {
        viewModel.updateRating(id, rating)
    }

    private fun getTimeInFormat(time: Double): String {
        val ans: String
        val hours = time.toInt() / 3600
        val remainder = time.toInt() - (hours * 3600)
        val mins = remainder / 60
        val formatter = DecimalFormat("00");
        ans = "${formatter.format(hours)}h : ${formatter.format(mins)}m"
        return ans
    }

    override fun changeRelevance(id: Int, relevance: Int) {
        viewModel.updateRelevance(id, relevance, "webbot")
    }

    override fun highlight(position: Int, label : String, duration: Double) {
        val dataSet = binding.outerPieChart.data.getDataSetByIndex(0) as? IPieDataSet
        if (dataSet != null) {
            val highlight = Highlight(position.toFloat(), 0f, 0)
            binding.outerPieChart.highlightValue(highlight)
            binding.categoryDuration.text = getTimeInFormat(duration.toDouble())
            binding.categoryName.text = label
        }
    }

    override fun unhighlight() {
        binding.outerPieChart.highlightValue(null)
        binding.categoryDuration.text = getTimeInFormat(totalTime)
        binding.categoryName.text = "Total Time"
    }
}
/*
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentActivityLogBinding
import com.tft.selfbest.models.SelectedCategory
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.*
import com.tft.selfbest.ui.dialog.ActivityLogFiltersDialog
import com.tft.selfbest.ui.fragments.statistics.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
@AndroidEntryPoint
class ActivityLogFragment(
    override var defaultCategories: MutableList<SelectedCategory>,
    var selectedPlatform: String,
    var selectedDuration: String,
    var startDate1: String,
    var endDate1: String,
    var firstTime: Boolean
) :
    Fragment(), View.OnClickListener,
    QueryResponseAdapter.ChangeStatusListener, ActivityLogFiltersDialog.FilterListener,
    AnsweredQueryAdapter.ChangeStatusListener, ForCategories {
    lateinit var binding: FragmentActivityLogBinding
    val viewModel by viewModels<StatisticsViewModel>()
    var categories: List<SelectedCategory> = listOf()
    lateinit var startDate: String
    val startDateCal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    //var selectedPlatform = "Mobile"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivityLogBinding.inflate(layoutInflater)
        startDate = dateFormat.format(startDateCal.time)
//        setRadarChart()
        setClickListeners()
        binding.fabFilter.text = selectedPlatform
        binding.fabFilter.extend()
        binding.RadarChart.setTouchEnabled(false)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.fabFilter.shrink()
        }, 4000)
        //sample data
//        categories = listOf(
//            SelectedCategory("Development", false, 1.43),
//            SelectedCategory("Documentation", true, 521.577),
//            SelectedCategory("Management & Planning", true, 78.9),
//            SelectedCategory("Designing", true, 940.0),
//            SelectedCategory("Marketing & Sales", false, 379.0),
//            SelectedCategory("Collaboration", false, 39.0)
//        )
        viewModel.queryObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.hoursSavedSp.text = it.data!!.hours_saved
            }
        }
        val adapter = activity?.let { ViewPagerQueryAdapter(it.supportFragmentManager, lifecycle) }
        binding.viewpager.adapter = adapter
        binding.tabLayout.tabMode = TabLayout.MODE_FIXED
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Asked"
                }
                1 -> {
                    tab.text = "Answered"
                }
            }
        }.attach()
        if(selectedDuration.equals("custom"))
            viewModel.getLogs(selectedPlatform, selectedDuration, startDate1, endDate1)
        else
            viewModel.getLogs(selectedPlatform, selectedDuration)
        viewModel.activityLogsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                val list: ArrayList<RadarEntry> = ArrayList()
                val labels: MutableList<String> = mutableListOf()
                binding.hoursSaved.text = getTimeInFormat(it.data!!.focusTime)
                binding.timeSaved.text = getTimeInFormat(it.data.timeSaved)
                binding.distractedTime.text = getTimeInFormat(it.data.distractedTime)
                binding.breakTime.text = getTimeInFormat(it.data.defaultList.breakTime)
                binding.others.text = getTimeInFormat(it.data.defaultList.others)
                binding.distractionTime1.text = getTimeInFormat(it.data.defaultList.distraction)
                list.add(RadarEntry(it.data.defaultList.breakTime.toFloat()))
                labels.add("Break")
                list.add(RadarEntry(it.data.defaultList.others.toFloat()))
                labels.add("Others")
                list.add(RadarEntry(it.data.defaultList.distraction.toFloat()))
                labels.add("Distraction")
                categories = it.data.selectedCategories
                Log.e("Default Categories 0", getSelectedCategories().toString())
                if (getSelectedCategories().isEmpty() && firstTime) {
                    val a = categories.sortedByDescending { it -> it.duration }
                        .take(3) as MutableList<SelectedCategory>
                    setSelectedCategories(categories.sortedByDescending { it -> it.duration }
                        .take(3) as MutableList<SelectedCategory>)
                    Log.e("Default Categories 0.1", a.toString())
                }
                for (cat in getSelectedCategories()) {
                    list.add(RadarEntry(cat.duration.toFloat()))
                    labels.add(cat.category.substring(0, 10))
                }
                setRadarChart(list, labels)
                binding.categoryList.layoutManager = LinearLayoutManager(requireContext())
                Log.e("Selected Categories", getSelectedCategories().toString())
                binding.categoryList.adapter = CategoryAdapter(
                    binding.root.context,
                    getSelectedCategories().sortedByDescending { it -> it.duration }
                )
            }
        }
        return binding.root
    }
    private fun setClickListeners() {
        binding.workDen.setOnClickListener(this)
        binding.solutionPoint.setOnClickListener(this)
        //binding.selectCatContainer.setOnClickListener(this)
        binding.fabFilter.setOnClickListener(this)
    }
    private fun setRadarChart(list: ArrayList<RadarEntry>, labels: List<String>) {
        //binding.RadarChart.setBackgroundColor(Color.parseColor("#accbff"))
        binding.RadarChart.webLineWidth = 2f
        binding.RadarChart.webColor = Color.BLUE
        //binding.RadarChart.webLineWidthInner = 2f
        binding.RadarChart.webColorInner = Color.BLACK
        binding.RadarChart.webAlpha = 100
//        list.add(RadarEntry(100f))
//        list.add(RadarEntry(101f))
//        list.add(RadarEntry(102f))
//        list.add(RadarEntry(103f))
//        list.add(RadarEntry(104f))
        val radarDataSet = RadarDataSet(list, "List")
//        radarDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        radarDataSet.color = Color.rgb(0, 255, 0)
        radarDataSet.fillColor = Color.rgb(0, 255, 0)
        radarDataSet.setDrawFilled(true)
        //radarDataSet.highLightColor = Color.rgb(0, 255, 0);
        radarDataSet.lineWidth = 2f
        radarDataSet.valueTextColor = Color.RED
        radarDataSet.valueTextSize = 14f
        radarDataSet.setDrawValues(false)
        radarDataSet.setDrawVerticalHighlightIndicator(true)
        //radarDataSet.setDrawCircles(true)
        val radarData = RadarData()
        radarData.addDataSet(radarDataSet)
//        val labels = listOf("Documenting", "Coding", "Break", "Distraction", "Other")
        val xAxis = binding.RadarChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.textSize = 12f
        binding.RadarChart.data = radarData
        //binding.RadarChart.getAxisLeft().setDrawLabels(false);
        binding.RadarChart.yAxis.setDrawLabels(false);
        //binding.RadarChart.getXAxis().setDrawLabels(false);
        binding.RadarChart.description.text = ""
        binding.RadarChart.legend.isEnabled = false
        binding.RadarChart.animateY(2000)
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.work_den -> {
                binding.solutionPointContainer.visibility = View.GONE
                binding.workDenContainer.visibility = View.VISIBLE
                binding.workDen.setTextColor(Color.parseColor("#FFFFFF"))
                binding.solutionPoint.setTextColor(Color.parseColor("#C8C8C8"))
                binding.workDen.setBackgroundResource(R.drawable.work_den_bg)
                binding.solutionPoint.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding.fabFilter.visibility = View.VISIBLE
            }
            R.id.solution_point -> {
                binding.workDenContainer.visibility = View.GONE
                binding.solutionPointContainer.visibility = View.VISIBLE
                binding.solutionPoint.setTextColor(Color.parseColor("#FFFFFF"))
                binding.workDen.setTextColor(Color.parseColor("#C8C8C8"))
                binding.workDen.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding.solutionPoint.setBackgroundResource(R.drawable.work_den_bg)
                binding.fabFilter.visibility = View.GONE
                viewModel.getQuery(startDate, "daily")
            }
//            R.id.select_cat_container -> {
//                if (binding.categoryContainer.isVisible) {
//                    binding.categoryContainer.visibility = View.GONE
//                } else {
//                    binding.categoryContainer.visibility = View.VISIBLE
//                }
//            }
            R.id.fab_filter -> {
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(
                    R.id.fragmentContainerView,
                    ActivityLogFiltersDialog(this, categories, getSelectedCategories())
                )
                transaction.addToBackStack(null)
                transaction.commit()
//                ActivityLogFiltersDialog(
//                    this
//                ).show(
//                    (activity as HomeActivity).supportFragmentManager,
//                    "ActivityLogFilter"
//                )
            }
        }
    }
    override fun changeStatus(id: Int, status: Int) {
        Log.e("Status ", "2")
        viewModel.updateStatus(id, status)
    }
    override fun filterData(platform: String, duration: String) {
        selectedPlatform = platform
        binding.fabFilter.text = selectedPlatform
        binding.fabFilter.extend()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.fabFilter.shrink()
        }, 4000)
        viewModel.getLogs(platform, duration)
    }
    override fun filterData(
        platform: String,
        duration: String,
        startDate: String,
        endDate: String
    ) {
        selectedPlatform = platform
        binding.fabFilter.text = selectedPlatform
        binding.fabFilter.extend()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.fabFilter.shrink()
        }, 4000)
        viewModel.getLogs(platform, duration, startDate, endDate)
    }
    private fun getTimeInFormat(time: Double): String {
        val ans: String
        val hours = time.toInt() / 3600
        val remainder = time.toInt() - hours * 3600
        val mins = remainder / 60
        val formatter = DecimalFormat("00");
        ans = "${formatter.format(hours)}h : ${formatter.format(mins)}m"
        return ans
    }
}
*/