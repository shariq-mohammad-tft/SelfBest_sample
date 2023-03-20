package com.tft.selfbest.ui.fragments.notification

import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.databinding.FragmentNotificationBinding
import com.tft.selfbest.models.NotificationDetail
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.NotificationAdapter
import com.tft.selfbest.ui.fragments.overview.OverviewViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentNotificationBinding
    val viewModel by viewModels<OverviewViewModel>()
    lateinit var list : List<NotificationDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding =
            FragmentNotificationBinding.inflate(inflater, container, false)
        //viewModel.getNotifications()
        viewModel.notifyObserver.observe(viewLifecycleOwner){
            if(it is NetworkResponse.Success){
                list=it.data!!.notificationList
                if(list.isNotEmpty()){
                    binding.noNotification.visibility=View.GONE
                    binding.notificationList.visibility=View.VISIBLE
                    binding.notificationList.layoutManager = LinearLayoutManager(binding.root.context)
                    binding.notificationList.adapter = NotificationAdapter(list, binding.root.context)
                }
            }
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}