package com.airasoft.brawlers.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.airasoft.brawlers.model.Brawler

@Dao
interface BrawlersDatabaseDao {

    @Insert
    fun insert(brawler: Brawler)

    @Update
    fun update(brawler: Brawler)

    @Delete
    fun deleteBrawler(brawler: Brawler)

    @Query("SELECT * FROM brawlers_table WHERE brawler_name LIKE :query")
    fun searchBrawler(query: String): List<Brawler>

    @Query("SELECT * FROM brawlers_table ORDER BY brawler_name")
    fun getAllBrawlers(): List<Brawler>

    /*@Query("DELETE FROM brawlers_table WHERE brawlerId = :brawlerId")
    fun deleteBrawler(brawlerId: Int)*/

    @Query("DELETE FROM brawlers_table")
    fun deleteAllBrawlers()
}