package com.focusguard.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_domains")
data class BlockedDomain(
    @PrimaryKey val domain: String
)
