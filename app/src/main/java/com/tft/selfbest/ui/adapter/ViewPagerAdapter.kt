package com.tft.selfbest.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tft.selfbest.ui.fragments.userManagement.request.UserManagementRequestAccept
import com.tft.selfbest.ui.fragments.userManagement.request.UserManagementRequestPending
import com.tft.selfbest.ui.fragments.userManagement.request.UserManagementRequestReject

class ViewPagerAdapter(fragmentManager:FragmentManager,lifecycle: Lifecycle)
    :FragmentStateAdapter(fragmentManager,lifecycle)
{
    val page_count=3

//        override fun getCount(): Int {
//            return page_count
//        }

//        override fun getItem(position: Int): Fragment {
//
//        }
//
//        override fun getPageTitle(position: Int): CharSequence? {
//             when(position){
//                0->{
//                    "Pending"
//                }
//                1->{
//                    "Accepted"
//                }
//                2->{
//                    "Rejected"
//                }
//
//            }
//            return super.getPageTitle(position)
//        }

    override fun getItemCount(): Int {
        return page_count
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                UserManagementRequestPending()
            }
            1->{
                UserManagementRequestAccept()
            }
            2->{
                UserManagementRequestReject()
            }
            else->{
                UserManagementRequestPending()
            }
        }
    }
}