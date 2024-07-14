package com.serranoie.android.buybuddy.ui.util

import android.content.Context
import javax.inject.Inject

class PreferenceUtil @Inject constructor(context: Context) {
    companion object {
        // Shared preferences file name
        private const val PREFS_NAME = "buybuddy_settings"

        // Main preference keys
        const val APP_THEME_INT = "theme_settings"
        const val MATERIAL_YOU_BOOL = "material_you"
        const val APP_LOCK_BOOL = "app_lock"
    }

    private var prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Check if a key exists in the shared preferences.
     * @param key The key to check.
     * @return true if the key exists, false otherwise.
     */
    private fun keyExists(key: String): Boolean {
        return prefs.contains(key)
    }

    /**
     * Put a string value in the shared preferences.
     * @param key The key to store the value under.
     * @param value The value to store.
     */
    fun putString(key: String, value: String) {
        val prefsEditor = prefs.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    /**
     * Put an integer value in the shared preferences.
     * @param key The key to store the value under.
     * @param value The value to store.
     */
    fun putInt(key: String, value: Int) {
        val prefsEditor = prefs.edit()
        prefsEditor.putInt(key, value)
        prefsEditor.apply()
    }

    /**
     * Put a boolean value in the shared preferences.
     * @param key The key to store the value under.
     * @param value The value to store.
     */
    fun putBoolean(key: String, value: Boolean) {
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    /**
     * Get a string value from the shared preferences.
     * @param key The key to get the value from.
     * @param defValue The default value to return if the key does not exist.
     * @return The value stored under the key, or the default value if the key does not exist.
     */
    fun getString(key: String, defValue: String): String? {
        return prefs.getString(key, defValue)
    }

    /**
     * Get an integer value from the shared preferences.
     * @param key The key to get the value from.
     * @param defValue The default value to return if the key does not exist.
     * @return The value stored under the key, or the default value if the key does not exist.
     */
    fun getInt(key: String, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }

    /**
     * Get a boolean value from the shared preferences.
     * @param key The key to get the value from.
     * @param defValue The default value to return if the key does not exist.
     * @return The value stored under the key, or the default value if the key does not exist.
     */
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }
}