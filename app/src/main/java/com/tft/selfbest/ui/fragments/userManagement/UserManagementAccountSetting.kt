package com.tft.selfbest.ui.fragments.userManagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentUserManagementAccountSettingBinding
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.DeleteRequestsAdapter
import com.tft.selfbest.ui.adapter.RequestAdapter
import com.tft.selfbest.ui.adapter.ViewPagerSkillAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManagementAccountSetting : Fragment(),
    DeleteRequestsAdapter.ChangeAccountRequestListener {
    lateinit var binding: FragmentUserManagementAccountSettingBinding
    val viewModel by viewModels<UserManagementViewModel>()
    var isData = false
    lateinit var deleteRequests: List<DeleteAccountResponse>
    lateinit var adapter: DeleteRequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserManagementAccountSettingBinding.inflate(layoutInflater)
        viewModel.getDeleteAccounts()
        viewModel.deleteRequestsDataObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                Log.e("Requests", "Progress Bar")
                binding.progress.visibility = View.GONE
                deleteRequests = it.data!!
                if (deleteRequests.isNotEmpty()) {
                    isData = true
                    binding.cbContainer.visibility = View.VISIBLE
                    binding.requests.layoutManager = LinearLayoutManager(binding.root.context)
                    adapter = DeleteRequestsAdapter(
                        binding.root.context,
                        deleteRequests,
                        binding.checkBox.isChecked,
                        this
                    )
                    binding.requests.adapter = adapter
                }
            }
        }

        binding.checkBox.setOnClickListener(View.OnClickListener {
            if(binding.checkBox.isChecked){
                binding.selected.visibility = View.VISIBLE
                binding.requests.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = DeleteRequestsAdapter(
                    binding.root.context,
                    deleteRequests,
                    binding.checkBox.isChecked,
                    this
                )
                binding.requests.adapter = adapter
            }else{
                binding.selected.visibility = View.GONE
                binding.requests.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = DeleteRequestsAdapter(
                    binding.root.context,
                    deleteRequests,
                    binding.checkBox.isChecked,
                    this
                )
                binding.requests.adapter = adapter
            }
        })

        binding.rejectAll.setOnClickListener(View.OnClickListener {
            val rejectAllList = mutableListOf<AccountRequestBody>()
            for(req in deleteRequests){
                rejectAllList.add(AccountRequestBody(req.email, req.type))
            }
            viewModel.changeAccountRequest(
                ChangeAccountRequestBody(
                    "Reject",
                    rejectAllList
                )
            )
        })

        binding.acceptAll.setOnClickListener(View.OnClickListener {
            val acceptAllList = mutableListOf<AccountRequestBody>()
            for(req in deleteRequests){
                acceptAllList.add(AccountRequestBody(req.email, req.type))
            }
            viewModel.changeAccountRequest(
                ChangeAccountRequestBody(
                    "Accept",
                    acceptAllList
                )
            )
        })

//        val adapter= activity?.let { ViewPagerSkillAdapter(it.supportFragmentManager,lifecycle) }
//        binding.viewpager.adapter=adapter
//        binding.tablayout.tabMode= TabLayout.MODE_FIXED
//
//        TabLayoutMediator(binding.tablayout,binding.viewpager){tab,position->
//            when(position){
//                0->{
//                    tab.text="Pending"
//                }
//                1->{
//                    tab.text="Accepted"
//                }
//            }
//
//        }.attach()
        return binding.root
    }

    override fun changeAccountRequest(request: ChangeAccountRequestBody) {
        viewModel.changeAccountRequest(request)
    }

    private fun searchViewListener(){
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty() && isData) {
                    Log.e("Query", "1")
                    adapter.filter.filter(query)
                    return false
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isNotEmpty() && isData) {
                    val searchViewIcon: ImageView =
                        binding.search.findViewById(R.id.search_close_btn) as ImageView
                    searchViewIcon.visibility = View.GONE
                    Log.e("Query", "1")
                    adapter.filter.filter(query)
                    return false
                }
                return true
            }
        })
    }

}