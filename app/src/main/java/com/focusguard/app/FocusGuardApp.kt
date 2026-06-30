package com.focusguard.app

import android.app.Application
import com.focusguard.app.data.AppDatabase

class FocusGuardApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}
