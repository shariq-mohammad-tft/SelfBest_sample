package com.tft.selfbest.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.tft.selfbest.R
import com.tft.selfbest.models.suggestedApps.AppDetail
import com.tft.selfbest.utils.InstalledAppInfoUtil
import java.util.*


class InstalledAppsAdapter2(
    itemList: ArrayList<InstalledAppInfoUtil.Companion.InfoObject>,
    mContext: Context, isAddMore: Boolean,
    val selectedAppInterface: SelectedAppInterface,
    val isLaunchOnClick: Boolean,
) :
    RecyclerView.Adapter<InstalledAppsAdapter2.CourseItemViewHolder>(), Filterable {
    private val list = itemList
    private val context = mContext
    private val isEnableAddMore = isAddMore
    private var selectedIndex = -1
    var filterList = ArrayList<InstalledAppInfoUtil.Companion.InfoObject>()

    init {
        filterList = itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.installed_app, parent, false)
        return CourseItemViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: CourseItemViewHolder, position: Int) {
        val appInfo = filterList[position]
        holder.appTitle.text = appInfo.appName
        var drawable1: Drawable? = appInfo.icon
        holder.appLogo.setImageDrawable(drawable1)
        holder.containerView.setBackgroundResource(if (selectedIndex == position) R.drawable.selected_installed_app else R.drawable.top_suggested_installed_app)
        holder.containerView.setOnClickListener(View.OnClickListener {
            if (isLaunchOnClick) {
                val launchIntent: Intent? =
                    context.packageManager.getLaunchIntentForPackage(appInfo.pname)
                if (launchIntent != null)
                    startActivity(context, launchIntent, null)
            } else {
                selectedIndex = position
                notifyDataSetChanged()
                selectedAppInterface.selectedAppListener(AppDetail(appInfo.appName, "Learning"))
            }

        })

    }

    override fun getItemCount(): Int {
        return if (isEnableAddMore) if (list.size >= 5) list.subList(0,
            5).size + 1 else list.size + 1 else filterList.count()
    }

    inner class CourseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var appTitle: TextView = view.findViewById(R.id.app_name) as TextView
        var appLogo: ImageView = view.findViewById(R.id.app_icon) as ImageView
        var containerView: View = view.findViewById(R.id.main_container)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filterList = if (charSearch.isEmpty()) {
                    list
                } else {
                    val resultList = ArrayList<InstalledAppInfoUtil.Companion.InfoObject>()
                    for (row in list) {
                        if (row.appName.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<InstalledAppInfoUtil.Companion.InfoObject>
                notifyDataSetChanged()
            }

        }
    }

    interface SelectedAppInterface {
        fun selectedAppListener(appDetail: AppDetail)
    }
}