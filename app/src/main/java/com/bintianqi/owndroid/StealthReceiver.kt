package com.bintianqi.owndroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class StealthReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "com.bintianqi.owndroid.HIDE_APP" -> {
                val token = intent.getStringExtra("token") ?: ""
                if (StealthManager.validateAdbToken(context, token)) {
                    val success = StealthManager.setStealthMode(context, true)
                    if (success) {
                        Toast.makeText(context, "App hidden successfully", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Failed to hide app", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Invalid ADB token", Toast.LENGTH_LONG).show()
                }
            }
            
            "com.bintianqi.owndroid.UNHIDE_APP" -> {
                val token = intent.getStringExtra("token") ?: ""
                if (StealthManager.validateAdbToken(context, token)) {
                    val success = StealthManager.setStealthMode(context, false)
                    if (success) {
                        Toast.makeText(context, "App unhidden successfully", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Failed to unhide app", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Invalid ADB token", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
