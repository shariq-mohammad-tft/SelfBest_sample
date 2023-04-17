package com.tft.selfbest.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tft.selfbest.ui.fragments.activityLog.AnsweredQuery
import com.tft.selfbest.ui.fragments.activityLog.AskedQuery

class ViewPagerQueryAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val startDate: String,
    val endDate: String,
    val type: String
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val page_count = 2
    override fun getItemCount(): Int {
        return page_count
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AskedQuery(startDate, endDate, type)
            }
            1 -> {
                AnsweredQuery(startDate, endDate, type)
            }

            else -> {
                AskedQuery(startDate, endDate, type)
            }
        }
    }
}