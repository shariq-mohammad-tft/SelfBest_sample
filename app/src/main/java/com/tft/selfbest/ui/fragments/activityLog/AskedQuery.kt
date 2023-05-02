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
import com.tft.selfbest.databinding.FragmentAskedQueryBinding
import com.tft.selfbest.models.QueryResponse
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.QueryResponseAdapter
import com.tft.selfbest.ui.fragments.statistics.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AskedQuery(
    val startDate1: String,
    val endDate: String,
    val type: String
) : Fragment(), QueryResponseAdapter.ChangeStatusListener {

    lateinit var binding: FragmentAskedQueryBinding
    val viewModel by viewModels<StatisticsViewModel>()
    lateinit var queries: List<QueryResponse>
    var queries1: MutableList<QueryResponse> = mutableListOf()
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
        binding = FragmentAskedQueryBinding.inflate(layoutInflater)
        startDate = dateFormat.format(startDateCal.time)
//        viewModel.getQuery(startDate, "daily")
        viewModel.getQuery(startDate1, endDate, type)
        viewModel.queryObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                queries = it.data!!.query_data
                for (query in queries) {
                    if (query.question != "")
                        queries1.add(query)
                }
//                if (queries1.isNotEmpty()) {
//                    if(queries1.size > 10) {
//                        binding.loadMoreBtn.visibility = View.VISIBLE
//                        binding.query.layoutManager = LinearLayoutManager(binding.root.context)
//                        binding.query.adapter = QueryResponseAdapter(
//                            binding.root.context,
//                            queries1.take(10),
//                            this
//                        )
//                    }
//                    else {
//                        binding.loadMoreBtn.visibility = View.GONE
//                        binding.query.layoutManager = LinearLayoutManager(binding.root.context)
                binding.askedQueries.layoutManager = LinearLayoutManager(binding.root.context)
                binding.askedQueries.adapter = QueryResponseAdapter(
                    binding.root.context,
                    queries1,
                    this
                )
                //}
                //Log.e("Timestamp ", "${queries.size} $queries")
                //    }
            }
        }

        return binding.root
    }

    override fun changeStatus(id: Int, status: Int) {
        viewModel.updateStatus(id, status)
    }

    override fun updateRating(id: Int, rating: Int) {
        viewModel.updateRating(id, rating)
    }
}