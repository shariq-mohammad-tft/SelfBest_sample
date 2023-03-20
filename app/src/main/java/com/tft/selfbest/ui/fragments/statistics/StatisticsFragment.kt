package com.tft.selfbest.ui.fragments.statistics

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.internal.LinkedTreeMap
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentStatisticsBinding
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.HomeActivity
import com.tft.selfbest.ui.adapter.ActivityLogAdapter
import com.tft.selfbest.ui.adapter.AnsweredQueryAdapter
import com.tft.selfbest.ui.adapter.QueryResponseAdapter
import com.tft.selfbest.ui.dialog.ActivityLogFiltersDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StatisticsFragment : Fragment(), View.OnClickListener,
    QueryResponseAdapter.ChangeStatusListener, AnsweredQueryAdapter.ChangeStatusListener, ActivityLogFiltersDialog.FilterListener {
    lateinit var pieChart: PieChart
    lateinit var binding: FragmentStatisticsBinding
    lateinit var startDate: String
    lateinit var endDate: String
    lateinit var queries: List<QueryResponse>
    lateinit var points: List<PointGraphResponse>
    var queries1: MutableList<QueryResponse> = mutableListOf()
    lateinit var answeredQueries: List<QueryAnsweredResponse>
    val startDateCal = Calendar.getInstance()
    val endDateCal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    lateinit var observationsData: ObservationsResponse
    lateinit var activityLogs: List<ActivityLogValues>
    private var observationDetail: List<ObservationDetail>? = null
    lateinit var category: Any
    var ans = false
    private val bt_one_open: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.button_one_open
        )
    }
    private val bt_one_close: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.button_one_close
        )
    }
    private val bt_two_open: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.button_two_open
        )
    }
    private val bt_two_close: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.button_two_close
        )
    }


    private var clicked = false
    val viewModel by viewModels<StatisticsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStatisticsBinding.inflate(layoutInflater)
        pieChart = binding.pieChart
        startDate = dateFormat.format(startDateCal.time)
        endDate = dateFormat.format(endDateCal.time)
        binding.pointGraph.axisLeft.axisMinimum = -10F
        binding.pointGraph.axisRight.axisMinimum = -10F
        binding.pointGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.pointGraph.axisRight.isEnabled = false
        binding.pointGraph.description.isEnabled = false
        binding.pointGraph.xAxis.setDrawGridLines(false)
        binding.pointGraph.axisRight.setDrawGridLines(false)
        binding.pointGraph.axisLeft.setDrawGridLines(false)

        binding.exPg.axisLeft.axisMinimum = -10F
        binding.exPg.axisRight.axisMinimum = -10F
        binding.exPg.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.pointGraph.axisRight.isEnabled = false
        binding.exPg.description.isEnabled = false
        binding.exPg.xAxis.setDrawGridLines(false)
        binding.exPg.axisRight.setDrawGridLines(false)
        binding.exPg.axisLeft.setDrawGridLines(false)
        setClickListeners()
        setUpPieChart()
        viewModel.getLogs("Mobile", "daily")
//        viewModel.activityLogsObserver.observe(viewLifecycleOwner) {
//            if (it is NetworkResponse.Success && it.data!!.logs != null) {
//                activityLogs = it.data.logs
//                if (activityLogs!= null && activityLogs.isNotEmpty()) {
//                    Log.e("Activity Log", "Entered")
//                    loadPieChart(activityLogs)
//                    setAdapter(activityLogs)
//                }else if(activityLogs == null || activityLogs.isEmpty()){
//                    binding.pieChart.clear()
//                    binding.taskList.adapter = observationDetail?.let {
//                        ActivityLogAdapter(
//                            binding.root.context,
//                            listOf()
//                        )
//                    }
//                }
//            }else if(it is NetworkResponse.Success && it.data!!.logs == null){
//                binding.pieChart.clear()
//                binding.taskList.adapter = observationDetail?.let {
//                    ActivityLogAdapter(
//                        binding.root.context,
//                        listOf()
//                    )
//                }
//
//            }
        //}

        viewModel.getStats(startDate, endDate)
        viewModel.observationsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.progressBar.visibility = GONE
                observationsData = it.data!!
                observationDetail = observationsData.observations
                binding.totalHr.text = observationsData.taskCompleted.toString()
                category = observationsData.categories
                if (category.toString() != "{}") {
                    val categoryMap = category as LinkedTreeMap<String, Double>
                    for (entry in categoryMap.entries) {
                        if (entry.key.toString().equals("DistractionTime")) {
                            val distractedTime = getTimeInFormat(entry.value)
                            val formatter = DecimalFormat("00")
                            binding.distractedHr.text =
                                if (distractedTime.indexOf("hr") != -1) formatter.format(
                                    distractedTime.substring(0, distractedTime.indexOf("hr") - 1)
                                        .toInt()
                                ) else {
                                    "00"
                                }
                            if (distractedTime.indexOf("hr") != -1) {
                                binding.distractedM.text = formatter.format(
                                    distractedTime.substring(
                                        distractedTime.indexOf("hr") + 3,
                                        distractedTime.indexOf("min") - 1
                                    ).toInt()
                                )
                            } else {
                                binding.distractedM.text = formatter.format(
                                    distractedTime.substring(
                                        0,
                                        distractedTime.indexOf("min") - 1
                                    ).toInt()
                                )
                            }
                        }
                    }
                }
