package com.tft.selfbest.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FilterSearchCourseDialogBinding
import com.tft.selfbest.models.mycourse.FilterSearchCourse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseFilterDialog(
    private val filterSearchCourse: FilterSearchCourse,
    private val applyFilterListener: ApplyFilterListener
) : BottomSheetDialogFragment(),
    View.OnClickListener {
    lateinit var binding: FilterSearchCourseDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FilterSearchCourseDialogBinding.inflate(inflater, container, false)
        binding.free.isChecked = filterSearchCourse.minAmount == 0
        binding.paid.isChecked = filterSearchCourse.maxAmount != 0
        if (filterSearchCourse.length[0] != "any") {
            binding.longDuration.isChecked = filterSearchCourse.length.contains("long")
            binding.mediumDuration.isChecked = filterSearchCourse.length.contains("medium")
            binding.shortDuration.isChecked = filterSearchCourse.length.contains("short")
        }
        if (filterSearchCourse.providers.isNotEmpty()) {
            binding.udemy.isChecked = filterSearchCourse.providers.contains("Udemy")
            binding.youtube.isChecked = filterSearchCourse.providers.contains("Youtube")
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.cancelFilter.setOnClickListener(this)
        binding.applyFilter.setOnClickListener(this)
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.cancel_filter -> {
                dialog?.dismiss()
            }
            R.id.apply_filter -> {
                filterSearchCourse.minAmount = if (binding.free.isChecked) 0 else 1
                filterSearchCourse.maxAmount = if (binding.paid.isChecked) 10000 else 0
                filterSearchCourse.length.clear()
                filterSearchCourse.providers.clear()
                if (!binding.longDuration.isChecked && !binding.mediumDuration.isChecked && !binding.shortDuration.isChecked) {
                    filterSearchCourse.length.add("any")
                } else {
                    if (binding.longDuration.isChecked) {
                        filterSearchCourse.length.add("long")
                    }
                    if (binding.mediumDuration.isChecked) {
                        filterSearchCourse.length.add("medium")
                    }
                    if (binding.shortDuration.isChecked) {
                        filterSearchCourse.length.add("short")
                    }
                }
                if (binding.udemy.isChecked) {
                    filterSearchCourse.providers.add("Udemy")
                }
                if (binding.youtube.isChecked) {
                    filterSearchCourse.providers.add("Youtube")
                }
                applyFilterListener.filterData(filterSearchCourse)
                dialog?.dismiss()
            }
        }
    }

    interface ApplyFilterListener {
        fun filterData(filterSearchCourse: FilterSearchCourse)
    }
}