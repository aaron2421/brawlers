package com.airasoft.brawlers.database

import com.airasoft.brawlers.model.Brawler
import javax.inject.Inject

class BrawlersRepository @Inject constructor (
    private val brawlersDao: BrawlersDatabaseDao
    ) {

/*    suspend fun insert(brawler: Brawler) {
        brawlersDao.insert(brawler)
    }

    suspend fun update(brawler: Brawler) {
        brawlersDao.update(brawler)
    }*/

    fun getAllBrawlers(): List<Brawler> {
        return brawlersDao.getAllBrawlers()
    }

    fun deleteBrawler(brawler: Brawler) {
        brawlersDao.deleteBrawler(brawler)
    }

/*    fun deleteAllBrawlers() {
        brawlersDao.deleteAllBrawlers()
    }*/

}