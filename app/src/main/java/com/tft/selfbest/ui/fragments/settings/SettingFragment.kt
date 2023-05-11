package com.tft.selfbest.ui.fragments.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.chat_feature.utils.SharedPrefManager
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.databinding.FragmentSettingBinding
import com.tft.selfbest.models.ProfileData
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.DetailActivity
import com.tft.selfbest.ui.fragments.profile.ProfileViewModel
import com.tft.selfbest.ui.fragments.userManagement.UserManagement
import com.tft.selfbest.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentSettingBinding

    @Inject
    lateinit var preferences: SelfBestPreference
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    val viewModel by viewModels<ProfileViewModel>()
    private lateinit var profileData: ProfileData
    private var isAdmin:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater)
        setOnClickListener()
        isAdmin = preferences.isOrgAdmin
//        if(isAdmin)
            binding.usermanagement.visibility = View.VISIBLE
//        else
//            binding.usermanagement.visibility = View.GONE

        /* viewModel.profileObserver.observe(viewLifecycleOwner) {
             if (it is NetworkResponse.Success) {
                 profileData = it.data?.profileData!!
                 isAdmin = profileData.isOrgAdmin!!
                 if (isAdmin)
                     binding.usermanagement.visibility = View.VISIBLE
                 else
                     binding.usermanagement.visibility = View.GONE
                 Log.e("checkAdmin", profileData.isOrgAdmin.toString())
             }
         }*/
        return binding.root
    }

    private fun setOnClickListener() {
        binding.editProfile.setOnClickListener(this)
        binding.distraction.setOnClickListener(this)
//        binding.calendar.setOnClickListener(this)
        binding.logout.setOnClickListener(this)
        binding.usermanagement.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.edit_profile -> {
                val intentOpenDetailPage = Intent(activity, DetailActivity::class.java)
                intentOpenDetailPage.putExtra("header_title", "Profile")
                intentOpenDetailPage.putExtra("detailType", "profile")
                startActivity(intentOpenDetailPage)
            }

            /* R.id.calendar -> {
                 Toast.makeText(activity, "Calendar Event", Toast.LENGTH_SHORT).show()
                 loadFragment(MyCalendarFragment())
             }*/

            R.id.distraction -> {
                //Toast.makeText(activity, "Distraction", Toast.LENGTH_SHORT).show()
                loadFragment(DistractionFragment())
            }

            R.id.usermanagement -> {
                //Toast.makeText(activity, "Distraction", Toast.LENGTH_SHORT).show()
//                if(isAdmin)
                    loadFragment(UserManagement())
                // else Toast.makeText(activity, "you are not an admin", Toast.LENGTH_SHORT).show()

            }

            R.id.logout -> {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                preferences.clear()
                                sharedPrefManager.clear()
                                CookieManager.getInstance().removeAllCookies(null)
                                CookieManager.getInstance().flush()
                                val loginScreen = Intent(activity, LoginActivity::class.java)
                                loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(loginScreen)
                                dialog.dismiss()

                                activity?.finish()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                                dialog.dismiss()
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Logout", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show()

            }
        }
    }


    private fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}