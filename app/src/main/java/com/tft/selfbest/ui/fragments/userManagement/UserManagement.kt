package com.tft.selfbest.ui.fragments.userManagement

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentUserManagementBinding
import com.tft.selfbest.ui.adapter.ViewPagerAdapter
import com.tft.selfbest.ui.fragments.profile.ProfileFragment
import com.tft.selfbest.ui.fragments.settings.SettingFragment
import com.tft.selfbest.ui.fragments.userManagement.request.UserManagementRequest
import com.tft.selfbest.utils.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManagement : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentUserManagementBinding
    val viewModel by viewModels<UserManagementViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkNetworkConnectivity()
        // Inflate the layout for this fragment
        binding= FragmentUserManagementBinding.inflate(layoutInflater)
        loadFragment(UserManagementRequest())
        binding.requests.setTextColor(Color.WHITE)
        binding.requests.setBackgroundResource(R.drawable.user_mgmt_selected_part_bg)
        //searchViewListener()
        val adapter= activity?.let { ViewPagerAdapter(it.supportFragmentManager,lifecycle) }
//        binding.viewpager.adapter=adapter
//        binding.tablayout.tabMode=TabLayout.MODE_FIXED

//        TabLayoutMediator(binding.tablayout,binding.viewpager){tab,position->
//            when(position){
//                0->{
//                    tab.text="Pending"
//                }
//                1->{
//                    tab.text="Accepted"
//                }
//                2->{
//                    tab.text= "Rejected"
//                }
//
//            }
//
//        }.attach()
//
//        for(i in 0..2){
//            val textView=LayoutInflater.from(requireContext()).inflate(R.layout.tab_title,null) as TextView
//            binding.tablayout.getTabAt(i)?.customView=textView
//        }
        setListeners()
        return binding.root

    }

    private fun setListeners(){
        binding.requests.setOnClickListener(this)
        binding.certificate.setOnClickListener(this)
        binding.skill.setOnClickListener(this)
        binding.accountSet.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.requests -> {
                binding.requests.setTextColor(Color.WHITE)
                binding.requests.setBackgroundResource(R.drawable.user_mgmt_selected_part_bg)
                binding.skill.setTextColor(Color.BLACK)
                binding.skill.setBackgroundColor(Color.TRANSPARENT)
                binding.certificate.setTextColor(Color.BLACK)
                binding.certificate.setBackgroundColor(Color.TRANSPARENT)
                binding.accountSet.setTextColor(Color.BLACK)
                binding.accountSet.setBackgroundColor(Color.TRANSPARENT)
                binding.certificate.text = "Certificate"
                binding.accountSet.text = "Account"
                loadFragment(UserManagementRequest())
            }

            R.id.certificate->{
                binding.certificate.text = "Certificate"
                binding.certificate.setTextColor(Color.WHITE)
                binding.certificate.setBackgroundResource(R.drawable.user_mgmt_selected_part_bg)
                binding.requests.setTextColor(Color.BLACK)
                binding.requests.setBackgroundColor(Color.TRANSPARENT)
                binding.skill.setTextColor(Color.BLACK)
                binding.skill.setBackgroundColor(Color.TRANSPARENT)
                binding.accountSet.setTextColor(Color.BLACK)
                binding.accountSet.setBackgroundColor(Color.TRANSPARENT)
                binding.accountSet.text = "Account"
                loadFragment(UserManagementCertificate())



            }
            R.id.skill->{
                binding.skill.setTextColor(Color.WHITE)
                binding.skill.setBackgroundResource(R.drawable.user_mgmt_selected_part_bg)
                binding.certificate.setTextColor(Color.BLACK)
                binding.certificate.setBackgroundColor(Color.TRANSPARENT)
                binding.requests.setTextColor(Color.BLACK)
                binding.requests.setBackgroundColor(Color.TRANSPARENT)
                binding.accountSet.setTextColor(Color.BLACK)
                binding.accountSet.setBackgroundColor(Color.TRANSPARENT)
                binding.certificate.text = "Certificate"
                binding.accountSet.text = "Account"
                loadFragment(UserManagementSkillRequest())

            }
            R.id.account_Set->{
                binding.accountSet.text = "Account"
                binding.accountSet.setTextColor(Color.WHITE)
                binding.accountSet.setBackgroundResource(R.drawable.user_mgmt_selected_part_bg)
                binding.certificate.setTextColor(Color.BLACK)
                binding.certificate.setBackgroundColor(Color.TRANSPARENT)
                binding.requests.setTextColor(Color.BLACK)
                binding.requests.setBackgroundColor(Color.TRANSPARENT)
                binding.skill.setTextColor(Color.BLACK)
                binding.skill.setBackgroundColor(Color.TRANSPARENT)
                binding.certificate.text = "Certificate"
                loadFragment(UserManagementAccountSetting())
            }

        }
    }


//    private fun searchViewListener(){
//        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            android.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (!query.isNullOrEmpty()) {
//                    searchViewModel.setQuery(query)
//                    return false
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(query: String): Boolean {
//                if (query.isEmpty()) {
//                    return false
//                }
//                return true
//            }
//        })
//    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.user_management_fragment_container, fragment)
        transaction.commit()
    }

    private fun loadFragmentWhenInternetIsNotAvailable(fragment: Fragment) {
        // load fragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.commit()
    }
    private fun checkNetworkConnectivity(){
        if(!requireContext().isInternetAvailable()){
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Please check your internet connection and try again.")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    loadFragmentWhenInternetIsNotAvailable(SettingFragment())
                }
            builder.create().show()
        }
    }
}