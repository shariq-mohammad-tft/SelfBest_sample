package com.tft.selfbest.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.ui.activites.DetailActivity
import com.tft.selfbest.utils.InstalledAppInfoUtil

class InstalledAppsOverviewAdapter(
    itemList: ArrayList<InstalledAppInfoUtil.Companion.InfoObject>,
    mContext: Context, isAddMore: Boolean,
//    val selectedAppInterface: SelectedAppInterface,
    val isLaunchOnClick: Boolean,
) :
    RecyclerView.Adapter<InstalledAppsOverviewAdapter.CourseItemViewHolder>() {
    private val list = itemList
    private val context = mContext
    private val isEnableAddMore = isAddMore
    private var selectedIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.installed_suggested_app, parent, false)
        return CourseItemViewHolder(layoutInflater)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: CourseItemViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (isEnableAddMore && position == if (list.size >= 5) list.subList(0,
                5).size else list.size
        ) {
            holder.appLogo.setImageDrawable(context.getDrawable(R.drawable.add_icon))
            holder.appTitle.text = "Add more"
            holder.containerView.setOnClickListener(View.OnClickListener {
                val intentOpenDetailPage = Intent(context, DetailActivity::class.java)
                intentOpenDetailPage.putExtra("header_title", "List of Apps")
                ContextCompat.startActivity(context, intentOpenDetailPage, null)
            })

        } else {
            val appInfo = list[position]
            holder.containerView.setBackgroundResource(if (selectedIndex == position) R.drawable.selected_installed_app else R.drawable.top_suggested_installed_app)
            holder.appTitle.text = appInfo.appName
            var drawable1: Drawable? = appInfo.icon
            holder.appLogo.setImageDrawable(drawable1)
//            holder.containerView.setOnClickListener(View.OnClickListener {
//                selectedAppInterface.selectedAppListener(appInfo.pname)
//                selectedIndex = position
//                notifyDataSetChanged()
                /*if (isLaunchOnClick) {
                    val launchIntent: Intent? =
                        context.packageManager.getLaunchIntentForPackage(appInfo.pname)
                    if (launchIntent != null)
                        ContextCompat.startActivity(context, launchIntent, null)
                } else {
                    selectedAppInterface.selectedAppListener(AppDetail(appInfo.appName, "Learning"))
                }
*/
//            })
        }

    }

    override fun getItemCount(): Int {
        return if (isEnableAddMore) if (list.size >= 5) list.subList(0,
            5).size + 1 else list.size + 1 else list.count()
    }

    inner class CourseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var appTitle: TextView = view.findViewById(R.id.app_name) as TextView
        var appLogo: ImageView = view.findViewById(R.id.app_icon) as ImageView
        var containerView: View = view.findViewById(R.id.main_container)
    }


    interface SelectedAppInterface {
        fun selectedAppListener(selectedAppPName: String)
    }
}