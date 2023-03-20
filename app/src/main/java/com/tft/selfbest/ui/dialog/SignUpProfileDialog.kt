package com.tft.selfbest.ui.dialog

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.internal.LinkedTreeMap
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentSignupProfileBinding
import com.tft.selfbest.models.ProfileWorkingData
import com.tft.selfbest.models.SignUpDetail
import com.tft.selfbest.models.calendarEvents.RecursiveDays
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.ProfileSkillsAdapter
import com.tft.selfbest.ui.adapter.RecursiveDaysAdapter
import com.tft.selfbest.ui.adapter.SignUpSkillListAdapter
import com.tft.selfbest.ui.fragments.profile.ProfileViewModel
import com.tft.selfbest.utils.DateTimeUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class SignUpProfileDialog() : DialogFragment(), View.OnClickListener, SignUpSkillListAdapter.ChangeRatingListener {
    lateinit var binding: FragmentSignupProfileBinding
    private val recursiveDays: ArrayList<RecursiveDays> = DateTimeUtil.getDefaultRecursiveDays()
    lateinit var mTimePicker: TimePickerDialog
    val sCurrentTime = Calendar.getInstance()
    var sHour = sCurrentTime.get(Calendar.HOUR_OF_DAY)
    var sMinute = sCurrentTime.get(Calendar.MINUTE)
    var eHour = sCurrentTime.get(Calendar.HOUR_OF_DAY)
    var eMinute = sCurrentTime.get(Calendar.MINUTE)
    var selectedStartTime = ""
    var selectedEndTime = ""
    private val viewModel by viewModels<ProfileViewModel>()
    lateinit var allSkills: List<String>
    private var profileSkills = LinkedTreeMap<String, Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.white_full_round_border)
        binding = FragmentSignupProfileBinding.inflate(inflater, container, false)
        binding.skillList.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.skillList.adapter =
            SignUpSkillListAdapter(
                binding.root.context,
                profileSkills,
                this
            )
        viewModel.getProfile("Window")
        viewModel.getJobs()
        viewModel.getSkills()
        viewModel.pSkillsObserver.observe(viewLifecycleOwner){
            if(it is NetworkResponse.Success){
                allSkills = it.data as ArrayList<String>
                Log.e("Skills List", "$allSkills")
                val adapter = ArrayAdapter(
                    binding.root.context,
                    android.R.layout.simple_list_item_1,
                    allSkills
                )
                binding.skill.setAdapter(adapter)
                binding.skill.threshold = 1
            }
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.recursiveDayList.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recursiveDayList.adapter = RecursiveDaysAdapter(binding.root.context, recursiveDays)
        setListeners()
    }

    private fun setListeners() {
        binding.startTimeContainer.setOnClickListener(this)
        binding.endTimeContainer.setOnClickListener(this)
        binding.skill.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.cancelEvent.setOnClickListener(this)
    }

    override fun changeRating(level: Float, skill: String){
        profileSkills[skill] = level.toInt()
        Log.e("Skill Level Changed Level : ", "$level")
        Log.e("Skill Level Changed", "$profileSkills")
    }

    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        TODO("Not yet implemented")
    }



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start_time_container -> {
                mTimePicker =
                    TimePickerDialog(context,
                        { _, hourOfDay, minutes ->
                            sHour = hourOfDay
                            sMinute = minutes
                            val suffix = if (hourOfDay > 11) "PM" else "AM"
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

                            val suffix = if (hourOfDay > 11) "PM" else "AM"
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
            R.id.skill -> {
                binding.skill.requestFocus()
                if (binding.skill.text.isEmpty()) {
                    Toast.makeText(
                        binding.root.context,
                        "Skill must be not null",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                profileSkills[binding.skill.text.toString()] = 1
                (binding.skillList.adapter as SignUpSkillListAdapter).addSkill(
                    binding.skill.text.toString(),
                    1
                )
                Toast.makeText(context, "Skill added successfully", Toast.LENGTH_SHORT).show()
            }
            R.id.cancel_event -> {
                dialog!!.dismiss()
            }
            R.id.save ->{
                val getUpdatedList = (binding.recursiveDayList.adapter as RecursiveDaysAdapter).list
                if(binding.jobPosition.text.equals("") || binding.linkdinContainer.text.equals("")){
                    Toast.makeText(activity, "All the fields are mandatory!!", Toast.LENGTH_SHORT).show()
                }else{
//                    viewModel.saveData(
//                        SignUpDetail(
//                            if(binding.experience.text.equals("")) 0 else binding.experience.text.toString().toInt(),
//                            binding.linkdinContainer.text.toString(),
//                            "Asia/Calcutta",
//                            binding.jobPosition.text.toString(),
//                            profileSkills,
//                            binding.endTime.text.toString(),
//                            binding.startTime.text.toString(),
//                            listOf()
//                        )
//                    )
                }
            }

        }
    }
        /*  when (v?.id) {
              R.id.cancel_feedback -> {
                  dialog!!.dismiss()
              }
              R.id.send_feedback -> {
                  *//*Toast.makeText(context, "Feed back has been sent to server", Toast.LENGTH_LONG)
                    .show()*//*

                *//* if (pref.getUserProfile == null) {
                     binding.checkName.visibility = View.GONE
                     // binding.checkMail.visibility = View.GONE
                     //binding.feedbackLengthCheck.visibility = View.GONE
                     if (!binding.emailContainer.text.isNullOrEmpty() && !binding.feedbackText.text.isNullOrEmpty()) {
                         val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                         if (!binding.emailContainer.text.matches(emailPattern.toRegex())) {
                             binding.emailContainer.requestFocus()
                             binding.checkMail.text = "Enter correct email"
                             binding.checkMail.visibility = View.VISIBLE
                             return
                         }
                         if (binding.feedbackText.text.toString().length < 20) {
                             binding.feedbackLengthCheck.requestFocus()
                             binding.feedbackLengthCheck.text = "Enter minimum 20 characters"
                             binding.feedbackLengthCheck.visibility = View.VISIBLE
                             return
                         }
                         viewModel.loadFeedbackResult(
                             binding.nameContainer.text.toString(),
                             binding.emailContainer.text.toString(),
                             binding.feedbackText.text.toString(), context
                         )
                     } else {
                         // binding.checkMail.visibility = View.GONE
                         // binding.feedbackLengthCheck.visibility = View.GONE
                         if (binding.nameContainer.text.isNullOrEmpty()) {
                             binding.nameContainer.requestFocus()
                             binding.checkName.visibility = View.VISIBLE
                             Toast.makeText(
                                 context,
                                 context?.getString(R.string.enter_name),
                                 Toast.LENGTH_LONG
                             )
                                 .show()
                             return
                         }
                         if (binding.emailContainer.text.isNullOrEmpty()) {
                             binding.checkMail.visibility = View.VISIBLE
                             binding.emailContainer.requestFocus()
                             binding.checkMail.text =
                                 requireContext().getString(R.string.enter_email)
                             Toast.makeText(
                                 context,
                                 requireContext().getString(R.string.enter_email),
                                 Toast.LENGTH_LONG
                             )
                                 .show()
                             return
                         } else if (binding.feedbackText.text.isNullOrEmpty()) {
                             binding.feedbackText.requestFocus()
                             binding.feedbackLengthCheck.visibility = View.VISIBLE
                             binding.feedbackLengthCheck.text =
                                 context?.getString(R.string.enter_feedback)
                             Toast.makeText(
                                 context,
                                 context?.getString(R.string.enter_feedback),
                                 Toast.LENGTH_LONG
                             )
                                 .show()
                             return
                         }

                     }
                 } else {
                     if (binding.feedbackText.text.toString().length < 20) {
                         binding.feedbackLengthCheck.requestFocus()
                         binding.feedbackLengthCheck.visibility = View.VISIBLE
                         binding.feedbackLengthCheck.text = "Enter minimum 20 characters"
                         return
                     }
                     viewModel.loadFeedbackResult(
                         JsonUtil.getUserProfile(pref).displayName,
                         pref.getUserEmail.toString(),
                         binding.feedbackText.text.toString(),
                         context
                     )
                 }*//*

            }
        }*/
//    }

    private fun setOnClickListener() {
        /* binding.cancelFeedback.setOnClickListener(this)
         binding.sendFeedback.setOnClickListener(this)*/
    }

    private fun setObservers() {
        /*viewModel.getDataObserver.observe(this, Observer {
            when (it) {
                is NetworkResponse.Success -> {
                    it.data?.let { it1 ->
                        dialog!!.dismiss()
                    }
                }
                is NetworkResponse.Error -> {
                    Toast.makeText(
                        context,
                        getString(R.string.feedback_error_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResponse.Loading -> {
                }
            }
        })*/
    }


}
