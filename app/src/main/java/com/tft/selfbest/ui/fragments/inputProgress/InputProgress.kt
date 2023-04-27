package com.tft.selfbest.ui.fragments.inputProgress

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.databinding.FragmentInputProgressBinding
import com.tft.selfbest.models.*
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.adapter.InputProgressAdapter
import com.tft.selfbest.ui.fragments.getGoHour.GetGoHourViewModel
import com.tft.selfbest.ui.fragments.overview.OverviewFragment
import com.tft.selfbest.ui.fragments.overview.OverviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InputProgress.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class InputProgress : Fragment(),View.OnClickListener, InputProgressAdapter.ChangeCategory {

    @Inject
    lateinit var preferences: SelfBestPreference

    lateinit var binding: FragmentInputProgressBinding
    lateinit var observationsData: ObservationsResponse
    private var observationDetail= mutableListOf<Observations>()
    private val overviewViewModel by viewModels<OverviewViewModel>()
    var categories = mutableListOf<String>()

    private val gghViewModel by viewModels<GetGoHourViewModel>()
    lateinit var activities: List<ActivitySingleResponse>
    lateinit var getGoHourResponse: GetGoHourResponse

    val viewModel by viewModels<InputProgressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            requireActivity().window.setDecorFitsSystemWindows(false)
//            requireActivity().window.insetsController?.apply {
//                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
//                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentInputProgressBinding.inflate(layoutInflater,container,false)
        binding.recycleInputProgress.layoutManager= LinearLayoutManager(binding.root.context)
        //binding.ratingBar.stepSize = 1F
        gghViewModel.getActivity()
        gghViewModel.activityLogObserver.observe(viewLifecycleOwner){
            if (it is NetworkResponse.Success) {
                Log.e("ActivityLog: ", " Success")
                if(it.data!!.activities == null){
                    viewModel.getInput(InputData( preferences.getGetHourId, listOf(),1))
                }
                else if (it.data.activities.isNotEmpty()) {
                    activities = it.data.activities
                    if(it.data.categoryList != null){
                        for (cat in it.data.categoryList as Map<String, String>)
                            categories.add(cat.value)
                    }
                    for(activity in activities){
                        observationDetail.add(Observations(activity.category, activity.duration, activity.type, activity.url))
                    }
                    binding.recycleInputProgress.adapter = InputProgressAdapter(activities, categories,requireContext(), this)
                }
            }
        }
        viewModel.ratinObserver.observe(viewLifecycleOwner){
            val transaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, OverviewFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
            //Toast.makeText(binding.root.context, it, Toast.LENGTH_LONG).show()
        }

//        overviewViewModel.getGoHour()
//        Log.d("sdsd","callem")
//        overviewViewModel.getGoHourObserver.observe(viewLifecycleOwner){
//            Log.d("sdsd","callem")
//            if(it is NetworkResponse.Success){
//                getGoHourResponse = it.data!!
//                if (getGoHourResponse.ended){
//                    binding.ratings.text=getGoHourResponse.id.toString()
//                    val getgohourid=binding.ratings.text
//                    Log.e("gTHour",binding.ratings.text.toString())
//                }
//            }
//        }

        binding.save.setOnClickListener(this)


        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InputProgress().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.save->{
                if(binding.ratingBar.rating <= 0){
                    Log.d("endcall", binding.ratingBar.rating.toString())
                    Toast.makeText(requireContext(), "Please rate your get go hour", Toast.LENGTH_SHORT).show()
                }else {
                    //val getgohourid = preferences.getGetHourId
                    //Log.d("endcall", getgohourid.toString())
                    val rating = binding.ratingBar.rating
                    Log.d("endcall", binding.ratingBar.rating.toString())
                    val inputData = InputData(preferences.getGetHourId, observationDetail, rating.toInt())
                    viewModel.getInput(inputData)
                }
            }

        }
    }

    override fun changeCategory(activity: ActivitySingleResponse, selectedCategory: String) {
        for(entity in observationDetail){
            if(entity.url.equals(activity.url))
                entity.category = selectedCategory
        }
    }


}