//                if (observationDetail != null) {
//                    loadPieChart(observationDetail)
//                    setAdapter(observationDetail)
//                }
            }
        }

        viewModel.getPoints("daily", startDate, endDate)
        viewModel.pgObserver.observe(viewLifecycleOwner){
            Log.e("Point Graph", "Monthly 1")

            if(it is NetworkResponse.Success){
                points = it.data!!
                Log.e("Point Graph", "Monthly")
                if(points.isNotEmpty()){
                    setPointsGraph(points)
                }
            }
        }
        viewModel.getQuery(startDate, "daily")
        viewModel.queryObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                queries = it.data!!.query_data
                for(query in queries){
                    if(query.question != "")
                        queries1.add(query)
                }
                if (queries1.isNotEmpty()) {
                    if(queries1.size > 10) {
                        binding.loadMoreBtn.visibility = VISIBLE
                        binding.query.layoutManager = LinearLayoutManager(binding.root.context)
                        binding.query.adapter = QueryResponseAdapter(
                            binding.root.context,
                            queries1.take(10),
                            this
                        )
                    }
                    else {
                        binding.loadMoreBtn.visibility = GONE
                        binding.query.layoutManager = LinearLayoutManager(binding.root.context)
                        binding.query.adapter = QueryResponseAdapter(
                            binding.root.context,
                            queries1,
                            this
                        )
                    }
                    //Log.e("Timestamp ", "${queries.size} $queries")
                }
            }
        }

        viewModel.queryAnsweredObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                answeredQueries = it.data!!
                if(answeredQueries.size > 10){
                    binding.loadMoreBtn.visibility = VISIBLE
                    binding.query.layoutManager = LinearLayoutManager(binding.root.context)
                    binding.query.adapter = AnsweredQueryAdapter(
                        binding.root.context,
                        answeredQueries.take(10),
                        this
                    )
                }else{
                    binding.loadMoreBtn.visibility = GONE
                    binding.query.layoutManager = LinearLayoutManager(binding.root.context)
                    binding.query.adapter = AnsweredQueryAdapter(
                        binding.root.context,
                        answeredQueries,
                        this
                    )
                }
            }
        }
