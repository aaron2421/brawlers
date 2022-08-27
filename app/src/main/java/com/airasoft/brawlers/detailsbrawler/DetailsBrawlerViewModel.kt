package com.airasoft.brawlers.detailsbrawler

import androidx.lifecycle.*
import com.airasoft.brawlers.database.BrawlersDatabaseDao
import com.airasoft.brawlers.model.Brawler
import com.airasoft.brawlers.model.SnackbarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsBrawlerViewModel(
    brawler: Brawler = Brawler(),
    dataSource: BrawlersDatabaseDao) : ViewModel() {

    val database = dataSource
    val brawler = brawler

    // Navigation config
    private val _navigateToListBrawlers = MutableLiveData<Boolean?>()
    val navigateToListBrawler: LiveData<Boolean?>
        get() = _navigateToListBrawlers

    fun doneNavigating() {
        _navigateToListBrawlers.value = null
    }

    // Snackbar configuration
    private var _showSnackBarEvent = MutableLiveData<SnackbarModel?>()
    val showSnackBarEvent: LiveData<SnackbarModel?>
        get() = _showSnackBarEvent

    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = SnackbarModel()
    }

    fun onEditBrawler(brawler: Brawler) {
        editBrawler(brawler)
    }

    private fun editBrawler(brawler: Brawler) {
        /*viewModelScope.launch(Dispatchers.IO) {
            database.update(brawler)
        }*/
        _showSnackBarEvent.value = SnackbarModel(true, "Brawler editado.")
        _navigateToListBrawlers.value = true
    }

}