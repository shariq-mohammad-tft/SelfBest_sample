package com.tft.selfbest.ui.fragments.userManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentUserManagementCertificateBinding
import com.tft.selfbest.ui.adapter.ViewPagerAdapterCertificate

class UserManagementCertificate : Fragment() {

    lateinit var binding: FragmentUserManagementCertificateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentUserManagementCertificateBinding.inflate(layoutInflater)

        val adapter= activity?.let { ViewPagerAdapterCertificate(it.supportFragmentManager,lifecycle) }
        binding.viewpagerCertificate.adapter=adapter
        binding.tablayoutCertificate.tabMode= TabLayout.MODE_FIXED

        TabLayoutMediator(binding.tablayoutCertificate,binding.viewpagerCertificate){tab,position->
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