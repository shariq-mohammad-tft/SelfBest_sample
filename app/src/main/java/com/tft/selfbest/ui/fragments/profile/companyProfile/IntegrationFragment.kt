package com.tft.selfbest.ui.fragments.profile.companyProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.tft.selfbest.databinding.FragmentIntegrationBinding
import com.tft.selfbest.models.CompanyDetails
import com.tft.selfbest.models.IntegratedTools
import com.tft.selfbest.network.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntegrationFragment : Fragment() {

    lateinit var binding: FragmentIntegrationBinding
    val viewModel by viewModels<CompanyProfileViewModel>()
    lateinit var companyDetails: CompanyDetails
    lateinit var simplifyTools: IntegratedTools

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntegrationBinding.inflate(inflater, container, false)
        viewModel.getCompanyDetails()
        viewModel.companyProfileObserver.observe(viewLifecycleOwner) {
            if (it is NetworkResponse.Success) {
                companyDetails = it.data!!.detail
                simplifyTools = it.data.simplifyPathTools

                binding.isWorkDen.isChecked = companyDetails.workDen
                binding.isWorkDen.isEnabled = false
                binding.isSolutionPoint.isChecked = companyDetails.solutionPoint
                binding.isSolutionPoint.isEnabled = false
                binding.isSimplifyPath.isChecked = companyDetails.simplifyPath
                binding.isSimplifyPath.isEnabled = false

                if (companyDetails.simplifyPath) {
                    binding.integratedTools.visibility = View.VISIBLE
                    binding.simplifyPathContainer.elevation = 2f
//                    binding.isKekaConnected.text =
//                        if (simplifyTools.keka!!) "Connected" else ""
                    binding.keka.visibility = if(simplifyTools.keka!!) View.VISIBLE else View.GONE
                    if(simplifyTools.keka!!){
                        binding.kekaText.text = "Keka("+simplifyTools.kekaDomainName+")"
                    }
                    binding.googleCalendar.visibility = if(simplifyTools.googleCalendar!!) View.VISIBLE else View.GONE
                    binding.microsoftCalendar.visibility = if(simplifyTools.microsoftCalendar!!) View.VISIBLE else View.GONE
                    binding.jira.visibility = if(simplifyTools.jira!!) View.VISIBLE else View.GONE
                    binding.freshwork.visibility = if(simplifyTools.freshwork!!) View.VISIBLE else View.GONE
                    binding.salesforce.visibility = if(simplifyTools.salesforce!!) View.VISIBLE else View.GONE
                    binding.gmail.visibility = if(simplifyTools.gmail!!) View.VISIBLE else View.GONE
                    binding.dbf.visibility = if(simplifyTools.dbf!!) View.VISIBLE else View.GONE
                }
            } else {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}