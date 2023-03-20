package com.tft.selfbest.ui.fragments.activityLog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentAnsweredQueryBinding
import com.tft.selfbest.models.QueryAnsweredResponse
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.AnsweredQueryAdapter
import com.tft.selfbest.ui.fragments.statistics.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AnsweredQuery : Fragment(), AnsweredQueryAdapter.ChangeStatusListener {

    lateinit var binding: FragmentAnsweredQueryBinding
    val viewModel by viewModels<StatisticsViewModel>()
    lateinit var answeredQueries: List<QueryAnsweredResponse>
    lateinit var startDate: String
    val startDateCal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAnsweredQueryBinding.inflate(layoutInflater)
        startDate = dateFormat.format(startDateCal.time)
        viewModel.getAnsweredQuery("", startDate, "daily")
        viewModel.queryAnsweredObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                answeredQueries = it.data!!
                //if(answeredQueries.size > 10){
                    //binding.loadMoreBtn.visibility = View.VISIBLE
                    binding.answeredQueries.layoutManager = LinearLayoutManager(binding.root.context)
                    binding.answeredQueries.adapter = AnsweredQueryAdapter(
                        binding.root.context,
                        answeredQueries.take(10),
                        this
                    )
            }
        }
        return binding.root
    }

    override fun changeStatus(id: Int, status: Int) {
        viewModel.updateStatus(id, status)

    }
}