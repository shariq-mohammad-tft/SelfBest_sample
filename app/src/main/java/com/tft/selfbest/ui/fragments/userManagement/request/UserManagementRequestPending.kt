package com.tft.selfbest.ui.fragments.userManagement.request

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentUserManagementRequestPendingBinding
import com.tft.selfbest.models.ChangeRequestStatus
import com.tft.selfbest.models.RequestStatus
import com.tft.selfbest.models.UserRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.AcceptedRequestAdapter
import com.tft.selfbest.ui.adapter.QueryResponseAdapter
import com.tft.selfbest.ui.adapter.RequestAdapter
import com.tft.selfbest.ui.fragments.statistics.StatisticsViewModel
import com.tft.selfbest.ui.fragments.userManagement.UserManagementViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManagementRequestPending : Fragment(), RequestAdapter.ChangeRequestListener {

    lateinit var binding: FragmentUserManagementRequestPendingBinding
    val viewModel by viewModels<UserManagementViewModel>()
    lateinit var requests : List<UserRequest>
    var isData = false
    var pendingRequests : MutableList<UserRequest> = mutableListOf()
    lateinit var adapter: RequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentUserManagementRequestPendingBinding.inflate(layoutInflater)
        searchViewListener()
        viewModel.getUserRequests("pending")
        viewModel.userRequestsDataObserver.observe(viewLifecycleOwner){
            if(it is NetworkResponse.Success){
                binding.progress.visibility = View.GONE
                requests = it.data!!
                if(requests.isNotEmpty()){
                    binding.cbContainer.visibility = View.VISIBLE
                    isData = true
                }
//                for(req in requests){
//                    if(req.status.equals("pending")){
//                        pendingRequests.add(req)
//                    }
//                }

                binding.pendingRequest.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = RequestAdapter(
                    binding.root.context,
                    requests,
                    binding.checkBox.isChecked,
                    this
                )
                binding.pendingRequest.adapter = adapter
            }
        }
//        viewModel.skillRequestChangeDataObserver.observe(viewLifecycleOwner){
//            if(it is NetworkResponse.Success){
//                Toast.makeText(context!!, "Data Changed Successfully", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.checkBox.setOnClickListener(View.OnClickListener {
            if(binding.checkBox.isChecked){
                binding.selected.visibility = View.VISIBLE
                binding.pendingRequest.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = RequestAdapter(
                    binding.root.context,
                    requests,
                    binding.checkBox.isChecked,
                    this
                )
                binding.pendingRequest.adapter = adapter
            }else{
                binding.selected.visibility = View.GONE
                binding.pendingRequest.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = RequestAdapter(
                    binding.root.context,
                    requests,
                    binding.checkBox.isChecked,
                    this
                )
                binding.pendingRequest.adapter = adapter
            }
        })

        binding.rejectAll.setOnClickListener(View.OnClickListener {
            val rejectAllList = mutableListOf<RequestStatus>()
            for(req in requests){
                rejectAllList.add(RequestStatus("Rejected", req.userid, req.userType))
            }
            viewModel.changeRequestStatus(ChangeRequestStatus(
                false, rejectAllList
            ))
        })

        binding.acceptAll.setOnClickListener(View.OnClickListener {
            val acceptAllList = mutableListOf<RequestStatus>()
            for(req in requests){
                acceptAllList.add(RequestStatus("accepted", req.userid, req.userType))
            }
            viewModel.changeRequestStatus(ChangeRequestStatus(
                false, acceptAllList
            ))
        })

        return binding.root
    }



    override fun changeRequest(request: ChangeRequestStatus) {
        viewModel.changeRequestStatus(request)
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