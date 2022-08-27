package com.airasoft.brawlers.addbrawler

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airasoft.brawlers.database.BrawlersDatabaseDao

class AddBrawlerViewModelFactory(
        private val dataSource: BrawlersDatabaseDao,
        private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBrawlerViewModel::class.java)) {
            return AddBrawlerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}