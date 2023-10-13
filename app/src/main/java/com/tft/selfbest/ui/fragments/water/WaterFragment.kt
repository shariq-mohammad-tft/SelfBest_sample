package com.tft.selfbest.ui.fragments.water

import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentWaterBinding
import com.tft.selfbest.models.StartBody
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.HomeActivity
import com.tft.selfbest.ui.fragments.calenderEvents.MyCalendarFragment
import com.tft.selfbest.ui.fragments.getGoHour.GetGoHour
import com.tft.selfbest.ui.fragments.getGoHour.GetGoHourViewModel
import com.tft.selfbest.ui.login.LoginActivity
import java.time.Instant

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WaterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class WaterFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentWaterBinding
    val viewModel by viewModels<GetGoHourViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWaterBinding.inflate(inflater, container, false)
        val activity = activity as HomeActivity
        binding.No.setOnClickListener(this)
        binding.yes.setOnClickListener(this)
        viewModel.startGetGoHourObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                val transaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, GetGoHour())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }
        Handler().postDelayed({
            viewModel.start(StartBody(Instant.now().toString(), 0))
        }, 10000)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WaterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WaterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.No -> {
                //Log.e("No", "work")
                viewModel.start(StartBody(Instant.now().toString(), 0))
//                val transaction = requireActivity().supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.fragmentContainerView, MyCalendarFragment())
//                transaction.disallowAddToBackStack()
//                transaction.commit()
            }

            R.id.yes -> {
                //Toast.makeText(this,"clicked on yes",Toast.LENGTH_SHORT).show()
                viewModel.start(StartBody(Instant.now().toString(), 1))
            }
        }
    }
}