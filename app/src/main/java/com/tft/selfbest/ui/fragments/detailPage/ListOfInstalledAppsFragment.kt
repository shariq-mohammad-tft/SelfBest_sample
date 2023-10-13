package com.tft.selfbest.ui.fragments.detailPage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.tft.selfbest.R
import com.tft.selfbest.databinding.FragmentListOfInstalledAppsBinding
import com.tft.selfbest.models.suggestedApps.AppDetail
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.DetailActivity
import com.tft.selfbest.ui.adapter.InstalledAppsAdapter2
import com.tft.selfbest.utils.InstalledAppInfoUtil
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListOfInstalledAppsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ListOfInstalledAppsFragment : Fragment(), View.OnClickListener,
    InstalledAppsAdapter2.SelectedAppInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentListOfInstalledAppsBinding
    private var isViewAll: Boolean = false
    val viewModel by viewModels<SuggestedAppsViewModel>()
    lateinit var selectedAppDetail: AppDetail
    private var suggestedInstalledApps: ArrayList<InstalledAppInfoUtil.Companion.InfoObject> =
        ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel.getSuggestedApps()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListOfInstalledAppsBinding.inflate(inflater, container, false)
        binding.installedAppList.layoutManager = GridLayoutManager(context, 4)
        val context = activity as DetailActivity
        binding.installedAppList.adapter =
            InstalledAppsAdapter2(suggestedInstalledApps, binding.root.context, false, this, false)
        viewModel.suggestedAppsObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                suggestedInstalledApps =
                    InstalledAppInfoUtil.getSuggestedInstalledApps(it.data as ArrayList<AppDetail>,
                        context.listOfInstalledApps)
                binding.installedAppList.adapter =
                    InstalledAppsAdapter2(suggestedInstalledApps,
                        binding.root.context,
                        false,
                        this,
                        true)
            }
        }
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                (binding.installedAppList.adapter as InstalledAppsAdapter2).filter.filter(query)
                return false
            }
        })
        binding.addToSuggestedList.isEnabled = false
        binding.suggested.setOnClickListener(this)
        binding.viewAll.setOnClickListener(this)
        binding.addToSuggestedList.setOnClickListener(this)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListOfInstalledAppsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListOfInstalledAppsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.suggested -> {
                if (!isViewAll)
                    return
                isViewAll = false
                binding.suggested.background =
                    resources.getDrawable(R.drawable.left_organisation_round)
                binding.suggested.setTextColor(resources.getColor(R.color.white))
                binding.viewAll.setBackgroundColor(Color.TRANSPARENT)
                binding.viewAll.setTextColor(resources.getColor(R.color.profile_border_color))
                binding.bottomContainer.visibility = View.GONE
                binding.installedAppList.adapter =
                    InstalledAppsAdapter2(suggestedInstalledApps,
                        binding.root.context,
                        false,
                        this,
                        true)
            }
            R.id.viewAll -> {
                if (isViewAll)
                    return
                isViewAll = true
                binding.viewAll.background =
                    resources.getDrawable(R.drawable.right_organisation_round)
                binding.viewAll.setTextColor(resources.getColor(R.color.white))
                binding.suggested.setBackgroundColor(Color.TRANSPARENT)
                binding.suggested.setTextColor(resources.getColor(R.color.profile_border_color))
                binding.bottomContainer.visibility = View.VISIBLE
                binding.installedAppList.adapter =
                    InstalledAppsAdapter2((activity as DetailActivity).listOfInstalledApps,
                        binding.root.context,
                        false, this, false)
            }
            R.id.add_to_suggested_list -> {
                viewModel.addSelectedAppInSuggestedList(selectedAppDetail)
                Toast.makeText(binding.root.context,
                    "${selectedAppDetail.appName} is added to suggested list", Toast.LENGTH_LONG)
                    .show()
                viewModel.getSuggestedApps()
                isViewAll = false
                binding.suggested.background =
                    resources.getDrawable(R.drawable.left_organisation_round)
                binding.suggested.setTextColor(resources.getColor(R.color.white))
                binding.viewAll.setBackgroundColor(Color.TRANSPARENT)
                binding.viewAll.setTextColor(resources.getColor(R.color.black))
                binding.bottomContainer.visibility = View.GONE
            }
        }
    }

    override fun selectedAppListener(appDetail: AppDetail) {
        binding.addToSuggestedList.isEnabled = true
        selectedAppDetail = appDetail
    }
}