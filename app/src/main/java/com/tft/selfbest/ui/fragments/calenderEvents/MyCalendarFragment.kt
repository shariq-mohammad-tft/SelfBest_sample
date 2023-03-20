package com.tft.selfbest.ui.fragments.calenderEvents

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentMyCalendarBinding
import com.tft.selfbest.models.calendarEvents.EventDetail
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.HomeActivity
import com.tft.selfbest.ui.adapter.MyCalendarEventsAdapter
import com.tft.selfbest.ui.dialog.AddEventDialog
import com.tft.selfbest.utils.DateTimeUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyCalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MyCalendarFragment : Fragment(), View.OnClickListener, AddEventDialog.AddEventListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentMyCalendarBinding
    val viewModel by viewModels<CalendarEventsViewModel>()
    val myCalendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MMM-yyyy")
    lateinit var datePickerDialog: DatePickerDialog

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
        binding = FragmentMyCalendarBinding.inflate(layoutInflater, container, false)
        viewModel.getAllUpcomingEvents(
            myCalendar.get(Calendar.DAY_OF_MONTH),
            myCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
            myCalendar.get(Calendar.YEAR)
        )
        binding.dateText.text = dateFormat.format(myCalendar.time)
        binding.eventList.layoutManager = LinearLayoutManager(binding.root.context)
        viewModel.upComingEventsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.eventList.adapter =
                    MyCalendarEventsAdapter(it.data as ArrayList<EventDetail>)
            }
        }
        viewModel.addEventObserver.observe(viewLifecycleOwner) {
            Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
        }
        binding.arrowBack.setOnClickListener(this)
        binding.arrowNext.setOnClickListener(this)
        binding.dateText.setOnClickListener(this)
        binding.addEvent.setOnClickListener(this)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EventsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyCalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.arrow_back -> {
                myCalendar.add(Calendar.DATE, -1)
                viewModel.getAllUpcomingEvents(
                    myCalendar.get(Calendar.DAY_OF_MONTH),
                    myCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
                    myCalendar.get(Calendar.YEAR)
                )
                binding.dateText.text = dateFormat.format(myCalendar.time)
            }
            R.id.arrow_next -> {
                myCalendar.add(Calendar.DATE, +1)
                viewModel.getAllUpcomingEvents(
                    myCalendar.get(Calendar.DAY_OF_MONTH),
                    myCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
                    myCalendar.get(Calendar.YEAR)
                )
                binding.dateText.text = dateFormat.format(myCalendar.time)
            }
            R.id.date_text -> {
                datePickerDialog = DatePickerDialog(
                    binding.root.context,
                    object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(
                            p0: DatePicker?, year: Int, monthOfYear: Int,
                            dayOfMonth: Int
                        ) {
                            myCalendar.set(Calendar.YEAR, year)
                            myCalendar.set(Calendar.MONTH, monthOfYear)
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            binding.dateText.text = dateFormat.format(myCalendar.time)
                            viewModel.getAllUpcomingEvents(
                                myCalendar.get(Calendar.DAY_OF_MONTH),
                                myCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
                                myCalendar.get(Calendar.YEAR)
                            )
                        }
                    },
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
            }
            R.id.add_event -> {
                AddEventDialog(
                    this,
                    DateTimeUtil.eventCategories,
                    DateTimeUtil.getDefaultRecursiveDays()
                ).show(
                    (activity as HomeActivity).supportFragmentManager,
                    "AddEventDialog"
                )
            }
        }
    }

    override fun addToCalenderAPI(eventDetail: EventDetail) {
        viewModel.addRecursiveDayEvent(eventDetail)
    }
}