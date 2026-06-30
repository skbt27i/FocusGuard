package com.focusguard.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.focusguard.app.services.FocusVpnService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val prefs = context.getSharedPreferences("focusguard_prefs", Context.MODE_PRIVATE)
            if (prefs.getBoolean("vpn_enabled", false)) {
                val vpnIntent = Intent(context, FocusVpnService::class.java).apply {
                    action = FocusVpnService.ACTION_START
                }
                context.startForegroundService(vpnIntent)
            }
        }
    }
}
