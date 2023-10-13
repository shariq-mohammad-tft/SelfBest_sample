package com.tft.selfbest.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.tft.selfbest.models.suggestedApps.AppDetail

class InstalledAppInfoUtil {
    companion object {
        class InfoObject {
            var appName = ""
            var pname = ""
            var icon: Drawable? = null
        }

        /*  private fun getPackages(): ArrayList<InfoObject>? {
              val apps = getInstalledApps(false)
              val max: Int = apps.size()
              for (i in 0 until max) {
                  apps[i].prettyPrint()
              }
              return apps
          }*/

        fun getInstalledApps(mContext: Context): ArrayList<InfoObject> {
            val res = ArrayList<InfoObject>()
            val packs: List<PackageInfo> =
                mContext.packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
            val defaultActivityIcon = mContext.packageManager.defaultActivityIcon
            for (i in packs.indices) {
                val p = packs[i]
                if (mContext.packageName.equals(p.packageName))
                    continue // skip own app
                // add only apps with application icon
                val intentOfStartActivity: Intent =
                    mContext.packageManager.getLaunchIntentForPackage(p.packageName)
                        ?: continue
                val applicationIcon =
                    mContext.packageManager.getActivityIcon(intentOfStartActivity)
                if (applicationIcon != null && !defaultActivityIcon.equals(applicationIcon)) {
                    val newInfo = InfoObject()
                    newInfo.appName =
                        p.applicationInfo.loadLabel(mContext.packageManager).toString()
                    newInfo.pname = p.packageName
                    newInfo.icon = p.applicationInfo.loadIcon(mContext.packageManager)
                    res.add(newInfo)
                }
            }
            return res
        }

        private fun isSystemPackage(ri: PackageInfo): Boolean {
            return ri.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }

        @Throws(PackageManager.NameNotFoundException::class)
        fun getInstallApps(mContext: Context): ArrayList<InfoObject> {
            val res1 = ArrayList<InfoObject>()
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val ril: List<ResolveInfo> =
                mContext.packageManager.queryIntentActivities(mainIntent, 0) //all activities gor given intent
            val componentList: List<String> = ArrayList()
            var i = 0
            val defaultActivityIcon = mContext.packageManager.defaultActivityIcon
            for (ri in ril) {
                if (ri.activityInfo != null) {
                    if (mContext.packageName.equals(ri.activityInfo.packageName))
                        continue // skip own app

                    val intentOfStartActivity: Intent =
                        mContext.packageManager.getLaunchIntentForPackage(ri.activityInfo.packageName)
                            ?: continue
                    val applicationIcon =
                        mContext.packageManager.getActivityIcon(intentOfStartActivity)
                    if (applicationIcon != null && !defaultActivityIcon.equals(applicationIcon)) {
                        val newInfo = InfoObject()
                        // get package
                        val res: Resources =
                            mContext.packageManager.getResourcesForApplication(ri.activityInfo.applicationInfo)
                        val appName = if (ri.activityInfo.labelRes != 0) {
                            res.getString(ri.activityInfo.labelRes)
                        } else {
                            ri.activityInfo.applicationInfo.loadLabel(
                                mContext.packageManager).toString()
                        }
                        val icon = if (ri.activityInfo.iconResource != 0) {
                            res.getDrawable(ri.activityInfo.iconResource)
                        } else {
                            ri.activityInfo.applicationInfo.loadIcon(
                                mContext.packageManager)
                        }
                        newInfo.appName = appName
                        newInfo.pname = ri.activityInfo.packageName
                        newInfo.icon = icon
                        res1.add(newInfo)
                    }
                }
            }
            return res1
        }

        fun getSuggestedInstalledApps(
            suggestedList: ArrayList<AppDetail>,
            installedAppList: ArrayList<InfoObject>,
        ): ArrayList<InfoObject> {
            val filterSuggestedList = ArrayList<InfoObject>()
            suggestedList.forEach { appDetail ->
                installedAppList.forEach { infoObject ->
                    if ((appDetail.appName) == infoObject.appName)
                        filterSuggestedList.add(infoObject)
                    return@forEach
                }
            }
            return filterSuggestedList
        }
    }
}