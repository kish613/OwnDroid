package com.bintianqi.owndroid

import android.content.Context
import android.content.pm.PackageManager

object StealthManager {
    private const val STEALTH_PREF_KEY = "app_stealth_mode"
    private const val ADB_AUTH_TOKEN_KEY = "adb_auth_token"
    
    /**
     * Toggle app stealth mode
     */
    fun setStealthMode(context: Context, enabled: Boolean): Boolean {
        return try {
            val packageManager = context.packageManager
            val componentName = android.content.ComponentName(
                context,
                "com.bintianqi.owndroid.MainActivity"
            )
            
            val newState = if (enabled) {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            }
            
            packageManager.setComponentEnabledSetting(
                componentName,
                newState,
                PackageManager.DONT_KILL_APP
            )
            
            // Save state to SharedPreferences
            context.getSharedPreferences("stealth_prefs", Context.MODE_PRIVATE)
                .edit()
                .putBoolean(STEALTH_PREF_KEY, enabled)
                .apply()
                
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Check if app is currently in stealth mode
     */
    fun isStealthModeEnabled(context: Context): Boolean {
        return context.getSharedPreferences("stealth_prefs", Context.MODE_PRIVATE)
            .getBoolean(STEALTH_PREF_KEY, false)
    }
    
    /**
     * Generate secure ADB authentication token
     */
    fun generateAdbToken(context: Context): String {
        val token = (100000..999999).random().toString()
        context.getSharedPreferences("stealth_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString(ADB_AUTH_TOKEN_KEY, token)
            .apply()
        return token
    }
    
    /**
     * Validate ADB authentication token
     */
    fun validateAdbToken(context: Context, token: String): Boolean {
        val storedToken = context.getSharedPreferences("stealth_prefs", Context.MODE_PRIVATE)
            .getString(ADB_AUTH_TOKEN_KEY, "")
        return token == storedToken
    }
    
    /**
     * Get ADB command for unhiding app
     */
    fun getUnhideCommand(context: Context, token: String): String {
        return "adb shell am broadcast -a com.bintianqi.owndroid.UNHIDE_APP --es token $token"
    }

    /**
     * Get ADB command for hiding app
     */
    fun getHideCommand(context: Context, token: String): String {
        return "adb shell am broadcast -a com.bintianqi.owndroid.HIDE_APP --es token $token"
    }

    /**
     * Hide app icon from launcher
     */
    fun hideAppIcon(context: Context) {
        val packageManager = context.packageManager
        val componentName = android.content.ComponentName(
            context,
            "com.bintianqi.owndroid.MainActivity"
        )
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    /**
     * Unhide app icon from launcher
     */
    fun unhideAppIcon(context: Context) {
        val packageManager = context.packageManager
        val componentName = android.content.ComponentName(
            context,
            "com.bintianqi.owndroid.MainActivity"
        )
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    /**
     * Check if app is currently hidden from launcher
     */
    fun isAppHidden(context: Context): Boolean {
        val packageManager = context.packageManager
        val componentName = android.content.ComponentName(
            context,
            "com.bintianqi.owndroid.MainActivity"
        )
        return packageManager.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    }
}
