package com.tft.selfbest.ui.fragments.userManagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentUserManagementAcceptedSkillBinding
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.AcceptedSkillRequestAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManagementAcceptedSkill : Fragment(),
    AcceptedSkillRequestAdapter.ChangeSkillRequestListener {
    lateinit var binding: FragmentUserManagementAcceptedSkillBinding
    val viewModel by viewModels<UserManagementViewModel>()
    lateinit var skills: List<SkillResponse>
    val acceptedSkills = mutableListOf<SkillResponse>()
    lateinit var adapter: AcceptedSkillRequestAdapter
    var allSelected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserManagementAcceptedSkillBinding.inflate(layoutInflater)
        viewModel.getSkillRequests()
        searchViewListener()
        viewModel.skillRequestDataObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.progress.visibility = View.GONE
                skills = it.data!!.skills
                for (sk in skills) {
                    if (sk.status.equals("Accepted")) {
                        acceptedSkills.add(sk)
                    }
                }
                if (acceptedSkills.isNotEmpty()) {
                    binding.cbContainer.visibility = View.VISIBLE
                    binding.withData.visibility = View.VISIBLE
                    binding.withoutData.visibility = View.GONE
                }else{
                    binding.withData.visibility = View.GONE
                    binding.withoutData.visibility = View.VISIBLE
                    binding.animationView.playAnimation()
                }
                binding.acceptedSkill.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = AcceptedSkillRequestAdapter(
                    binding.root.context,
                    acceptedSkills,
                    binding.checkBox.isChecked,
                    this
                )
                binding.acceptedSkill.adapter = adapter
            }
        }
        viewModel.skillRequestChangeDataObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                Toast.makeText(requireContext(), "Skill replaced successfully", Toast.LENGTH_SHORT).show()
                viewModel.getSkillRequests()
            }
        }

        binding.checkBox.setOnClickListener(View.OnClickListener {
            if (binding.checkBox.isChecked) {
                allSelected = true
                binding.selected.visibility = View.VISIBLE
                binding.acceptedSkill.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = AcceptedSkillRequestAdapter(
                    binding.root.context,
                    acceptedSkills,
                    binding.checkBox.isChecked,
                    this
                )
                binding.acceptedSkill.adapter = adapter
            } else {
                allSelected = false
                binding.selected.visibility = View.GONE
                binding.acceptedSkill.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = AcceptedSkillRequestAdapter(
                    binding.root.context,
                    acceptedSkills,
                    binding.checkBox.isChecked,
                    this
                )
                binding.acceptedSkill.adapter = adapter
            }
        })

        binding.rejectAll.setOnClickListener(View.OnClickListener {
            val rejectAllList = mutableListOf<Int>()
            if(allSelected){
                for (req in acceptedSkills) {
                    rejectAllList.add(req.id)
                }
            }else{
                for (req in adapter.getSelectedItems()) {
                    rejectAllList.add(req.id)
                }
            }
            viewModel.changeSkillRequestStatus(
                ChangeSkillRequestStatus(
                    "", rejectAllList, "Rejected"
                )
            )
        })

        return binding.root
    }

    private fun updateButtonVisibility() {
        if(adapter.getSelectedItems().isNotEmpty() || allSelected){
            binding.selected.visibility = View.VISIBLE
        }else{
            binding.selected.visibility = View.GONE
        }
    }

    override fun changeSkillRequest(request: ChangeSkillRequestStatus) {
        viewModel.changeSkillRequestStatus(request)
    }

    override fun updateVisibility() {
        updateButtonVisibility()
    }


    private fun searchViewListener(){
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty() && acceptedSkills.isNotEmpty()) {
                    Log.e("Query", "1")
                    adapter.filter.filter(query)
                    val searchViewIcon: ImageView =
                        binding.search.findViewById(R.id.search_close_btn) as ImageView
                    searchViewIcon.visibility = View.GONE
                    return false
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isNotEmpty() && acceptedSkills.isNotEmpty()) {
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