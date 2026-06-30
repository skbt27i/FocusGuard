package com.focusguard.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedDomainDao {
    @Query("SELECT * FROM blocked_domains ORDER BY domain ASC")
    fun getAll(): Flow<List<BlockedDomain>>

    @Query("SELECT domain FROM blocked_domains")
    fun getDomains(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(domain: BlockedDomain)

    @Delete
    suspend fun delete(domain: BlockedDomain)

    @Query("DELETE FROM blocked_domains WHERE domain = :domain")
    suspend fun deleteByDomain(domain: String)
}
