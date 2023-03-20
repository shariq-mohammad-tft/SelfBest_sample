package com.tft.selfbest.ui.fragments.userManagement.request

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentUserManagementRequestBinding
import com.tft.selfbest.ui.adapter.ViewPagerAdapter

class UserManagementRequest : Fragment() {

    lateinit var binding: FragmentUserManagementRequestBinding
    private val tabTitles= arrayListOf("Pending","Accepted", "Rejected")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentUserManagementRequestBinding.inflate(layoutInflater)
        //loadFragment(UserManagementPending())
        val adapter= activity?.let { ViewPagerAdapter(it.supportFragmentManager,lifecycle) }
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
                2->{
                    tab.text= "Rejected"
                }

            }

        }.attach()

        return binding.root
    }

}