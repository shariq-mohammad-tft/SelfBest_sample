    package com.example.chat_feature.utils

    import android.app.Application
    import android.content.Context
    import android.content.SharedPreferences
    import dagger.hilt.android.qualifiers.ApplicationContext
    import javax.inject.Inject
    import javax.inject.Singleton

    @Singleton
    class SharedPrefManager @Inject constructor(@ApplicationContext val context: Context, application: Application) {

        private val PREFS_NAME = "login"
        private val editor: SharedPreferences.Editor


        init {
            val prefs = application.getSharedPreferences(PREFS_NAME, 0)
            editor = prefs.edit()
        }


        fun setInt(key: String?, value: Int) {
            editor.putInt(key, value)
            editor.apply()
        }

        fun getInt(key: String?): Int {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.getInt(key, 0)
        }

        fun setString(key: String?, value: String?) {
            editor.putString(key, value)
            editor.apply()
        }

        fun getString(key: String): String? {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.getString(key, "")
        }

        fun setBoolean(key: String?, value: Boolean) {
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun getBoolean(key: String?): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.getBoolean(key, false)
        }

        fun clear() {
            editor.clear()
            editor.apply()
        }


    }