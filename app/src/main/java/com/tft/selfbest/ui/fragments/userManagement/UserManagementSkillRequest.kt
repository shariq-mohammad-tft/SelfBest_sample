package com.tft.selfbest.ui.fragments.userManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentUserManagementSkillRequestBinding
import com.tft.selfbest.ui.adapter.ViewPagerAdapter
import com.tft.selfbest.ui.adapter.ViewPagerSkillAdapter

class UserManagementSkillRequest : Fragment() {

    lateinit var binding: FragmentUserManagementSkillRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentUserManagementSkillRequestBinding.inflate(layoutInflater)
        val adapter= activity?.let { ViewPagerSkillAdapter(it.supportFragmentManager,lifecycle) }
        binding.viewpager.adapter=adapter
        binding.tablayout.tabMode= TabLayout.MODE_FIXED

        TabLayoutMediator(binding.tablayout,binding.viewpager){tab,position->
            when(position){
                0->{
                    tab.text="Pending"
                }
                1->{
                    tab.text="Accepted"
                }
            }

        }.attach()
        return binding.root
    }

}