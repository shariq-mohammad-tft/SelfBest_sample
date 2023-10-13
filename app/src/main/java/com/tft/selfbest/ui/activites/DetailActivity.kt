package com.tft.selfbest.ui.activites

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tft.selfbest.R
import com.tft.selfbest.databinding.ActivityDetailBinding
import com.tft.selfbest.ui.fragments.calenderEvents.UpcomingEventsFragment
import com.tft.selfbest.ui.fragments.detailPage.ListOfInstalledAppsFragment
import com.tft.selfbest.ui.fragments.notification.NotificationFragment
import com.tft.selfbest.ui.fragments.overview.OverviewFragment
import com.tft.selfbest.ui.fragments.overview.ShareWithManagerFragment
import com.tft.selfbest.ui.fragments.profile.ProfileFragment
import com.tft.selfbest.ui.fragments.profile.companyProfile.CompanyProfileFragment
import com.tft.selfbest.utils.InstalledAppInfoUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    lateinit var listOfInstalledApps: ArrayList<InstalledAppInfoUtil.Companion.InfoObject>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progress.visibility = View.GONE
        //listOfInstalledApps = InstalledAppInfoUtil.getInstallApps(this)
        binding.headerTitle.text = intent.getStringExtra("header_title")
        when (intent.getStringExtra("detailType")) {
            "calendar" -> {
                loadFragment(UpcomingEventsFragment())
            }
            "notifications" -> {
                loadFragment(NotificationFragment())
            }
            "profile" -> {
                loadFragment(ProfileFragment())
            }
            "share" -> {
                loadFragment(ShareWithManagerFragment())
            }
            else -> {
                loadFragment(OverviewFragment())
            }
        }
        binding.backArrow.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.detail_fragment, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}