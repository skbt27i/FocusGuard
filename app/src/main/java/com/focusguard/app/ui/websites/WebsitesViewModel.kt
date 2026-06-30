package com.focusguard.app.ui.websites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.focusguard.app.FocusGuardApp
import com.focusguard.app.data.BlockedDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WebsitesViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = (app as FocusGuardApp).database.blockedDomainDao()

    val blockedDomains = dao.getAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addDomain(domain: String) {
        val cleaned = domain.trim().lowercase().removePrefix("https://").removePrefix("http://").removeSuffix("/")
        if (cleaned.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(BlockedDomain(cleaned))
        }
    }

    fun removeDomain(domain: String) = viewModelScope.launch(Dispatchers.IO) {
        dao.deleteByDomain(domain)
    }
}
