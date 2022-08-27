package com.airasoft.brawlers.detailsbrawler

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airasoft.brawlers.addbrawler.AddBrawlerViewModel
import com.airasoft.brawlers.database.BrawlersDatabaseDao
import com.airasoft.brawlers.model.Brawler

class DetailsBrawlerViewModelFactory(
        private val brawler: Brawler,
        private val dataSource: BrawlersDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsBrawlerViewModel::class.java)) {
            return DetailsBrawlerViewModel(brawler, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}