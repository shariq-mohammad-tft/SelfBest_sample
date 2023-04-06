package com.tft.selfbest.ui.fragments.getGoHour

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.*
import android.util.Log
import android.view.*
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.EntryXComparator
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.databinding.GetGoHourActivityBinding
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.ActivityTimelineAdapter
import com.tft.selfbest.ui.adapter.BarGraphListAdapter
import com.tft.selfbest.ui.fragments.inputProgress.InputProgress
import com.tft.selfbest.ui.fragments.inputProgress.InputProgressViewModel
import com.tft.selfbest.ui.fragments.overview.OverviewFragment
import com.tft.selfbest.ui.fragments.overview.OverviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class GetGoHour : Fragment(), View.OnClickListener {

    @Inject
    lateinit var preferences: SelfBestPreference
    lateinit var binding: GetGoHourActivityBinding
    val viewModel by viewModels<GetGoHourViewModel>()
    //val viewModelCal by viewModels<CalenderViewModel>()
    private val viewModelChart by viewModels<ChartViewModel>()
    private val overviewViewModel by viewModels<OverviewViewModel>()
    private val ipViewModel by viewModels<InputProgressViewModel>()
    private var max = 0
    var activities: List<ActivitySingleResponse> = listOf()
    private var running: Boolean = false
    private var progressDetail: List<ProgressDetails>? = null
    private var acvityDetails: List<ActivitiesDetails>? = null
    lateinit var chartlogDa: GetChartData
    var pauseTime = ""
    var timeLeft: Long = 0
    var resetLeft = 4
    var yVals: ArrayList<Entry> = arrayListOf()
    var labels = mutableListOf<String>()

    //    lateinit var swipeListener: SwipeListener
    lateinit var getGoHourResponse: GetGoHourResponse
    lateinit var observationsData: ObservationsResponse
    private var observationDetail: List<ObservationDetail>? = null
    lateinit var category: Any
    lateinit var progress: List<SubProgressResponse>


    //code for auto pause
    private lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())
    private val interval: Long = 5 * 1000

    var vowel_list: List<Double>? = null
    var listDur: ArrayList<ActivitiesDetailsDuration> = arrayListOf()
    var neu: Float = 0.0f
    var dist: Float = 0.0f
    var code: Float = 0.0f
    var learn: Float = 0.0f
    var doc: Float = 0.0f
    var brk: Float = 0.0f
    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
    lateinit var barEntriesList: ArrayList<BarEntry>
    lateinit var barChart: BarChart

    var timerEnded = false

    companion object {
        lateinit var timer: CountDownTimer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GetGoHourActivityBinding.inflate(layoutInflater)
//        swipeListener = SwipeListener(binding.scrollView)
        setListeners()
        //val mainHandler = Handler(Looper.getMainLooper())
        setChart()
        setBarChart(activities)
        //binding.lChart.setViewPortOffsets(4f, 4f, 4f, 4f)

        //dummydata
//        binding.recentEvent.layoutManager = LinearLayoutManager(context)
//        binding.recentEvent.adapter = ActivityTimelineAdapter(
//            timeline
//        )


        viewModel.activityLogObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                if(it.data!!.categoryList != null) {
                    labels.clear()
                    for (entry in (it.data.categoryList as Map<String, String>)) {
                        if(entry.value != "")
                            labels.add(entry.value)
                    }
                }
                if(it.data!!.activities != null) {
                    activities = it.data.activities
                    setBarChart(activities)
                }
                Log.e("ActivityLog: ", " Success")
                if (it.data.progress != null) {
                    progress = it.data.progress!!
                    setLineChart(progress)
                }
                if (it.data.isPaused) {
                    pauseTimer()
                    binding.pause.setImageResource(R.drawable.ic_play_fill)
                }
            }
        }

        overviewViewModel.getGoHour()
        overviewViewModel.getGoHourObserver.observe(viewLifecycleOwner) {
            binding.progress.visibility = View.GONE
            if (it is NetworkResponse.Success) {
                getGoHourResponse = it.data!!
                binding.resetLeft.text = it.data.resetsLeft.toString()
                resetLeft = it.data.resetsLeft
                if (getGoHourResponse.isPaused) {
                    pauseTime = getGoHourResponse.pauseStartTime
                    pauseTimer()
                }
                if (getGoHourResponse.isActive && !getGoHourResponse.isPaused && !getGoHourResponse.ended && it.data.startTime != null) {
                    Log.e("Already Started", "GGH")
                    binding.pause.setImageResource(R.drawable.ic_pause)
                    if (running)
                        timer.cancel()
                    binding.myView.startAnimation()
                    binding.timeHour.text = getGoHourResponse.timeInterval.toString()
                    max = binding.timeHour.text.toString().toInt() * 60 * 60
                    updateProgressBar(max)
                    val abc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ChronoUnit.SECONDS.between(
                            startTime(getGoHourResponse.startTime),
                            Instant.now()
                        )
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                    //val updatedTime = max - abc
                    val updatedTime = (max - abc) + (getGoHourResponse.totalPauseTime / 1000000000)
                    timer = object : CountDownTimer(updatedTime * 1000, 1 * 1000) {
                        override fun onTick(millisUntilFinished: Long) {

                            timeLeft = millisUntilFinished
                            binding.progressBar.progress =
                                ((max * 1000 - millisUntilFinished) / 1000.0).roundToInt()
                            binding.myView.progress =
                                ((max * 1000 - millisUntilFinished) / 1000.0).roundToInt()
                            val f: NumberFormat = DecimalFormat("00")
                            val hour = (max * 1000 - millisUntilFinished) / 3600000 % 24
                            val min = (max * 1000 - millisUntilFinished) / 60000 % 60
                            val sec = (max * 1000 - millisUntilFinished) / 1000 % 60
                            val finalTime = f.format(hour)
                                .toString() + ":" + f.format(min) + ":" + f.format(sec)
                            binding.text.text = finalTime
                        }

                        override fun onFinish() {
                            timerEnded = true
                            running = false
                            updateProgressBar(max)
                            viewModel.end(EndTime(Instant.now().toString()))
                        }
                    }.start()
                    running = true

                }
//                if (getGoHourResponse.ended) {
//                    Log.e("Timer: ", "Ended")
//                    endTimer()
//                }
                else if (!getGoHourResponse.isActive) {
                    Log.e("Timer: ", "Ended")
                    endTimer()
                    timerEnded = true
                    viewModel.getActivity()
                }
            }
        }
        viewModel.getTimeline()
        viewModel.activityTimelineObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success && it.data != null) {
                //binding.noActivityTimelineData.visibility = View.GONE
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
        viewModel.pauseGetGoHourObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                pauseTimer()
            }
        }
        viewModel.resumeGetGoHourObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success)
                resumeTimer()
        }
        viewModel.resetGetGoHourObserver.observe(viewLifecycleOwner) {
            Log.e("Timer:", "Success 1 reset")
            if (it is NetworkResponse.Success) {
                Log.e("Timer:", "Success 2 reset")
                if (it.data?.resetsLeft?.toInt()!! >= 0) {
                    //Log.e("Timer:", "${it.data}")
                    binding.resetLeft.text = it.data.resetsLeft
                    resetLeft = it.data.resetsLeft.toInt()
                    resetTimer()
                } else
                    Toast.makeText(requireContext(), "Maximum Limits reached", Toast.LENGTH_SHORT)
                        .show()
            }
        }
        viewModel.endGetGoHourObserver.observe(viewLifecycleOwner) {
            endTimer()
            timerEnded = true
            viewModel.getActivity()
        }
        viewModel.activityLogObserver.observe(viewLifecycleOwner){
            if (it is NetworkResponse.Success && timerEnded) {
                Log.e("ActivityLog: ", " Success")
                if(it.data!!.activities == null || it.data.activities.isEmpty()){
                    ipViewModel.getInput(InputData( preferences.getGetHourId, listOf(),1))
                }
                else if (it.data.activities.isNotEmpty()) {
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
/*
        viewModel.timeIntervalObserver.observe(viewLifecycleOwner) {

        }

        binding.recentEvent.layoutManager = LinearLayoutManager(context)
        binding.recentEvent.adapter =
            RecentEventAdapter(DateTimeUtil.getEventsTime(null, DateTimeUtil.get24HourList()))

        viewModelCal.getAllUpcomingEvents(
            cal.get(Calendar.DAY_OF_MONTH),
            cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
            cal.get(Calendar.YEAR)
        )

        viewModelCal.upComingEventsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.recentEvent.adapter =
                    RecentEventAdapter(it.data as java.util.ArrayList<EventDetail>)
            }
        }
        viewModelCal.addEventObserver.observe(viewLifecycleOwner) {
            Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
        }
*/
        return binding.root
    }

    private fun setChart() {
        binding.lChart.axisLeft.axisMinimum = 0F
        binding.lChart.axisRight.axisMinimum = 0F
        binding.lChart.xAxis.axisMinimum = 0F
        binding.lChart.xAxis.axisMaximum = (binding.timeHour.text.toString().toInt()*60F)
        binding.exPg.axisLeft.axisMinimum = 0F
        binding.exPg.axisRight.axisMinimum = 0F
        yVals.add(Entry(0F, 0F))
        val sety = LineDataSet(yVals, "Time")
        val data = LineData(sety)
        sety.mode = LineDataSet.Mode.CUBIC_BEZIER
        sety.setDrawCircles(false)
        sety.setDrawValues(false)
        binding.lChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lChart.axisRight.isEnabled = false
        binding.lChart.description.isEnabled = true
        binding.lChart.description.text = "Time (in minutes)"
        binding.exPg.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.exPg.axisRight.isEnabled = false
        binding.exPg.description.isEnabled = false
        //binding.lChart.animateX(1800, Easing.EaseInExpo)
        binding.lChart.data = data
        binding.lChart.xAxis.setDrawGridLines(false)

        binding.exPg.animateX(1800, Easing.EaseInExpo)
        binding.exPg.data = data
        binding.exPg.xAxis.setDrawGridLines(false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startTime(startTime: String): Temporal? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.parse(startTime)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        //return Instant.parse(startTime)
    }


    override fun onResume() {
        //start handler as activity become visible
        handler.postDelayed(Runnable {
            overviewViewModel.getGoHour()
            viewModel.getActivity()
            handler.postDelayed(runnable, interval)
        }.also { runnable = it }, interval)
        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnable) //stop handler when activity not visible
        super.onPause()
    }

    private fun setListeners() {
        binding.timeInc.setOnClickListener(this)
        binding.timeDec.setOnClickListener(this)
        binding.pause.setOnClickListener(this)
        binding.reset.setOnClickListener(this)
        binding.end.setOnClickListener(this)
        binding.bargraphLoad.setOnClickListener(this)
        binding.list.setOnClickListener(this)
        binding.expandPg.setOnClickListener(this)
        binding.collapsePg.setOnClickListener(this)

    }

    private fun updateProgressBar(max: Int) {
        binding.progressBar.max = max
        binding.myView.max = max
//        binding.progressBar.progress = prog
//        binding.myView.progress = prog
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.timeDec -> {
                if (binding.timeHour.text.toString() == "2") {
                    Toast.makeText(activity, "Time can't be less than 2 hours", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val time = binding.timeHour.text.toString().toInt() - 1
                    viewModel.timeInterval(TimeInterval(time))
                    binding.timeHour.text = time.toString()
                    updateProgressBar(time * 60 * 60)
                }
            }
            R.id.timeInc -> {
                if (binding.timeHour.text.toString() == "9") {
                    Toast.makeText(activity, "Time can't be more than 9 hours", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val time: Int = binding.timeHour.text.toString().toInt() + 1
                    viewModel.timeInterval(TimeInterval(time))
                    binding.timeHour.text = time.toString()
                    updateProgressBar(time * 60 * 60)
                }
            }
            R.id.pause -> {
                if (running) {
                    //pauseTime = Instant.now().toString()
                    binding.pause.setImageResource(R.drawable.ic_play_fill)
                    //val inst = Instant.parse(getServerTimeZone(System.currentTimeMillis()))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.e("Timer : Pause", "${Instant.now()}")
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        viewModel.pause(StartTime(Instant.now().toString()))
                    }
                } else {
                    binding.pause.setImageResource(R.drawable.ic_pause)
                    //val inst = Instant.parse(getServerTimeZone(System.currentTimeMillis()))
                    Log.e("Timer : Resume", "${Instant.now()}")
                    viewModel.resume(EndTime(Instant.now().toString()))
                }
            }
            R.id.reset -> {
                if (resetLeft > 0)
                    viewModel.reset(StartTime(Instant.now().toString()))
                else
                    Toast.makeText(requireContext(), "Maximum Limits reached", Toast.LENGTH_SHORT)
                        .show()

//                if(resetLeft-- > 0)
//                    resetTimer()
//                else
//                    Toast.makeText(activity, "Maximum Limits reached", Toast.LENGTH_SHORT).show()
            }
            R.id.end -> {
                binding.progress.visibility = View.VISIBLE
                viewModel.end(EndTime(Instant.now().toString()))
            }

            R.id.bargraphLoad -> {
                binding.bar.visibility = View.VISIBLE
                binding.listdatalinear.visibility = View.INVISIBLE
                setBarChart(activities)
//                viewModelChart.observationsObserver.observe(viewLifecycleOwner) {
//                    if (it is NetworkResponse.Success) {
//                        observationsData = it.data!!
//                        observationDetail = observationsData.observations
//
//                        category = observationsData.categories
//                        Log.d("rrr", observationsData.categories.toString())
//                        if (category.toString() != "{}") {
//                            val categoryMap = category as LinkedTreeMap<String, Double>
//                            for (entry in categoryMap.entries) {
//                                if (entry.key.toString().equals("DistractionTime")) {
//                                    val distractedTime = getTimeInFormat(entry.value)
//                                }
//                            }
//                        }
//                    }
//                }

                //viewModelChart.getChart("Asia/Calcutta")
//                viewModelChart.chartLogObserver.observe(viewLifecycleOwner) {
//                    if (it is NetworkResponse.Success)
//                        chartlogDa = it.data!!
//                    progressDetail = chartlogDa.Progress
//                    acvityDetails = chartlogDa.Activity
//                    if (acvityDetails.toString() != null) {

                        //  val categoryMap= acvityDetails as LinkedTreeMap<>
                    }

//                }
                // var list:ArrayList<ProgressDetailsSubPart> = arrayListOf()

//                val listCat: ArrayList<ActivitiesDetailsSubPart> = arrayListOf()
//                if (acvityDetails != null) {
//                    for (actDet in acvityDetails!!) {
//                        listCat.add(ActivitiesDetailsSubPart(actDet.Cate, actDet.duration))
//                        if (actDet.Cate == "Neutral") {
//                            neu = getTimeInFormat(actDet.duration)
//                            Log.d("kkk", actDet.Cate)
//                        } else if (actDet.Cate == "Distraction") {
//                            dist = getTimeInFormat(actDet.duration)
//                        } else if (actDet.Cate == "Code") {
//                            code = getTimeInFormat(actDet.duration)
//                        } else if (actDet.Cate == "Learning") {
//                            learn = getTimeInFormat(actDet.duration)
//                        } else if (actDet.Cate == "Documentation") {
//                            doc = getTimeInFormat(actDet.duration)
//                        }
//                        Log.d("yyy", actDet.Cate)
                        //Log.d("zzz","time : "+actDet.duration.toString())

                    //}
                //}
//                if (acvityDetails != null) {
//                    for (actDet in acvityDetails!!) {
//                        listDur.add(ActivitiesDetailsDuration(actDet.duration))
//                        //Log.d("yyy",actDet.Cate)
//                        Log.d("zzz", "time : " + actDet.duration.toString())
//                        vowel_list = listOf(actDet.duration)
//                        Log.d("arrList", vowel_list.toString())
//
//                    }
//                }

//                var mutlist: MutableList<Float> = mutableListOf<Float>()
//                val itr = vowel_list?.iterator()
//                if (itr != null) {
//                    while (itr.hasNext()) {
//                        Log.d("listDur", itr.next().toString())
//
//                    }
//                }
            //}

            R.id.list -> {
                binding.bar.visibility = View.INVISIBLE
                binding.listdatalinear.visibility = View.VISIBLE
                Log.e("BAr Chart", activities.toString())
                binding.listdata.layoutManager = LinearLayoutManager(binding.root.context)
                binding.listdata.adapter = BarGraphListAdapter(
                    binding.root.context,
                    activities
                )
            }

            R.id.expand_pg -> {
                binding.constLayout.visibility = View.GONE
                //binding.exPg.data = binding.pointGraph.data
                val rotateAnim = RotateAnimation(
                    0.0f, 90.0f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f
                )

                rotateAnim.duration = 0
                rotateAnim.fillAfter = true
                binding.exPg.startAnimation(rotateAnim)
                binding.graph.visibility = View.VISIBLE
            }

            R.id.collapse_pg -> {
                binding.graph.visibility = View.GONE
                binding.constLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun pauseTimer() {
//        Toast.makeText(activity,"paused",Toast.LENGTH_SHORT).show()
        if (running) {
            timer.cancel()
            running = false
        }
    }

    private fun getTimeInFormat(time: Double): Float {
        var ans = 0.0f
        val hours = time.toInt() / 3600
        val remainder = time.toInt() - hours * 3600
        val mins = remainder / 60
        if (hours == 0) {
            ans = mins.toFloat()
        }
        return ans

    }

    private fun resumeTimer() {
//        Toast.makeText(activity,"Resumed",Toast.LENGTH_SHORT).show()
        max = binding.timeHour.text.toString().toInt() * 60 * 60
        updateProgressBar(max)
        timer = object : CountDownTimer(timeLeft, 1 * 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                binding.progressBar.progress =
                    (((max * 1000) - millisUntilFinished) / 1000.0).roundToInt()
                binding.myView.progress =
                    (((max * 1000) - millisUntilFinished) / 1000.0).roundToInt()
                Log.e("Timer: ", "${(((max * 1000) - millisUntilFinished) / 1000.0).roundToInt()}")
                val f: NumberFormat = DecimalFormat("00")
                val hour = (max * 1000 - millisUntilFinished) / 3600000 % 24
                val min = (max * 1000 - millisUntilFinished) / 60000 % 60
                val sec = (max * 1000 - millisUntilFinished) / 1000 % 60
                binding.text.text =
                    f.format(hour).toString() + ":" + f.format(min) + ":" + f.format(sec)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFinish() {
                running = false
                timerEnded = true
                Log.d("TAG", "Time's up!")
                viewModel.end(EndTime(Instant.now().toString()))
            }
        }.start()
        running = true
    }

    private fun endTimer() {
        if (running) {
            timer.cancel()
//            binding.progressBar.progress = 0
//            binding.myView.progress = 0
//            binding.text.text = "00:00:00"
            running = false
        }
    }

    private fun resetTimer() {
        if (running)
            timer.cancel()
        max = binding.timeHour.text.toString().toInt() * 60 * 60
        updateProgressBar(max)
        timer = object : CountDownTimer(max.toLong() * 1000, 1 * 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //Log.e("Timer: ", "$millisUntilFinished")
                timeLeft = millisUntilFinished
                binding.progressBar.progress =
                    ((max * 1000 - millisUntilFinished) / 1000.0).roundToInt()
                binding.myView.progress = ((max * 1000 - millisUntilFinished) / 1000.0).roundToInt()
                val f: NumberFormat = DecimalFormat("00")
                val hour = (max * 1000 - millisUntilFinished) / 3600000 % 24
                val min = (max * 1000 - millisUntilFinished) / 60000 % 60
                val sec = (max * 1000 - millisUntilFinished) / 1000 % 60
                binding.text.text =
                    f.format(hour).toString() + ":" + f.format(min) + ":" + f.format(sec)
            }


            override fun onFinish() {
                timerEnded = true
                running = false
                updateProgressBar(max)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewModel.end(EndTime(Instant.now().toString()))
                }
            }
        }.start()
        running = true
    }

    /*inner class SwipeListener(view: View?) : View.OnTouchListener {

        var gestureDetector: GestureDetector

        init {
            val threshold = 100
            val velocityThreshold = 100

            val listener = object : GestureDetector.SimpleOnGestureListener() {
//                override fun onDown(e: MotionEvent?): Boolean {
//                    return true
//                }

                override fun onFling(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (e1 == null || e2 == null)
                        return false
                    val xDiff = e2.x - e1.x
                    val yDiff = e2.y - e1.y
                    try {
                        if (abs(yDiff) > abs(xDiff)) {
                            if (abs(yDiff) > threshold && abs(velocityY) > velocityThreshold) {
                                if (yDiff > 0) {
                                    binding.progress.visibility = View.VISIBLE
                                    overviewViewModel.getGoHour()
//                                    yVals.clear()
//                                    yVals.add(Entry(0F, 0F))
                                    viewModel.getActivity()
                                }
                            }
                            return true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return false
                }

            }
            gestureDetector = GestureDetector(listener)
            view?.setOnTouchListener(this)
        }


        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            return gestureDetector.onTouchEvent(p1!!)
        }

    }*/

    private fun getBarChartData() {
        //var i:Float=0f
        barEntriesList = ArrayList()
       barEntriesList.add(BarEntry(1f, 4f))
        barEntriesList.add(BarEntry(2f, 5f))
        barEntriesList.add(BarEntry(3f, 3f))
        barEntriesList.add(BarEntry(4f, 2f))
        barEntriesList.add(BarEntry(5f, 1f))
        barEntriesList.add(BarEntry(6f, 1.5f))
//        if(acvityDetails!= null){
//            for (actDet in acvityDetails!!){
//                listDur.add(ActivitiesDetailsDuration(actDet.duration))
        //Log.d("yyy",actDet.Cate)
//      barEntriesList.add(BarEntry(1f, code))
//      barEntriesList.add(BarEntry(2f, learn))
//      barEntriesList.add(BarEntry(3f, doc))
//      barEntriesList.add(BarEntry(4f, neu))
//      barEntriesList.add(BarEntry(5f, brk))
//      barEntriesList.add(BarEntry(6f, dist))
    }

    private fun setLineChart(progress: List<SubProgressResponse>) {
        for (p in progress) {
            yVals.add(Entry(p.xAxisLabel.toFloat(), p.point))
        }
        Collections.sort(yVals, EntryXComparator())
        val sety = LineDataSet(yVals, "Points")
        sety.color = Color.parseColor("#1D71D4")
        val data = LineData(sety)
        sety.mode = LineDataSet.Mode.CUBIC_BEZIER
        sety.setDrawCircles(false)
        sety.setDrawValues(false)
        binding.lChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lChart.axisRight.isEnabled = false
        binding.lChart.xAxis.axisMinimum = 0F
        binding.lChart.xAxis.axisMaximum = (binding.timeHour.text.toString().toInt()*60F)
        binding.lChart.animateX(1800, Easing.EaseInOutQuad)
        binding.lChart.data = data
        binding.exPg.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.exPg.axisRight.isEnabled = false
        binding.exPg.animateX(1800, Easing.EaseInOutQuad)
        binding.exPg.data = data
    }

    fun printCHart(arrayList: List<Entry>?) {
        Log.d("prt", arrayList.toString())
        val l = LineDataSet(arrayList, "Time")
        l.setDrawCircles(false)
        l.setDrawValues(false)
        binding.lChart.data = LineData(l)

        binding.lChart.axisLeft.axisMinimum = 0f
        binding.lChart.axisRight.axisMinimum = 0f
        binding.lChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lChart.axisRight.isEnabled = false
        binding.lChart.description.isEnabled = false
        binding.lChart.animateX(1800, Easing.EaseInExpo)
        binding.lChart.defaultValueFormatter
        LineDataSet.Mode.CUBIC_BEZIER
    }

    fun chartCall() {
        viewModelChart.getChart("Asia/Calcutta")
        viewModelChart.chartLogObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success)
                chartlogDa = it.data!!
            progressDetail = chartlogDa.Progress
            acvityDetails = chartlogDa.Activity
//            val entryList=progressDetail?.mapIndexed { index, progressDetails ->
//                Entry(progressDetails.xis.toFloat(),progressDetails.point.toFloat())
//            }
            if (progressDetail != null) {
                for (p in progressDetail!!) {
                    yVals.add(Entry(p.xis.toFloat(), p.point.toFloat()))
                }
            }

            printCHart(yVals)
        }
    }

    private fun setBarChart(activities: List<ActivitySingleResponse>) {
        binding.bar.description.isEnabled = false

        barEntriesList = ArrayList()
        val barEntries = mutableMapOf<String, Double>()
        var x = 1f
        labels.remove("")

        for(entry in labels){
            barEntries[entry] = 0.0
        }

        for(activity in activities) {
            if (activity.category in barEntries.keys) {
                barEntries[activity.category] = barEntries[activity.category]!! + activity.duration
            } else {
                barEntries[activity.category] = activity.duration
            }
        }

        labels.add(0, "")

        Log.e("BarChart", barEntries.toString())
        for(entry in barEntries.keys){
            barEntriesList.add(BarEntry(x, getTimeForBarChart(barEntries[entry]!!)))
            Log.e("BarChart X = ", "$x")
            x += 1f
        }
        Log.e("BarChart", labels.toString())
        //getBarChartData()

        barDataSet = BarDataSet(barEntriesList, "")
        barData = BarData(barDataSet)
        barData.barWidth = .5f
        barDataSet.highLightColor = Color.TRANSPARENT
        binding.bar.data = barData
        binding.bar.description.isEnabled = false
        binding.bar.elevation = 30f
//                binding.bar.renderer=
//                    RoundedBarChart(binding.bar,binding.bar.animator,binding.bar.viewPortHandler)
        binding.bar.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.bar.xAxis.labelRotationAngle = -45F
        binding.bar.setScaleEnabled(false)
        binding.bar.setDrawValueAboveBar(true)
        binding.bar.xAxis.setDrawGridLines(false)
        binding.bar.xAxis.labelCount = labels.size - 1
        binding.bar.setGridBackgroundColor(Color.parseColor("#FCFCFC"))
        binding.bar.axisLeft.setDrawGridLines(true)
        binding.bar.setDrawGridBackground(true)
        binding.bar.legend.isEnabled = false
        binding.bar.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.bar.axisLeft.axisMinimum = 0f
        binding.bar.axisLeft.axisMaximum = (binding.timeHour.text.toString().toInt()*60F)
        barDataSet.setColors(
            Color.parseColor("#C8C8C8"), Color.parseColor("#C8C8C8"),
            Color.parseColor("#C8C8C8"), Color.parseColor("#3E3E3E"),
            Color.parseColor("#E20404"), Color.parseColor("#7630F2"),
        )
        val rightAxis = binding.bar.axisRight
        rightAxis.isEnabled = false
    }

    private fun getTimeForBarChart(time: Double): Float {
        return time.toFloat() / 60
    }
    
}


