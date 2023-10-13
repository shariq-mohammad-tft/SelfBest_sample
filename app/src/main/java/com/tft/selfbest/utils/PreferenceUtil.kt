package com.tft.selfbest.utils

import android.content.SharedPreferences
import android.text.TextUtils

object PreferenceUtil {
    private val TAG = PreferenceUtil::class.java.simpleName

    fun setString(_pref: SharedPreferences?, key: String, value: String?) {
        if (!TextUtils.isEmpty(key) && value != null) {
            try {
                if (_pref != null) {
                    val editor = _pref.edit()
                    editor.putString(key, value)
                    editor.apply()
                }
            } catch (e: Exception) {
                //Logger.e(TAG + ": "+ e.message)
            }

        }
    }

    fun setLong(_pref: SharedPreferences?, key: String, value: Long) {
        if (!TextUtils.isEmpty(key)) {
            try {
                if (_pref != null) {
                    val editor = _pref.edit()
                    editor.putLong(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                //Logger.e(TAG + ": "+ e.message)
            }

        }
    }


    fun setInt(_pref: SharedPreferences?, key: String, value: Int) {
        if (!TextUtils.isEmpty(key)) {
            try {
                if (_pref != null) {
                    val editor = _pref.edit()
                    editor.putInt(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                //Logger.e(TAG + ": "+ e.message)
            }

        }
    }


    fun setDouble(_pref: SharedPreferences?, key: String, value: Double) {
        if (!TextUtils.isEmpty(key)) {
            try {
                if (_pref != null) {
                    val editor = _pref.edit()
                    editor.putFloat(key, value.toFloat())
                    editor.commit()
                }
            } catch (e: Exception) {
                //Logger.e(TAG + ": "+ e.message)
            }

        }
    }


    fun setBoolean(_pref: SharedPreferences?, key: String, value: Boolean) {
        if (!TextUtils.isEmpty(key)) {
            try {
                if (_pref != null) {
                    val editor = _pref.edit()
                    editor.putBoolean(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                //Logger.e(TAG + ": "+ e.message)
            }

        }
    }

    fun getInt(_pref: SharedPreferences?, key: String, defaultValue: Int): Int {
        return if (_pref != null && !TextUtils.isEmpty(key) && _pref.contains(key)) {
            _pref.getInt(key, defaultValue)
        } else defaultValue
    }

    fun getLong(_pref: SharedPreferences?, key: String, defaultValue: Long): Long {
        return if (_pref != null && !TextUtils.isEmpty(key) && _pref.contains(key)) {
            _pref.getLong(key, defaultValue)
        } else defaultValue
    }

    fun getBoolean(_pref: SharedPreferences?, key: String, defaultValue: Boolean): Boolean {
        return if (_pref != null && !TextUtils.isEmpty(key) && _pref.contains(key)) {
            _pref.getBoolean(key, defaultValue)
        } else defaultValue
    }


    fun isNull(_pref: SharedPreferences?, key: String): Boolean {
        return !(_pref != null && !TextUtils.isEmpty(key) && _pref.contains(key))

    }

    fun getString(_pref: SharedPreferences?, key: String, defaultValue: String?): String? {
        return if (_pref != null && !TextUtils.isEmpty(key) && _pref.contains(key)) {
            _pref.getString(key, defaultValue)
        } else defaultValue
    }

    fun getDouble(_pref: SharedPreferences?, key: String, defaultValue: Double): Double {
        return if (_pref != null && !TextUtils.isEmpty(key) && _pref.contains(key)) {
            _pref.getFloat(key, defaultValue.toFloat()).toDouble()
        } else defaultValue
    }

    fun removeString(_pref: SharedPreferences?, key: String) {
        if (!TextUtils.isEmpty(key)) {
            try {
                if (_pref != null && _pref.contains(key)) {
                    val editor = _pref.edit()
                    editor.remove(key)
                    editor.apply()
                }
            } catch (e: Exception) {
                //Logger.e(TAG + ": "+ e.message)
            }

        }
    }
}