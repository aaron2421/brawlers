package com.airasoft.brawlers.listbrawlers

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airasoft.brawlers.database.BrawlersDatabaseDao
import com.airasoft.brawlers.database.BrawlersRepository

class ListBrawlersViewModelFactory (
    private val repository: BrawlersRepository,
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListBrawlersViewModel::class.java)) {
                return ListBrawlersViewModel(repository, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}