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
import com.tft.selfbest.databinding.FragmentUserManagementPendingSkillBinding
import com.tft.selfbest.models.ChangeSkillRequestStatus
import com.tft.selfbest.models.SkillResponse
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.PendingSkillRequestAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManagementPendingSkill : Fragment(), PendingSkillRequestAdapter.ChangeSkillRequestListener {
    lateinit var binding: FragmentUserManagementPendingSkillBinding
    val viewModel by viewModels<UserManagementViewModel>()
    lateinit var skills: List<SkillResponse>
    val pendingSkills = mutableListOf<SkillResponse>()
    lateinit var adapter: PendingSkillRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserManagementPendingSkillBinding.inflate(layoutInflater)
        searchViewListener()
        viewModel.getSkillRequests()
        viewModel.skillRequestDataObserver.observe(viewLifecycleOwner){
            if(it is NetworkResponse.Success){
                binding.progress.visibility = View.GONE
                skills = it.data!!.skills
                for(sk in skills){
                    if(sk.status.equals("Pending") || sk.status.equals("pending")){
                        pendingSkills.add(sk)
                    }
                }
                binding.pendingSkill.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = PendingSkillRequestAdapter(
                    binding.root.context,
                    pendingSkills,
                    listOf(),
                    this
                )
                binding.pendingSkill.adapter = adapter
            }
        }
        viewModel.skillRequestStatus("")
        viewModel.skillsRequestDataObserver.observe(viewLifecycleOwner){
            Log.e("SKill Search", "2")
            if(it is NetworkResponse.Success){
                Log.e("SKill Search", "3")
                adapter = PendingSkillRequestAdapter(
                    binding.root.context,
                    pendingSkills,
                    it.data!!,
                    this
                )
                binding.pendingSkill.adapter = adapter
            }
        }
        viewModel.skillRequestChangeDataObserver.observe(viewLifecycleOwner){
            if(it is NetworkResponse.Success){
                Toast.makeText(requireContext(), "Skill replaced successfully", Toast.LENGTH_SHORT).show()
                viewModel.getSkillRequests()
            }
        }
        return binding.root
    }

    override fun changeSkillRequest(request: ChangeSkillRequestStatus) {
        viewModel.changeSkillRequestStatus(request)
    }

    private fun searchViewListener(){
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty() && pendingSkills.isNotEmpty()) {
                    Log.e("Query", "1")
                    adapter.filter.filter(query)
                    return false
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isNotEmpty() && pendingSkills.isNotEmpty()) {
                    Log.e("Query", "1")
                    val searchViewIcon: ImageView =
                        binding.search.findViewById(R.id.search_close_btn) as ImageView
                    searchViewIcon.visibility = View.GONE
                    adapter.filter.filter(query)
                    return false
                }
                return true
            }
        })
    }

//    override fun requestSkill(skill: String) {
//        Log.e("SKill Search", "1")
//        viewModel.skillRequestStatus(skill)
//    }
}