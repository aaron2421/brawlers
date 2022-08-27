package com.airasoft.brawlers.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.airasoft.brawlers.model.Brawler
import kotlinx.coroutines.flow.Flow

@Dao
interface BrawlersDatabaseDao {

    @Insert
    fun insert(brawler: Brawler)

    @Update
    fun update(brawler: Brawler)

    @Query("SELECT * FROM brawlers_table WHERE brawler_name LIKE :query")
    fun searchBrawler(query: String): Flow<List<Brawler>>

    @Query("SELECT * FROM brawlers_table ORDER BY brawler_type")
    fun getAllBrawlers(): Flow<List<Brawler>>

    @Query("DELETE FROM brawlers_table WHERE brawlerId = :brawlerId")
    fun deleteBrawler(brawlerId: Long)

    @Query("DELETE FROM brawlers_table")
    fun deleteAllBrawlers()
}