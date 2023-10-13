package com.tft.selfbest.ui.fragments.userManagement.request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentUserManagementRequestAcceptBinding
import com.tft.selfbest.models.ChangeRequestStatus
import com.tft.selfbest.models.RequestStatus
import com.tft.selfbest.models.UserRequest
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.AcceptedRequestAdapter
import com.tft.selfbest.ui.fragments.userManagement.UserManagementViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManagementRequestAccept : Fragment(), AcceptedRequestAdapter.RejectRequestListener {

    lateinit var binding: FragmentUserManagementRequestAcceptBinding
    val viewModel by viewModels<UserManagementViewModel>()
    lateinit var requests: List<UserRequest>
    var isData = false
    var pendingRequests: MutableList<UserRequest> = mutableListOf()
    lateinit var adapter: AcceptedRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserManagementRequestAcceptBinding.inflate(layoutInflater)
        searchViewListener()
        viewModel.getUserRequests("accepted")
        viewModel.userRequestsDataObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                binding.progress.visibility = View.GONE
                requests = it.data!!
                if (requests.isNotEmpty()) {
                    binding.cbContainer.visibility = View.VISIBLE
                    binding.withData.visibility = View.VISIBLE
                    binding.withoutData.visibility = View.GONE
                    isData = true
                }else{
                    binding.withData.visibility = View.GONE
                    binding.withoutData.visibility = View.VISIBLE
                    binding.animationView.playAnimation()
                }
//                for(req in requests){
//                    if(req.status.equals("pending")){
//                        pendingRequests.add(req)
//                    }
//                }

                binding.pendingRequest.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = AcceptedRequestAdapter(
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
//                adapter.notifyItemRangeRemoved()
//            }
//        }
//        searchViewModel.getQuery().observe(viewLifecycleOwner, Observer<String> {
//            Log.e("Query", "1")
//            if (it != null) {
//                Log.e("Query", "2")
//                adapter.filter.filter(it);
//            }
//        }
//        )

        binding.checkBox.setOnClickListener(View.OnClickListener {
            if (binding.checkBox.isChecked) {
                binding.selected.visibility = View.VISIBLE
                binding.pendingRequest.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = AcceptedRequestAdapter(
                    binding.root.context,
                    requests,
                    binding.checkBox.isChecked,
                    this
                )
                binding.pendingRequest.adapter = adapter
            } else {
                binding.selected.visibility = View.GONE
                binding.pendingRequest.layoutManager = LinearLayoutManager(binding.root.context)
                adapter = AcceptedRequestAdapter(
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
            for (req in requests) {
                rejectAllList.add(RequestStatus("Rejected", req.userid, req.userType))
            }
            viewModel.changeRequestStatus(
                ChangeRequestStatus(
                    false, rejectAllList
                )
            )
        })

        return binding.root
    }

    override fun rejectRequest(request: ChangeRequestStatus) {
        viewModel.changeRequestStatus(request)
    }

    private fun searchViewListener(){
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty() && isData) {
                    Log.e("Query", "1")
                    val searchViewIcon: ImageView =
                        binding.search.findViewById(R.id.search_close_btn) as ImageView
                    searchViewIcon.visibility = View.GONE
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