//        viewModel.updationObserver.observe(viewLifecycleOwner) {
//            Log.e("Status ", it.toString())
//            binding.query.adapter = QueryResponseAdapter(
//                binding.root.context,
//                queries,
//                this
//            )
//        }
        return binding.root
    }

    private fun setAdapter(observationDetail: List<ActivityLogValues>?) {
        if (observationDetail != null) {
            if (observationDetail.isEmpty())
                return
        }

        observationDetail?.sortedBy { it.duration }
        binding.taskList.layoutManager = LinearLayoutManager(binding.root.context)
        binding.taskList.adapter = observationDetail?.let {
            ActivityLogAdapter(
                binding.root.context,
                it.take(5)
            )
        }

    }

    private fun setPointsGraph(points : List<PointGraphResponse>){
        val yVals: ArrayList<Entry> = arrayListOf()
        val xAxisLabel : ArrayList<String> = arrayListOf()
        Log.e("Points", "$points")
        for(p in points.indices){
            yVals.add(Entry(p.toFloat(), points[p].points.toFloat()))
            xAxisLabel.add(points[p].xAxisLabel)
        }
        val sety = LineDataSet(yVals, "")
        sety.color = R.color.tool_bar_color
        sety.setDrawCircles(true)
        val data = LineData(sety)
        //sety.mode = LineDataSet.Mode.CUBIC_BEZIER;
        sety.setDrawValues(false)
        sety.setDrawCircles(false)
        binding.pointGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.pointGraph.xAxis.setDrawGridLines(false)
        binding.pointGraph.xAxis.valueFormatter= IndexAxisValueFormatter(xAxisLabel)
        binding.pointGraph.axisRight.isEnabled = false
        binding.pointGraph.setTouchEnabled(false)
        //binding.lChart.animateX(1800, Easing.EaseInOutQuad)
        binding.pointGraph.data = data
        binding.pointGraph.invalidate()

        binding.exPg.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.exPg.axisRight.isEnabled = false
        binding.exPg.setTouchEnabled(false)
        //binding.lChart.animateX(1800, Easing.EaseInOutQuad)
        binding.exPg.data = data
        binding.exPg.invalidate()

    }

    private fun loadPieChart(observationDetail: List<ActivityLogValues>?) {
        if (observationDetail != null) {
            if (observationDetail.isEmpty())
                return
        }
        val list: ArrayList<ObservationDetailSubPart> = arrayListOf()
        if (observationDetail != null) {
            observationDetail.sortedBy { it.duration }
            for (detail in observationDetail.take(5)) {
                list.add((ObservationDetailSubPart(detail.url, detail.duration)))
            }
        }
        val entries = ArrayList<PieEntry>()
        for (entry in list) {
            entries.add(PieEntry(entry.duration.toFloat(), entry.url))
        }

        val colors = ArrayList<Int>()
//        for (color in ColorTemplate.MATERIAL_COLORS) {
//            colors.add(color)
//        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setDrawValues(false)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(7.2f)
        data.setValueTextColor(R.color.black)
        pieChart.data = data
        pieChart.invalidate()
        pieChart.animateY(1400, Easing.EaseInOutQuad)

    }

    private fun setUpPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(false)
        //pieChart.setEntryLabelTextSize(8f)
        pieChart.setDrawEntryLabels(false)
        //pieChart.setEntryLabelColor(R.color.black)
        pieChart.setNoDataText("No data")
        pieChart.description.isEnabled = false
        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.yEntrySpace = 4f
        legend.formToTextSpace = 4f
        legend.setDrawInside(false)
        legend.textSize = 8.0f
        legend.isEnabled = false

    }

    private fun setClickListeners() {
//        binding.activityButton.setOnClickListener(this)
//        binding.iconMobile.setOnClickListener(this)
//        binding.iconDesktop.setOnClickListener(this)
//        binding.iconWeb.setOnClickListener(this)
        binding.loadMoreBtn.setOnClickListener(this)
        binding.answered.setOnClickListener(this)
        binding.asked.setOnClickListener(this)
        binding.expandPg.setOnClickListener(this)
        binding.collapsePg.setOnClickListener(this)
        binding.filters.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.answered -> {
                ans = true
                binding.asked.setTextColor(Color.parseColor("#707070"))
                binding.asked.setBackgroundColor(Color.parseColor("#ccf8f8f8"))
                binding.answered.setTextColor(Color.WHITE)
                binding.answered.setBackgroundResource(R.drawable.ans_query_bg)
                binding.queryHeading.setTextColor(Color.parseColor("#00A2A4"))
                viewModel.getAnsweredQuery("", startDate, "daily")
            }

            R.id.asked -> {
                ans = false
                binding.asked.setTextColor(Color.WHITE)
                binding.asked.setBackgroundResource(R.drawable.get_started_btn_bg)
                binding.answered.setTextColor(Color.parseColor("#707070"))
                binding.answered.setBackgroundColor(Color.parseColor("#ccf8f8f8"))
                binding.queryHeading.setTextColor(Color.parseColor("#1D71D4"))
                queries1.clear()
                viewModel.getQuery(startDate, "daily")
            }

            R.id.load_more_btn -> {
                binding.loadMoreBtn.visibility = GONE
                if(ans){
                    binding.loadMoreBtn.visibility = GONE
                    binding.query.layoutManager = LinearLayoutManager(binding.root.context)
                    binding.query.adapter = AnsweredQueryAdapter(
                        binding.root.context,
                        answeredQueries,
                        this
                    )
                }else{
                    binding.loadMoreBtn.visibility = GONE
                    binding.query.layoutManager = LinearLayoutManager(binding.root.context)
                    binding.query.adapter = QueryResponseAdapter(
                        binding.root.context,
                        queries1,
                        this
                    )
                }
            }

            R.id.expand_pg -> {
                binding.main.visibility = GONE
                //binding.exPg.data = binding.pointGraph.data
                val rotateAnim = RotateAnimation(
                    0.0f, 90.0f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f
                )

                rotateAnim.duration = 0
                rotateAnim.fillAfter = true
                binding.exPg.startAnimation(rotateAnim)
                binding.graph.visibility = VISIBLE
            }

            R.id.collapse_pg -> {
                binding.graph.visibility = GONE
                binding.main.visibility = VISIBLE
            }

            R.id.filters -> {
//                ActivityLogFiltersDialog(
//                    this
//                ).show(
//                    (activity as HomeActivity).supportFragmentManager,
//                    "ActivityLogFilter"
//                )
            }
//            R.id.activity_button -> {
//                changeType()
//            }
//
//            R.id.icon_mobile -> {
//                Toast.makeText(activity, "Mobile type is selected", Toast.LENGTH_LONG).show()
//            }
//
//            R.id.icon_web -> {
//                Toast.makeText(activity, "Web type is selected", Toast.LENGTH_LONG).show()
//            }
//
//            R.id.icon_desktop -> {
//                Toast.makeText(activity, "Desktop type is selected", Toast.LENGTH_LONG).show()
//            }
        }
    }

