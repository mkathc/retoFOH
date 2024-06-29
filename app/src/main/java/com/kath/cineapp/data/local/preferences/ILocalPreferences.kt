package com.kath.cineapp.data.local.preferences

interface ILocalPreferences {
    fun getString(key: String): String
    fun setString(key: String, value: String)
    fun deleteString(key: String)
    fun hasString(key: String): Boolean
    fun getBoolean(key: String): Boolean
    fun setBoolean(key: String, value: Boolean)
}