package com.tft.selfbest.data

import android.app.Application
import android.content.Context
import android.util.Log
import com.tft.selfbest.models.DurationFilter
import com.tft.selfbest.models.LogedInData
import com.tft.selfbest.models.ProfileData
import com.tft.selfbest.utils.JsonUtil.Companion.fromJson
import com.tft.selfbest.utils.JsonUtil.Companion.toJson
import com.tft.selfbest.utils.PreferenceUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelfBestPreference @Inject constructor(application: Application) {

    private val _pref = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREF_NAME = "self_best_pref"
    }

    private object Key {
        const val LOGIN_DATA = "_login_data"
        const val OFFLINE_TIME = "_offline_time"
        const val PROFILE_PICTURE = "profile_picture"
        const val GetGoId = "getGoHourId"
        const val PROFILE_DATA = "_profile_data"
        const val FIRST_TIME = "_first_time"
        const val IS_ORG_ADMIN = "_is_org_admin"
        const val FILTERS = "_filters"
    }

    val getLoginData: LogedInData?
        get() = PreferenceUtil.getString(_pref, Key.LOGIN_DATA, null)?.fromJson<LogedInData>()

    fun setLoginData(data: LogedInData?) {
        PreferenceUtil.setString(_pref, Key.LOGIN_DATA, data?.toJson())
    }

    val getOfflineTime: String?
        get() = PreferenceUtil.getString(_pref, Key.OFFLINE_TIME, null)

    fun setOfflineTime(offlineTime: String) {
        PreferenceUtil.setString(_pref, Key.OFFLINE_TIME, offlineTime)
    }

    val getProfilePicture: String?
        get() = PreferenceUtil.getString(_pref, Key.PROFILE_PICTURE, "")

    fun setProfilePicture(profileImageUrl: String) {
        PreferenceUtil.setString(_pref, Key.PROFILE_PICTURE, profileImageUrl)
    }

    val getGetHourId: Int
        get() = PreferenceUtil.getInt(_pref, Key.GetGoId, 1)

    fun setGetGoHourId(getGoHourId: Int) {
        Log.e("msgtype", "Preference Success")
        PreferenceUtil.setInt(_pref, Key.GetGoId, getGoHourId)
    }

    fun clear() {
        val editor = _pref.edit()
        editor.clear()
        editor.commit()
    }

    fun clearOfflineTime() {
        val editor = _pref.edit()
        editor.remove(Key.OFFLINE_TIME)
        editor.commit()
    }

    val getProfileData: ProfileData?
        get() = PreferenceUtil.getString(_pref, Key.PROFILE_DATA, null)?.fromJson<ProfileData>()

    fun setProfileData(data: ProfileData?) {
        PreferenceUtil.setString(_pref, Key.PROFILE_DATA, data?.toJson())
    }

    val isFirstTime: Boolean
        get() = PreferenceUtil.getBoolean(_pref, Key.FIRST_TIME, false)

    fun setFirstTime(data: Boolean) {
        PreferenceUtil.setBoolean(_pref, Key.FIRST_TIME, data)
    }

    val isOrgAdmin: Boolean
        get() = PreferenceUtil.getBoolean(_pref, Key.IS_ORG_ADMIN, false)

    fun setOrgAdmin(data: Boolean) {
        PreferenceUtil.setBoolean(_pref, Key.IS_ORG_ADMIN, data)
    }


    val getFilters: DurationFilter?
        get() = PreferenceUtil.getString(_pref, Key.FILTERS, null)?.fromJson<DurationFilter>()

    fun setFilters(data: DurationFilter?) {
        PreferenceUtil.setString(_pref, Key.FILTERS, data?.toJson())
    }

}