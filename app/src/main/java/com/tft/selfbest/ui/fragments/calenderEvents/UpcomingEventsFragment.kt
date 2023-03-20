package com.tft.selfbest.ui.fragments.calenderEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.databinding.FragmentUpcomingEventsBinding
import com.tft.selfbest.models.calendarEvents.EventDetail
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.DetailActivity
import com.tft.selfbest.ui.activites.HomeActivity
import com.tft.selfbest.ui.adapter.DaysInWeekAdapter
import com.tft.selfbest.ui.adapter.UpcomingEventsAdapter
import com.tft.selfbest.ui.dialog.AddEventDialog
import com.tft.selfbest.utils.DateTimeUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpcomingEventsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class UpcomingEventsFragment : Fragment(), DaysInWeekAdapter.SelectedDate,
    AddEventDialog.AddEventListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val viewModel by viewModels<CalendarEventsViewModel>()
    var cal = Calendar.getInstance()
    private lateinit var binding: FragmentUpcomingEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        binding.daysInWeek.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.eventsTime.layoutManager = LinearLayoutManager(context)
        binding.daysInWeek.adapter =
            DaysInWeekAdapter(binding.root.context,
                DateTimeUtil.getWeekDays(),
                DateTimeUtil.dayofmonth, DateTimeUtil.totalDay, this)
        binding.daysInWeek.visibility = if (activity is DetailActivity) View.VISIBLE else View.GONE
        viewModel.getAllUpcomingEvents(cal.get(Calendar.DAY_OF_MONTH),
            cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())!!,
            cal.get(Calendar.YEAR))

        viewModel.upComingEventsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.eventsTime.adapter =
                    UpcomingEventsAdapter(DateTimeUtil.getEventsTime(it.data as ArrayList<EventDetail>,
                        DateTimeUtil.get24HourList()))
            }
        }
        viewModel.addEventObserver.observe(viewLifecycleOwner) {
            Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
        }
        binding.eventsTime.adapter =
            UpcomingEventsAdapter(DateTimeUtil.getEventsTime(null, DateTimeUtil.get24HourList()))
        binding.addEvent.visibility = if (activity is DetailActivity) View.GONE else View.VISIBLE
        binding.addEvent.setOnClickListener {
            AddEventDialog(
                this,
                DateTimeUtil.eventCategories,
                DateTimeUtil.getDefaultRecursiveDays()
            ).show(
                (activity as HomeActivity).supportFragmentManager,
                "AddEventDialog"
            )
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpcomingEventsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpcomingEventsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun addToCalenderAPI(eventDetail: EventDetail) {
        viewModel.addRecursiveDayEvent(eventDetail)
    }

    override fun getSelectedDay(day: Int) {
        cal.add(Calendar.DATE, day)
        viewModel.getAllUpcomingEvents(cal.get(Calendar.DAY_OF_MONTH),
            cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())!!,
            cal.get(Calendar.YEAR))
        if (day > 0)
            cal.add(Calendar.DATE, -day)
    }
}