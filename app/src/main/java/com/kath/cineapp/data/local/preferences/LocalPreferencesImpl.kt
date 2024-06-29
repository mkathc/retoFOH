package com.kath.cineapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences

class LocalPreferencesImpl(context: Context, name: String) : ILocalPreferences {

    companion object {
        const val USER_SHARED_PREFERENCES = "USER_SHARED_PREFERENCES"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    override fun getString(key: String): String {
        return sharedPref.getString(key, "") ?: ""
    }

    override fun setString(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    override fun deleteString(key: String) {
        sharedPref.edit().remove(key).apply()
    }

    override fun hasString(key: String): Boolean {
        return sharedPref.contains(key)
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    override fun setBoolean(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }
}