package com.airasoft.brawlers.database

import com.airasoft.brawlers.model.Brawler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BrawlersRepository @Inject constructor (
    private val brawlersDao: BrawlersDatabaseDao
    ) {

    suspend fun insert(brawler: Brawler) {
        brawlersDao.insert(brawler)
    }

    suspend fun update(brawler: Brawler) {
        brawlersDao.update(brawler)
    }

    fun getAllBrawlers(): Flow<List<Brawler>> {
        return brawlersDao.getAllBrawlers()
    }

    fun searchBrawler(query: String): Flow<List<Brawler>> {
        return brawlersDao.searchBrawler(query)
    }

    fun deleteBrawler(brawlerId: Long) {
        brawlersDao.deleteBrawler(brawlerId)
    }

    fun deleteAllBrawlers() {
        brawlersDao.deleteAllBrawlers()
    }

}