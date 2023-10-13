package com.tft.selfbest.ui.fragments.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentHomeBinding
import com.tft.selfbest.models.overview.OverViewActivityResponse
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.HomeActivity
import com.tft.selfbest.ui.adapter.InstalledAppsAdapter
import com.tft.selfbest.ui.adapter.ProgressLevelAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var levelAdapter: ProgressLevelAdapter
    private lateinit var installedAppsAdapter: InstalledAppsAdapter
    val viewModel by viewModels<HomeViewModel>()
    var overViewActivityResponse: OverViewActivityResponse? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        levelAdapter = ProgressLevelAdapter(
            listOf(
                "",
                "",
                "Level 01",
                "Level 02",
                "Level 03"
            ), 3
        )
        binding.levelList.layoutManager = LinearLayoutManager(activity as HomeActivity)
        binding.levelList.adapter = levelAdapter
        val layoutManagerCourse = FlexboxLayoutManager(binding.root.context)
        layoutManagerCourse.flexDirection = FlexDirection.ROW
        layoutManagerCourse.justifyContent = JustifyContent.FLEX_START

        binding.detailList.layoutManager = layoutManagerCourse
        viewModel.getOverViewLevel()
        viewModel.overViewLevelObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.progressBar.progress = it.data?.currentProgress!!.roundToInt()
                binding.progressValue.text = "${it.data.currentProgress!!.roundToInt()}%"
                val currentLevel = it.data.level
                val list = listOf(
                    if (currentLevel - 2 > 0) "Level " + (currentLevel - 2) else "",
                    if (currentLevel - 1 > 0) "Level " + (currentLevel - 1) else "",
                    "Level $currentLevel",
                    "Level " + (currentLevel + 1),
                    "Level " + (currentLevel + 2)
                )
                binding.levelList.adapter = ProgressLevelAdapter(list, 3)
                levelAdapter.notifyDataSetChanged()
            }
        }
        viewModel.getOverviewActivity()
        viewModel.getTotalDistractedTime()
        viewModel.getTotalCompletedTime()
        viewModel.overViewCompletedObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                if (it.data != null) {
                    val completedHrMinute =
                        it.data.codeTime + it.data.documentationTime + it.data.learningTime
                    binding.completedHr.text = (completedHrMinute).roundToInt().toString()
                    binding.completedM.text =
                        (((completedHrMinute * 100).roundToInt() % 100) * 3 / 5).toString()
                }
            }
        }
        viewModel.overViewDistractedObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                val distractedHrMinute = (it.data?.totalDistractedTime).toString().split(":")
                binding.distractedHr.text = distractedHrMinute[0]
                binding.distractedM.text = distractedHrMinute[1]
            }
        }
        viewModel.overViewActivityObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                overViewActivityResponse = it.data
            }
        }
        binding.unSelectedCourse.setOnClickListener(this)
        binding.unSelectedCode.setOnClickListener(this)
        binding.unSelectedDocument.setOnClickListener(this)
        binding.unSelectedOther.setOnClickListener(this)
        binding.selectedCourse.setOnClickListener(this)
        binding.selectedCode.setOnClickListener(this)
        binding.selectedDocument.setOnClickListener(this)
        binding.selectedOther.setOnClickListener(this)
        binding.detailSelectedItem.setOnClickListener(this)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(selectedView: View?) {
        when (selectedView?.id) {
            binding.unSelectedCourse.id -> {
                binding.activityCardsContainer.visibility = View.GONE
                binding.selectedActivityCardsContainer.visibility = View.VISIBLE
                binding.detailSelectedItem.visibility = View.VISIBLE
                binding.selectedCourse.background =
                    resources.getDrawable(R.drawable.selected_overview_activity_card)
                binding.selectedCode.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedDocument.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedOther.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.detailSelectedItem.background =
                    resources.getDrawable(R.drawable.left_open_corner_overview_card)

            }
            binding.unSelectedCode.id -> {
                binding.detailList.adapter = InstalledAppsAdapter(
                    listOf(
                        "Code 1"
                    )
                )
                binding.activityCardsContainer.visibility = View.GONE
                binding.selectedActivityCardsContainer.visibility = View.VISIBLE
                binding.detailSelectedItem.visibility = View.VISIBLE
                binding.selectedCode.background =
                    resources.getDrawable(R.drawable.selected_overview_activity_card)
                binding.selectedCourse.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedDocument.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedOther.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.detailSelectedItem.background =
                    resources.getDrawable(R.drawable.overview_activity_card)
            }
            binding.unSelectedDocument.id -> {
                binding.detailList.adapter =
                    InstalledAppsAdapter(overViewActivityResponse!!.documentation)
                binding.activityCardsContainer.visibility = View.GONE
                binding.selectedActivityCardsContainer.visibility = View.VISIBLE
                binding.detailSelectedItem.visibility = View.VISIBLE
                binding.selectedCode.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedCourse.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedDocument.background =
                    resources.getDrawable(R.drawable.selected_overview_activity_card)
                binding.selectedOther.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.detailSelectedItem.background =
                    resources.getDrawable(R.drawable.overview_activity_card)
            }
            binding.unSelectedOther.id -> {
                binding.detailList.adapter = InstalledAppsAdapter(overViewActivityResponse!!.others)
                binding.activityCardsContainer.visibility = View.GONE
                binding.selectedActivityCardsContainer.visibility = View.VISIBLE
                binding.detailSelectedItem.visibility = View.VISIBLE
                binding.selectedCode.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedCourse.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedDocument.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedOther.background =
                    resources.getDrawable(R.drawable.selected_overview_activity_card)
                binding.detailSelectedItem.background =
                    resources.getDrawable(R.drawable.right_open_corner_overview_card)
            }
            binding.selectedCourse.id -> {
                binding.selectedCode.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedCourse.background =
                    resources.getDrawable(R.drawable.selected_overview_activity_card)
                binding.selectedDocument.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedOther.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.detailSelectedItem.background =
                    resources.getDrawable(R.drawable.left_open_corner_overview_card)
            }
            binding.selectedCode.id -> {
                binding.detailList.adapter = InstalledAppsAdapter(overViewActivityResponse!!.code)
                binding.selectedCode.background =
                    resources.getDrawable(R.drawable.selected_overview_activity_card)
                binding.selectedCourse.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedDocument.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedOther.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.detailSelectedItem.background =
                    resources.getDrawable(R.drawable.overview_activity_card)
            }
            binding.selectedDocument.id -> {
                binding.detailList.adapter =
                    InstalledAppsAdapter(overViewActivityResponse!!.documentation)
                binding.selectedCode.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedCourse.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedDocument.background =
                    resources.getDrawable(R.drawable.selected_overview_activity_card)
                binding.selectedOther.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.detailSelectedItem.background =
                    resources.getDrawable(R.drawable.overview_activity_card)
            }
            binding.selectedOther.id -> {
                binding.detailList.adapter = InstalledAppsAdapter(overViewActivityResponse!!.others)
                binding.selectedCode.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedCourse.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedDocument.background =
                    resources.getDrawable(R.drawable.unselected_overview_activity_card)
                binding.selectedOther.background =
                    resources.getDrawable(R.drawable.selected_overview_activity_card)
                binding.detailSelectedItem.background =
                    resources.getDrawable(R.drawable.right_open_corner_overview_card)
            }
            else -> { // Note the block
                binding.activityCardsContainer.visibility = View.VISIBLE
                binding.selectedActivityCardsContainer.visibility = View.GONE
                binding.detailSelectedItem.visibility = View.GONE
            }
        }
    }
}