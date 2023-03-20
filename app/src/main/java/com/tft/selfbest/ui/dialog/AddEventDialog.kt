package com.tft.selfbest.ui.dialog

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tft.selfbest.R
import com.tft.selfbest.databinding.AddEventDialogBinding
import com.tft.selfbest.models.calendarEvents.EventDetail
import com.tft.selfbest.models.calendarEvents.RecursiveDays
import com.tft.selfbest.ui.adapter.RecursiveDaysAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEventDialog(
    private val addEventListener: AddEventListener,
    private val eventCategories: List<String>,
    private val recursiveDays: ArrayList<RecursiveDays>,
) : BottomSheetDialogFragment(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {
    lateinit var mTimePicker: TimePickerDialog
    val sCurrentTime = Calendar.getInstance()
    var sHour = sCurrentTime.get(Calendar.HOUR_OF_DAY)
    var sMinute = sCurrentTime.get(Calendar.MINUTE)
    var eHour = sCurrentTime.get(Calendar.HOUR_OF_DAY)
    var eMinute = sCurrentTime.get(Calendar.MINUTE)
    lateinit var binding: AddEventDialogBinding
    var isLessSelectedStartTime = false
    var isLessSelectedEndTime = false
    var selectedStartTime = ""
    var selectedEndTime = ""
    var type = "Reminder"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = AddEventDialogBinding.inflate(inflater, container, false)
        binding.recursiveDayList.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recursiveDayList.adapter = RecursiveDaysAdapter(binding.root.context, recursiveDays)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        binding.cancelEvent.setOnClickListener(this)
        binding.sendData.setOnClickListener(this)
        binding.startTimeContainer.setOnClickListener(this)
        binding.endTimeContainer.setOnClickListener(this)
        binding.spinner.onItemSelectedListener = this
        val spinAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_main_item_style,
            eventCategories
        )
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style)
        binding.spinner.adapter = spinAdapter
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.cancel_event -> {
                dialog!!.dismiss()
            }
            R.id.send_data -> {
                if (selectedStartTime == "" || selectedEndTime == "") {
                    Toast.makeText(
                        binding.root.context,
                        "Please select start time or end time first",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                val text: String =
                    if (type == "Reminder") binding.eventDetail.text.toString() else type
                val getUpdatedList = (binding.recursiveDayList.adapter as RecursiveDaysAdapter).list
                val recurrenceRule = getRecursiveDate(getUpdatedList)
                addEventListener.addToCalenderAPI(
                    EventDetail(
                        selectedStartTime, selectedEndTime,
                        text, false, type, recurrenceRule
                    )
                )
                dialog!!.dismiss()
            }
            R.id.start_time_container -> {
                mTimePicker =
                    TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minutes: Int) {
                            sHour = hourOfDay
                            sMinute = minutes
                            val suffix = if (hourOfDay > 11) "pm" else "am"
                            val selectedHour =
                                if (hourOfDay > 12) hourOfDay - 12 else if (hourOfDay == 0) 12 else hourOfDay
                            val timeIn12AmPm = String.format(
                                "%s : %s %s",
                                if (selectedHour < 10) "0$selectedHour" else selectedHour.toString(),
                                if (minutes < 10) "0$minutes" else minutes.toString(),
                                suffix
                            )
                            binding.startTime.text = timeIn12AmPm
                            sCurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            sCurrentTime.set(Calendar.MINUTE, minutes)
                            sCurrentTime.timeZone = TimeZone.getDefault()
                            selectedStartTime = SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                                Locale.getDefault()
                            ).format((sCurrentTime as GregorianCalendar).time)
                        }
                    }, sHour, sMinute, false)
                mTimePicker.show()
            }
            R.id.end_time_container -> {
                if (selectedStartTime == "") {
                    Toast.makeText(
                        binding.root.context,
                        "Please select start time first",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                mTimePicker =
                    TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minutes: Int) {
                            eHour = hourOfDay
                            eMinute = minutes
                            if (sHour > eHour || (sHour == eHour && sMinute > eMinute)) {
                                Toast.makeText(
                                    binding.root.context,
                                    "End time must be greater than start time",
                                    Toast.LENGTH_LONG
                                ).show()
                                return
                            }

                            val suffix = if (hourOfDay > 11) "pm" else "am"
                            val selectedHour =
                                if (hourOfDay > 12) hourOfDay - 12 else if (hourOfDay == 0) 12 else hourOfDay
                            val timeIn12AmPm = String.format(
                                "%s : %s %s",
                                if (selectedHour < 10) "0$selectedHour" else selectedHour.toString(),
                                if (minutes < 10) "0$minutes" else minutes.toString(),
                                suffix
                            )
                            binding.endTime.text = timeIn12AmPm
                            sCurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            sCurrentTime.set(Calendar.MINUTE, minutes)
                            sCurrentTime.timeZone = TimeZone.getDefault()
                            selectedEndTime = SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                                Locale.getDefault()
                            ).format((sCurrentTime as GregorianCalendar).time)
                        }
                    }, eHour, eMinute, false)
                mTimePicker.show()
            }
        }
    }

    interface AddEventListener {
        fun addToCalenderAPI(eventDetail: EventDetail)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        binding.eventDetail.visibility = if (p2 == 0) View.VISIBLE else View.GONE
        binding.eventTitle.visibility = if (p2 == 0) View.VISIBLE else View.GONE
        type = eventCategories[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(binding.root.context, "Nothing selected", Toast.LENGTH_LONG).show()
    }

    fun getRecursiveDate(list: ArrayList<RecursiveDays>): String {
        val recursiveRuleSuffix = "FREQ=WEEKLY;BYDAY="
        val selectedRecursiveDays = StringBuffer()
        for (recursiveDay in list) {
            if (recursiveDay.isSelected) {
                selectedRecursiveDays.append(recursiveDay.day).append(",")
            }
        }
        selectedRecursiveDays.deleteCharAt(selectedRecursiveDays.length - 1)
        return recursiveRuleSuffix + selectedRecursiveDays.toString()
    }
}