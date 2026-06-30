package com.focusguard.app.ui.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = app.getSharedPreferences("focusguard_prefs", Context.MODE_PRIVATE)

    private val _vpnEnabled = MutableLiveData(prefs.getBoolean("vpn_enabled", false))
    val vpnEnabled: LiveData<Boolean> = _vpnEnabled

    fun setVpnEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("vpn_enabled", enabled).apply()
        _vpnEnabled.value = enabled
    }
}