//    private fun loadFragment(fragment: Fragment) {
//        // load fragment
//        val fragmentManager: FragmentManager = activity!!.supportFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//        transaction.replace(R.id.fragmentContainerView, fragment)
//        transaction.r
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }

//    private fun changeType() {
//        setVisibility(clicked)
//        setAnimation(clicked)
//        isClickable(clicked)
//        clicked = !clicked
//    }

//    private fun setVisibility(clicked: Boolean) {
//        if (!clicked) {
//            binding.iconMobile.visibility = View.VISIBLE
//            binding.iconWeb.visibility = View.VISIBLE
//            binding.iconDesktop.visibility = View.VISIBLE
//        } else {
//            binding.iconMobile.visibility = View.INVISIBLE
//            binding.iconWeb.visibility = View.INVISIBLE
//            binding.iconDesktop.visibility = View.INVISIBLE
//        }
//    }

//    private fun setAnimation(clicked: Boolean) {
//        if (!clicked) {
//            binding.iconMobile.startAnimation(bt_one_open)
//            binding.iconWeb.startAnimation(bt_one_open)
//            binding.iconDesktop.startAnimation(bt_two_open)
//        } else {
//            binding.iconMobile.startAnimation(bt_one_close)
//            binding.iconDesktop.startAnimation(bt_two_close)
//            binding.iconWeb.startAnimation(bt_one_close)
//        }
//    }

//    private fun isClickable(clicked: Boolean) {
//        if (!clicked) {
//            binding.iconMobile.isClickable = true
//            binding.iconDesktop.isClickable = true
//            binding.iconWeb.isClickable = true
//        } else {
//            binding.iconMobile.isClickable = false
//            binding.iconDesktop.isClickable = false
//            binding.iconWeb.isClickable = false
//        }
//    }

    private fun getTimeInFormat(time: Double): String {
        val ans: String
        val hours = time.toInt() / 3600
        val remainder = time.toInt() - hours * 3600
        val mins = remainder / 60
        if (hours == 0) {
            ans = "$mins mins"
        } else {
            ans = "$hours hr $mins mins"
        }
        return ans
    }

    override fun changeStatus(id: Int, status: Int) {
        Log.e("Status ", "2")
        viewModel.updateStatus(id, status)
    }

    override fun filterData(platform: String, duration: String) {
        when (duration) {
            "Daily" -> {
                viewModel.getLogs(platform, "daily")
                viewModel.getStats(startDate, endDate)
                viewModel.getPoints(duration.lowercase(), "", "")
            }
            "Weekly" -> {
                viewModel.getLogs(platform, "week")
                endDateCal.add(Calendar.DAY_OF_YEAR, -7)
                endDate = dateFormat.format(endDateCal.time)
                viewModel.getStats(startDate, endDate)
                viewModel.getPoints(duration.lowercase(), "", "")
            }
            "Monthly" -> {
                Log.e("Point graph", "Month ")
                viewModel.getLogs(platform, "month")
                startDateCal.set(Calendar.DAY_OF_MONTH, startDateCal.getActualMinimum(Calendar.DAY_OF_MONTH))
                startDate = dateFormat.format(startDateCal.time)
                endDateCal.set(Calendar.DAY_OF_MONTH, endDateCal.getActualMaximum(Calendar.DAY_OF_MONTH))
                endDate = dateFormat.format(endDateCal.time)
                viewModel.getStats(startDate, endDate)
                viewModel.getPoints(duration.lowercase(), "", "")
            }
            else -> {
                viewModel.getLogs(platform, "month")
                startDateCal.set(Calendar.DAY_OF_MONTH, startDateCal.getActualMinimum(Calendar.DAY_OF_MONTH))
                startDate = dateFormat.format(startDateCal.time)
                endDateCal.set(Calendar.DAY_OF_MONTH, endDateCal.getActualMaximum(Calendar.DAY_OF_MONTH))
                endDate = dateFormat.format(endDateCal.time)
                viewModel.getStats(startDate, endDate)
                viewModel.getPoints(duration.lowercase(), "", "")
            }
        }
    }

    override fun filterData(
        platform: String,
        duration: String,
        startDate: String,
        endDate: String
    ) {
        TODO("Not yet implemented")
    }

}